package model;

// Class for a Car/Long/Long tuple used for start and end charging times
public class QueueItem
{
	private Car car;
	private ChargePoint chargePoint;
	private long timeStart;
	private long timeEnd;
	
	public QueueItem(Car aCar, ChargePoint aChargePoint, long aTimeStart, long aTimeEnd)
	{
		car = aCar;
		chargePoint = aChargePoint;
		timeStart = aTimeStart;
		timeEnd = aTimeEnd;
	}
	
	public QueueItem(Car aCar, long aTimeStart, long aTimeEnd)
	{
		this(aCar, null, aTimeStart, aTimeEnd);
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
