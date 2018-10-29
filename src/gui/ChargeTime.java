package gui;

public class ChargeTime
{
	public long durationInMillis;
	public long second;
	public long minute;
	public long hour;
	public long day;
	
	public ChargeTime(long aDurationInMillis)
	{
		durationInMillis = aDurationInMillis;
		
		second = durationInMillis % 60;
		minute = (durationInMillis / 60) % 60;
		hour = (durationInMillis / (60 * 60)) % 24;
		day = (durationInMillis / (60 * 60 * 24));
	}
	
	public long[] subMinutes(int aNumBlocks, int aBlockSize)
	{
		long h = hour;
		long m = minute;
		
		
		for ( int i = 0; i < aNumBlocks; i++ )
		{
			m -= aBlockSize;
			
			if ( m < 0 )
			{
				h -= 1;
				m = 60 - m;
			}
			
			if ( m >= 60 )
			{
				h += 1;
				m = m - 60;
			}
		}
		
		
		long[] r = { h, m };
		return r;
	}
	
	public long[] addMinutes(int aNum)
	{
		long h = hour;
		long m = minute + aNum;
		
		
		while ( m >= 60 )
		{
			h += 1;
			m = m - 60;
		}
		
		while ( m < 0 )
		{
			h -= 1;
			m = m + 60;
		}
		
		
		long[] r = { h, m };
		return r;
	}
}
