/*************************
 * Car.java
 * 
 * A agent representing a car. Can be created
 * using pre-determined values through the default
 * constructor, or by self-chosen values.
 * 
 */

package model;

import boot.GlobalVariables;
import gui.ChargeTime;
import jade.core.Agent;

// Cars typically range from around 20kWh (~120km / charge) to
// 90 kWh (~360km / charge).
// This equates to a discharge of around 8kWh / hour.

@SuppressWarnings("serial")
public class Car extends Agent
{
	// States represent a car's current status
	public static enum STATE { NONE, IDLE, CHARGE, CHARGING, BURN };

	private long id;
	private long flags;
	private double maxChargeCapacity;
	private double currentCharge;
	private double dischargeRate;

	private boolean wantCharge;
	private boolean waitingForCharge;
	
	// looked at by gui
	private STATE carState;

	// used in gui
	private double startAngle;
	
	// data of when car is used
	private CarProfile profile;

	// Default constructor for a car.
	public Car()
	{
		// Will call Agent constructor followed by initializing all default values
		super();
		wantCharge = false;
		waitingForCharge = false;
		id = -1;
		maxChargeCapacity = 70;
		currentCharge = 70;
		dischargeRate = (8.0/GlobalVariables.timeScale)/GlobalVariables.chargeInterval;
		carState = STATE.IDLE;
		startAngle = 0;
		flags = 1;
		profile = new CarProfile(this);
	}

	// Secondary constructor
	public Car(long aID, long aMaxChargeCapacity, long aCurrentCharge, long aFlags)
	{
		// Will first call our default constructor
		this();
		// ID Represents the Car's identifier
		id = aID;
		// maxChargeCapacity is the maximum amount of charge a car can hold
		maxChargeCapacity = aMaxChargeCapacity;
		// currentCharge is the current charge of the car at creation
		currentCharge = aCurrentCharge;
		// flags are used to determine which chargers the car can use. 1 is the default charging flag
		flags |= aFlags;
	}
	
	// override of toString
	@Override
	public String toString()
	{
		return getLocalName();
	}
	
	//return array of two strings, start charge time and charge time duration
	public String[] getChargeTimes()
	{
		String[] times = new String[2];
		
		// find longest time a car can charge
		
		int highestStartIndex = 0;
		int highestChainLength = 0;
		
		int currentStartIndex = 0;
		int currentChainLength = 0;
		
		int numberLookingFor = 0;
		
		// look for the longest change of 0
		
		for ( int i = 0; i < profile.getUsage().size(); i++ )
		{
			// the current value
			int x = profile.getUsage().get(i);
			
			if ( x == numberLookingFor )
			{
				if ( currentChainLength == 0 )
					currentStartIndex = i;
				
				currentChainLength += 1;
			}
			else
			{
				currentChainLength = 0;
			}
			
			if ( currentChainLength > highestChainLength )
			{
				highestStartIndex = currentStartIndex;
				highestChainLength = currentChainLength;
			}
		}
		
//		System.out.println( profile.getUsage().toString() );
//		System.out.println( "profile.getUsage().size() " + profile.getUsage().size() );
//		System.out.println( "highestStartIndex " + highestStartIndex );
//		System.out.println( "highestChainLength " + highestChainLength );
//		System.out.println( "--------------------------------------------- " );
		
		
		// new we know the index to start from and for how long to go for
		// we can use the number of hours and polls per hour to convert this to a time
		// we use blocks of time in 30 minutes to take getTimesPerHour() / 2
		// hour and duration are in 30 minute blocks so multiply by 30 to get minutes
		long start = (long) Math.ceil( highestStartIndex / ( profile.getTimesPerHour() / 2 ) ) * 30 * 60;
		long duration = (long) Math.floor( highestChainLength / ( profile.getTimesPerHour() / 2 ) ) * 30 * 60;
		
		// limit duration to 4 hours
		if ( duration > 4*60*60 )
			duration = 4*60*60;
		
//		System.out.println( "hour " + hour );
//		new ChargeTime(hour).print();
//		System.out.println( "duration " + duration );
//		new ChargeTime(duration).print();
		
		// if the current time is past the start time
		// find the current time days and add that number of days to the time
		// adjust for how long the system has been running
		ChargeTime ct = new ChargeTime( GlobalVariables.runTime );
		if ( GlobalVariables.runTime >= start-60 )
			start += ( ct.day + 1 ) * ( 24 * 60 * 60 );
		
		times[0] = ""+start;
		times[1] = ""+duration;
		
		return times;
	}

	// Used to add a behavior to the car
	protected void setup()
	{
		addBehaviour( new CarBehaviourBasic( this ) );
	}
	
	// Check if the car wants to charge
	public boolean isWantCharge()
	{
		return wantCharge;
	}
	
	// return the cars usage profile
	public CarProfile getProfile()
	{
		return profile;
	}
	
	// Set if the car wants to charge
	public void setWantCharge(boolean aBool)
	{
		wantCharge = aBool;
	}
	
	// Check if the car is waiting for charge
	public boolean isWaitingForCharge()
	{
		return waitingForCharge;
	}
	
	// Set if the car is waiting for charge
	public void setWaitingForCharge(boolean aBool)
	{
		waitingForCharge = aBool;
	}

	// Get the ID of the car
	public long getID()
	{
		return id;
	}

	// Set the ID of the car
	public void setID(long aID)
	{
		id = aID;
	}
	
	// Get the flags of a car
	public long getFlags()
	{
		return flags;
	}

	// Get the maximum charge capacity of the car
	public double getMaxChargeCapacity()
	{
		return maxChargeCapacity;
	}

	// Set the maximum charge capacity of the car
	public void setMaxChargeCapacity(long aMaxchargeCapacity)
	{
		maxChargeCapacity = aMaxchargeCapacity;
	}

	// Get the current charge of the car
	public double getCurrentCharge()
	{
		return currentCharge;
	}

	// Set the current charge of the car
	public void setCurrentCharge(double aCurrentcharge)
	{
		// Clamps the charge between 0 and maxChargeCapacity
		currentCharge = Math.max(0, Math.min(maxChargeCapacity, aCurrentcharge));
	}

	// Get start angle of the car
	public double getStartAngle()
	{
		return startAngle;
	}

	// Set start angle of the car
	public void setStartAngle(double aAngle)
	{
		startAngle = aAngle;
	}

	// Retrieve current car state
	public STATE getCarState()
	{
		return carState;
	}

	// Get current car state as string
	public String getCarStateString()
	{
		return getCarState().toString();
	}
	
	// Set current car state
	public void setCarState(STATE aState)
	{
		carState = aState;
	}
	
	// Discharge the car based on current dischargeRate
	public void discharge()
	{
		// We cannot discharge the car if it is charging
		if(carState == STATE.CHARGING)
			return;
		setCurrentCharge(getCurrentCharge() - dischargeRate);
		// If the car has less than 10% charge, we want to charge
		if(currentCharge < (maxChargeCapacity / 10))
		{
			carState = STATE.CHARGE;
			
			if ( !isWaitingForCharge() )
				setWantCharge(true);
		}
	}
}
