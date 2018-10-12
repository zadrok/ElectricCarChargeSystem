package scheduleAlgorithm;

import model.ChargerSystem;

public class ScheduleAlgorithm
{
	protected  ChargerSystem chargeSys;
	
	public ScheduleAlgorithm(ChargerSystem aChargeSys)
	{
		chargeSys = aChargeSys;
	}
	
	public void run()
	{
		
	}
	
	protected ChargerSystem getChargerSystem()
	{
		return chargeSys;
	}
}
