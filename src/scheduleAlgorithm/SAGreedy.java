package scheduleAlgorithm;

import java.util.List;

import model.Car;
import model.ChargerSystem;
import model.ChargerSystem.Tuple;

public class SAGreedy extends ScheduleAlgorithm
{

	public SAGreedy(ChargerSystem aChargeSys)
	{
		super(aChargeSys);
	}
	
	@Override
	public void run()
	{
		List<Tuple<Car, Long, Long>> queue = getChargerSystem().getChargeQueue();
		for(int i = queue.size()-1; i >= 0; --i)
		{
			if(getChargerSystem().getTimeSeconds() >= queue.get(i).timeStart())
			{
				if(getChargerSystem().tryChargeCar(queue.get(i).car(), queue.get(i).timeEnd()))
					queue.remove(i);
			}
		}
	}
	
}
