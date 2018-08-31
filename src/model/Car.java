package model;

public class Car 
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
	
	public Car( long aID, long aMaxchargeCapacity, long aCurrentcharge )
	{
		id = aID;
		maxChargeCapacity = aMaxchargeCapacity;
		currentCharge = aCurrentcharge;
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
