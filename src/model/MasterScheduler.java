package model;

public class MasterScheduler
{
	private long outlets;
	private long chargeInterval; // in minutes
	
	public MasterScheduler()
	{
		outlets = 50;
		chargeInterval = 30;
	}
	
	public MasterScheduler( long aNumOutlets, long aChargeInterval )
	{
		outlets = aNumOutlets;
		chargeInterval = aChargeInterval;
	}
	
	public long getOutlets()
	{
		return outlets;
	}
	
	public void setOutlets( long aNumOutlets )
	{
		outlets = aNumOutlets;
	}
	
	public long getChargeInterval()
	{
		return chargeInterval;
	}
	
	public void setChargeInterval( long aChargeInterval )
	{
		chargeInterval = aChargeInterval;
	}
}
