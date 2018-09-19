package model;

import jade.core.Agent;
import java.lang.Thread;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("serial")
public class Car extends Agent
{
	public static enum STATE { NONE, IDEL, CHARGE };
	
	private long id;
	private long maxChargeCapacity;
	private long currentCharge;
	
	// looked at by gui
	private STATE carState;
	
	// used in gui
	private double startAngle;
	
	private ChargeThread chargeThread;
	
	public Car()
	{
		super();
		id = -1;
		maxChargeCapacity = 1000;
		currentCharge = 0;
		
		initCommon();
	}
	
	public Car(long aID, long aMaxChargeCapacity, long aCurrentCharge)
	{
		super();
		id = aID;
		maxChargeCapacity = aMaxChargeCapacity;
		currentCharge = aCurrentCharge;
		
		initCommon();
	}
	
	private void initCommon()
	{
		carState = STATE.IDEL;
		startAngle = 0;
	}
	
	protected void setup()
	{	
		addBehaviour( new CarBehaviourBasic( this ) );
		chargeThread = new ChargeThread(10, 30, this);
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
	
	public double getStartAngle()
	{
		return startAngle;
	}
	
	public void setStartAngle(double aAngle)
	{
		startAngle = aAngle;
	}
	
	public boolean isRunning()
	{
		if ( chargeThread == null )
			return false;
		
		return chargeThread.isRunning();
	}
	
	public STATE getCarState()
	{
		return carState;
	}
	
	public String getCarStateString()
	{
		return getCarState().toString();
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
		private AtomicBoolean stop;
		
		public ChargeThread(long aChargeRate, long aChargePeriod, Car aCar)
		{
			// Set member variables
			chargeRate = aChargeRate;
			chargePeriod = aChargePeriod;
			car = aCar;
			stop  = new AtomicBoolean( false );
		}
		
		public void stop()
		{
			// Stop running
			stop.set( true );
		}
		
		public void run()
		{
			while( !stop.get() )
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
		
		public boolean isRunning()
		{
			return !stop.get();
		}
	}
	
}
