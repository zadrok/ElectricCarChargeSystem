package model;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

//ChargeThread to handle charging of a car
	public class ChargeThread implements Runnable
	{
		private List<ChargePoint> chargePoints;
		private int chargePeriod;
		private AtomicBoolean stop;
		
		public ChargeThread(List<ChargePoint> aChargers, int aChargePeriod)
		{
			stop = new AtomicBoolean(false);
			chargePoints = Collections.synchronizedList(aChargers);
			chargePeriod = aChargePeriod;
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
		
		public boolean attachCar(Car aCar)
		{
			for(int i = 0; i < chargePoints.size(); ++i)
			{
				if(chargePoints.get(i).GetConnectedCar() == -1)
				{
					chargePoints.get(i).AddCar(aCar);
					return true;
				}
			}
			return false;
		}
		
		public void run()
		{
			while(!stop.get())
			{
				// Sleep for given period of time
				try {
					Thread.sleep(chargePeriod);
				} catch (InterruptedException e) {
					return;
				}
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
			}
		}
	}