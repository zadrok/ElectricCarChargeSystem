/*************************
 * SAGreedy.java
 * 
 * Extends the SchedulerAlgorithm class to implement a
 * greedy scheduling algorithm
 * 
 */

package scheduleAlgorithm;

import java.util.ArrayList;

import model.*;

public class SAGreedy extends ScheduleAlgorithm
{
	// Constructor for algorithm
	public SAGreedy(ChargerSystem aChargeSys, String aName)
	{
		super(aChargeSys, aName);
	}
	
	// Request a charge using timestamp
	public boolean requestCharge(Car aCar, long aTimeStart, long aTimePeriod)
	{
		// Don't allow a car to be added to the queue twice
		if ( getChargerSystem().inQueue(aCar) )
			return false;
		
		// car not in queue, try and add it
		// find charge point free at give time
		
		// find all charge points that do not have a charge happening at a given time
		ArrayList<ChargePoint> freePoints = freePoints(aTimeStart);
		
		// cull points that have a charge happen during the time requested
		ArrayList<ChargePoint> culledPoints = cullPoints(freePoints, aTimeStart, aTimePeriod);
		
		// if culledPoints has any points in it then set one to be used and return true
		if ( culledPoints.size() > 0 )
		{
			QueueItem item = new QueueItem( aCar, culledPoints.get( getChargerSystem().getRandomInt( culledPoints.size()-1 ) ), aTimeStart, aTimePeriod );
			getChargerSystem().queueLock();
			try
			{
				getChargerSystem().getChargeQueue().add( item );
			}
			finally
			{
				getChargerSystem().queueUnlock();
			}
			return true;
		}
		
		// if there was no charge points free then return false
		// car will have to find a different time
		return false;
	}
	
	// return charge points that are free at a given time
	private ArrayList<ChargePoint> freePoints(long aTime)
	{
		ArrayList<ChargePoint> points = new ArrayList<>();
		
		// go through each charge point
		for ( ChargePoint point : getChargerSystem().getChargePoints() )
		{
			// get all QueueItems for this charge point
			ArrayList<QueueItem> items = getAllItems(point);
			// check to see if this charge point is free at this time
			if ( chargePointFree(items, aTime) )
			{
				// it is free! add to list
				points.add( point );
			}
		}
		
		return points;
	}
	
	// return new list of points that are free for the duration of the time
	private ArrayList<ChargePoint> cullPoints(ArrayList<ChargePoint> aList, long aStartTime, long aTimePeriod)
	{
		ArrayList<ChargePoint> points = new ArrayList<>();
		
		// check each chargePoint
		for ( ChargePoint point : aList )
		{
			// get all QueueItems for this charge point
			ArrayList<QueueItem> items = getAllItems(point);
			
			//check if any of the items conflict with times
			if ( hasRequiredTime( items, aStartTime, aTimePeriod ) )
			{
				// if true then add this charge point
				points.add(point);
			}
			
		}
		
		return points;
	}
	
	private boolean hasRequiredTime(ArrayList<QueueItem> aItems, long aStartTime, long aTimePeriod)
	{
		// check each item
		for ( QueueItem item : aItems )
		{
			// queue item start and end time
			long stItem = item.timeStart();
			long etItem = item.timeStart() + item.timeEnd();
			
			// start and end time we want to fit
			long st = aStartTime;
			long et = aStartTime + aTimePeriod;
			
			// make sure there is no overlap in times
			boolean a = valueBetweenExclusive(stItem, st, et);
			boolean b = valueBetweenExclusive(etItem, st, et);
			
			boolean c = valueBetweenExclusive(st, stItem, etItem);
			boolean d = valueBetweenExclusive(et, stItem, etItem);
			
			boolean isOverlap = a || b || c || d;
			
			// if there is overlap then return false
			if ( isOverlap )
			{
				return false;
			}
		}
		
		return true;
	}
	
	private boolean valueBetweenInclusive(long aValue, long aMin, long aMax)
	{
		return aValue >= aMin && aValue <= aMax;
	}
	
	private boolean valueBetweenExclusive(long aValue, long aMin, long aMax)
	{
		return aValue > aMin && aValue < aMax;
	}
	
	// return true if this time is not in any of the queue items
	private boolean chargePointFree(ArrayList<QueueItem> aItems, long aTime)
	{
		for ( QueueItem item : aItems )
		{
			// is aTime between queue times?
			long startTime = item.timeStart();
			long timeEnd = item.timeStart() + item.timeEnd();
			
			// between two numbers?
			if ( valueBetweenInclusive( aTime, startTime, timeEnd ) )
				return false;
		}
		
		// wasn't between any of the queue item times so return true! it is free!
		return true;
	}
	
	// find and return all queue items that belong to this charge point
	private ArrayList<QueueItem> getAllItems(ChargePoint aPoint)
	{
		ArrayList<QueueItem> items = new ArrayList<>();
		
		// current queue
		for ( QueueItem item : getChargerSystem().getChargeQueue() )
		{
			if ( item.getChargePoint() == aPoint )
				items.add(item);
		}
		
		// expired queue
		for ( QueueItem item : getChargerSystem().getChargeQueueOLD() )
		{
			if ( item == null || item.getChargePoint() == null || aPoint == null )
				continue;
			
			if ( item.getChargePoint() == aPoint )
				items.add(item);
		}
		
		return items;
	}
	
}
