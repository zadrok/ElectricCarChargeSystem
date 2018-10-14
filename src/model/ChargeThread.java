package model;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

//ChargeThread to handle charging of a car
	public class ChargeThread implements Runnable
	{
		private List<ChargePoint> chargePoints;
		private int pollRate;
		private long lastPoll;
		private AtomicBoolean stop;
		
		public ChargeThread(List<ChargePoint> aChargers, int aPollRate)
		{
			stop = new AtomicBoolean(false);
			chargePoints = Collections.synchronizedList(aChargers);
			pollRate = aPollRate;
			lastPoll = System.currentTimeMillis();
		}
		
		public void add(ChargePoint aCharger)
		{
			synchronized(chargePoints)
			{
				chargePoints.add(aCharger);
			}
		}
		
		public void stop()
		{
			// Stop running
			stop.set(true);
		}
		
		public boolean chargeStatus()
		{
			return !stop.get();
		}
		
		public boolean getCarChargeStatus(Car aCar)
		{
			for(int i = 0; i < chargePoints.size(); ++i){
				if(chargePoints.get(i).GetConnectedCar() == aCar.getID())
					return true;
			}
			return false;
		}
		
		public void run()
		{
			while(!stop.get())
			{
				// Increment car charge
				synchronized(chargePoints)
				{
//					System.out.println(chargePoints.size());

					for(int i = 0; i < chargePoints.size(); ++i)
					{
//						System.out.println("Charging at point " + i);
						chargePoints.get(i).performCharge();
					}
				}
				long pollDelay = (1000/pollRate) - (System.currentTimeMillis() - lastPoll);
				lastPoll = System.currentTimeMillis() + pollDelay;
				// Sleep for given period of time
				try {
					Thread.sleep(pollDelay < 0 ? 0 : pollDelay);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}