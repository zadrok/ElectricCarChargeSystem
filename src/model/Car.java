package model;

import boot.GlobalVariables;
import jade.core.Agent;

// Cars typically range from around 20kWh (~120km / charge) to
// 90 kWh (~360km / charge).
// This equates to a discharge of around 8kWh / hour.

@SuppressWarnings("serial")
public class Car extends Agent
{
	public static enum STATE { NONE, IDLE, CHARGE, CHARGING, BURN };

	private long id;
	private double maxChargeCapacity;
	private double currentCharge;
	private double dischargeRate;

	private boolean wantCharge;
	private boolean waitingForCharge;
	
	// looked at by gui
	private STATE carState;

	// used in gui
	private double startAngle;

	public Car()
	{
		super();
		wantCharge = false;
		waitingForCharge = false;
		id = -1;
		maxChargeCapacity = 70;
		currentCharge = 70;
		dischargeRate = (8.0/60.0)/GlobalVariables.chargeInterval;
		carState = STATE.IDLE;
		startAngle = 0;
	}

	public Car(long aID, long aMaxChargeCapacity, long aCurrentCharge)
	{
		this();
		id = aID;
		maxChargeCapacity = aMaxChargeCapacity;
		currentCharge = aCurrentCharge;
	}

	protected void setup()
	{
		addBehaviour( new CarBehaviourBasic( this ) );
	}
	
	public boolean isWantCharge()
	{
		return wantCharge;
	}
	
	public void setWantCharge(boolean aBool)
	{
		wantCharge = aBool;
	}
	
	public boolean isWaitingForCharge()
	{
		return waitingForCharge;
	}
	
	public void setWaitingForCharge(boolean aBool)
	{
		waitingForCharge = aBool;
	}

	public long getID()
	{
		return id;
	}

	public void setID(long aID)
	{
		id = aID;
	}

	public double getMaxChargeCapacity()
	{
		return maxChargeCapacity;
	}

	public void setMaxChargeCapacity(long aMaxchargeCapacity)
	{
		maxChargeCapacity = aMaxchargeCapacity;
	}

	public double getCurrentCharge()
	{
		return currentCharge;
	}

	public void setCurrentCharge(double aCurrentcharge)
	{
		currentCharge = Math.max(0, Math.min(maxChargeCapacity, aCurrentcharge));
	}

	public double getStartAngle()
	{
		return startAngle;
	}

	public void setStartAngle(double aAngle)
	{
		startAngle = aAngle;
	}

	public STATE getCarState()
	{
		return carState;
	}


	public String getCarStateString()
	{
		return getCarState().toString();
	}
	
	public void setCarState(STATE aState)
	{
		carState = aState;
	}
	
	public void discharge()
	{
		if(carState == STATE.CHARGING)
			return;
		setCurrentCharge(getCurrentCharge() - dischargeRate);
		if(currentCharge < (maxChargeCapacity / 10))
		{
			carState = STATE.CHARGE;
			
			if ( !isWaitingForCharge() )
				setWantCharge(true);
		}
	}
}
