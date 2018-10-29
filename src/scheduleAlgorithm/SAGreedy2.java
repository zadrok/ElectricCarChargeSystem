/*************************
 * SAGreedy.java
 * 
 * Extends the SchedulerAlgorithm class to implement a
 * greedy scheduling algorithm
 * 
 */

package scheduleAlgorithm;

import java.util.List;

import model.Car;
import model.ChargerSystem;
import model.ChargerSystem.Tuple;

public class SAGreedy2 extends ScheduleAlgorithm
{
	// Constructor for algorithm
	public SAGreedy2(ChargerSystem aChargeSys)
	{
		super(aChargeSys);
		name = "Greedy2";
	}
	
	@Override
	public void run()
	{
		// Attempt to charge the top car in the queue
		List<Tuple<Car, Long, Long>> queue = getChargerSystem().getChargeQueue();
		for(int i = queue.size()-1; i >= 0; --i)
		{
			if(getChargerSystem().getTimeSeconds() >= queue.get(i).timeStart())
			{
				// If we successfully start charging the car, we can remove it from the queue
				if(getChargerSystem().tryChargeCar(queue.get(i).car(), queue.get(i).timeEnd()))
				{
					getChargerSystem().getChargeQueueOLD().add( queue.get(i) );
					queue.remove(i);
				}
			}
		}
	}
	
}
