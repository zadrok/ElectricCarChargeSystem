package model;

import java.awt.Color;

import boot.GlobalVariables;

// Class for a Car/Long/Long tuple used for start and end charging times
public class QueueItem
{
	private Car car;
	private ChargePoint chargePoint;
	private long timeStart;
	private long timeEnd; // should be time period?
	public Color color;
	
	public QueueItem(Car aCar, ChargePoint aChargePoint, long aTimeStart, long aTimeEnd)
	{
		car = aCar;
		chargePoint = aChargePoint;
		timeStart = aTimeStart;
		timeEnd = aTimeEnd;
		
		color = null;
	}
	
	public QueueItem(Car aCar, long aTimeStart, long aTimeEnd)
	{
		this(aCar, null, aTimeStart, aTimeEnd);
	}
	
	// returns true if the current system time is within the start time and end time
	public boolean isActive()
	{
		long currentTime = GlobalVariables.runTime;
		// is current time within start and end?
//		System.out.println( "currentTime >= timeStart " + currentTime + " >= " + timeStart + " " + (currentTime >= timeStart) + 
//				" && currentTime <= ( timeStart + timeEnd ) " + currentTime + " <= " + timeStart + timeEnd + " " + (currentTime <= ( timeStart + timeEnd )) );
		return currentTime >= timeStart && currentTime <= ( timeStart + timeEnd );
	}
	
	private boolean valueBetweenInclusive(long aValue, long aMin, long aMax)
	{
		return aValue >= aMin && aValue <= aMax;
	}
	
	public Car getCar()
	{
		return car;
	}
	
	public void setCar(Car aCar)
	{
		car = aCar;
	}
	
	public ChargePoint getChargePoint()
	{
		return chargePoint;
	}
	
	public void setChargePoint(ChargePoint aChargePoint)
	{
		chargePoint = aChargePoint;
	}
	
	public long timeStart()
	{
		return timeStart;
	}
	
	public void setTimeStart(long aTime)
	{
		timeStart = aTime;
	}
	
	public long timeEnd()
	{
		return timeEnd;
	}
	
	public void setTimeEnd(long aTime)
	{
		timeEnd = aTime;
	}
}
