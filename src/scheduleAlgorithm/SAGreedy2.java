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

public class SAGreedy2 extends ScheduleAlgorithm
{
	// Constructor for algorithm
	public SAGreedy2(ChargerSystem aChargeSys, String aName)
	{
		super(aChargeSys, aName);
	}
	
	@Override
	public void run()
	{
		// Attempt to charge the top car in the queue
		ArrayList<QueueItem> queue = getChargerSystem().getChargeQueue();
		for(int i = queue.size()-1; i >= 0; --i)
		{
			if(getChargerSystem().getTimeSeconds() >= queue.get(i).timeStart())
			{
				// If we successfully start charging the car, we can remove it from the queue
				if(getChargerSystem().tryChargeCar(queue.get(i).getCar(), queue.get(i).timeEnd()))
				{
					getChargerSystem().getChargeQueueOLD().add( queue.get(i) );
					queue.remove(i);
				}
			}
		}
	}
	
}
