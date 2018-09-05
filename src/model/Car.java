package model;

import jade.core.Agent;
import java.lang.Thread;

@SuppressWarnings("serial")
public class Car extends Agent
{
	private long id;
	private long maxChargeCapacity;
	private long currentCharge;
	
	private ChargeThread chargeThread;
	
	public Car()
	{
		id = -1;
		maxChargeCapacity = 1000;
		currentCharge = 0;
	}
	
	protected void setup()
	{
		Object[] args = getArguments();
		
		id = (long)args[0];
		maxChargeCapacity = (long)args[1];
		currentCharge = (long)args[2];
		
		addBehaviour( new CarBehaviourBasic( this ) );
	}
	
	public long getID()
	{
		return id;
	}
	
	public void setID(long aID)
	{
		id = aID;
	}
	
	public long getMaxChargeCapacity()
	{
		return maxChargeCapacity;
	}
	
	public void setMaxChargeCapacity(long aMaxchargeCapacity)
	{
		maxChargeCapacity = aMaxchargeCapacity;
	}
	
	public long getCurrentCharge()
	{
		return currentCharge;
	}
	
	public void setCurrentCharge(long aCurrentcharge)
	{
		currentCharge = (getMaxChargeCapacity() > aCurrentcharge ? aCurrentcharge : getMaxChargeCapacity());
	}
	
	public void beginCharge(long aChargeRate)
	{
		// If charge thread is non-null, already exists
		// Charge rate must be positive. Consider unsigned?
		if(!(chargeThread == null) || aChargeRate < 0)
			return;
		// Create charge thread using given rate and default of 1000ms rate
		chargeThread = new ChargeThread(aChargeRate, 1000, this);
		// Run thread
		chargeThread.run();
	}
	
	public void stopCharge()
	{
		// If charge thread is null, doesn't exist
		if(chargeThread == null)
			return;
		// Call stop
		chargeThread.stop();
		// Make thread null
		chargeThread = null;
	}
	
	// ChargeThread to handle charging of a car
	public class ChargeThread implements Runnable
	{
		// Rate of charge
		private long chargeRate;
		// Frequency of charge
		private long chargePeriod;
		// Parent car
		private Car car;
		// Stop
		private boolean stop = false;
		
		public ChargeThread(long aChargeRate, long aChargePeriod, Car aCar)
		{
			// Set member variables
			chargeRate = aChargeRate;
			chargePeriod = aChargePeriod;
			car = aCar;
		}
		
		public void stop()
		{
			// Stop running
			stop = true;
		}
		
		public void run()
		{
			while(!stop)
			{
				// Sleep for given period of time
				try {
					Thread.sleep(chargePeriod);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Increment car charge
				car.setCurrentCharge(car.getCurrentCharge() + chargeRate);
				System.out.println( "Car " + car.getID() + " has charge " + car.getCurrentCharge() );
			}
		}
	}
}
