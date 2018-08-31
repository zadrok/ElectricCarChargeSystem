package model;

import jade.core.Agent;

@SuppressWarnings("serial")
public class Car extends Agent
{
	private long id;
	private long maxChargeCapacity;
	private long currentCharge;
	
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
		
		addBehaviour( new CarBehaviourBasic( this, 4 ) );
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
		currentCharge = aCurrentcharge;
	}
}
