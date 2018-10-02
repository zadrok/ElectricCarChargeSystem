package model;

import jade.core.Agent;
import java.lang.Thread;
import java.util.concurrent.atomic.AtomicBoolean;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class Car extends Agent
{
	public static enum STATE { NONE, IDLE, CHARGE, CHARGING };

	private long id;
	private long maxChargeCapacity;
	private long currentCharge;
	private long dischargeRate;

	// looked at by gui
	private STATE carState;

	// used in gui
	private double startAngle;

	public Car()
	{
		super();
		id = -1;
		maxChargeCapacity = 1000;
		currentCharge = 1000;
		dischargeRate = 10;

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
		carState = STATE.IDLE;
		startAngle = 0;
	}

	protected void setup()
	{
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
		setCurrentCharge(getCurrentCharge() - dischargeRate);
		if(currentCharge < (maxChargeCapacity / 10))
			carState = STATE.CHARGE;
	}
}
