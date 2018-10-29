/*************************
 * ScheduleAlgorithm.java
 * 
 * A class for scheduling algorithms. While this class won't do
 * much by itself, it can be inherited for a scheduling algorithm
 * 
 */

package scheduleAlgorithm;

import model.ChargerSystem;

public class ScheduleAlgorithm
{
	protected ChargerSystem chargeSys;
	protected String name;
	
	// Constructor for algorithm
	public ScheduleAlgorithm(ChargerSystem aChargeSys)
	{
		chargeSys = aChargeSys;
		name = "Default";
	}
	
	public String toString()
	{
		return name;
	}
	
	// Run function
	public void run()
	{
		
	}
	
	// Get charger system referenced by algorithm
	protected ChargerSystem getChargerSystem()
	{
		return chargeSys;
	}
}
