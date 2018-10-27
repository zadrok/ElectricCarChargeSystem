/*************************
 * ChargeThread.java
 * 
 * Handles charging of cars from charge points using
 * multi-threded behaviours
 * 
 */

package model;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import boot.GlobalVariables;

//ChargeThread to handle charging of a car
public class ChargeThread implements Runnable
{
	private List<ChargePoint> chargePoints;
	private int pollRate;
	private long lastPoll;
	private AtomicBoolean stop;
	
	// Constructor for charge thread
	public ChargeThread(List<ChargePoint> aChargers, int aPollRate)
	{
		stop = new AtomicBoolean(false);
		chargePoints = Collections.synchronizedList(aChargers);
		pollRate = aPollRate;
		lastPoll = System.currentTimeMillis();
	}
	
	// Add charge point to charge thread
	public void add(ChargePoint aCharger)
	{
		synchronized(chargePoints)
		{
			chargePoints.add(aCharger);
		}
	}
	
	// Stop thread
	public void stop()
	{
		// Stop running
		stop.set(true);
	}
	
	// Get status of charge thread
	public boolean chargeStatus()
	{
		return !stop.get();
	}
	
	// Get charge status of car
	public boolean getCarChargeStatus(Car aCar)
	{
		for(int i = 0; i < chargePoints.size(); ++i){
			if(chargePoints.get(i).GetConnectedCar() == aCar.getID())
				return true;
		}
		return false;
	}
	
	// Thread runner
	public void run()
	{
		// Loop while not stopped
		while(!stop.get())
		{
			// increment run time
			GlobalVariables.runTime++;
			
			// Increment car charge
			synchronized(chargePoints)
			{
				for(int i = 0; i < chargePoints.size(); ++i)
				{
					chargePoints.get(i).performCharge();
				}
			}
			// Determine polling delay based on last poll
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