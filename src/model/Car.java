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
}
