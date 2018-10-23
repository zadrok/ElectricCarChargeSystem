/*************************
 * MasterScheduler.java
 * 
 * Handles the scheduling of cars for charge points
 * 
 */

package model;

import jade.core.Agent;
import scheduleAlgorithm.SAGreedy;
import scheduleAlgorithm.ScheduleAlgorithm;

@SuppressWarnings("serial")
public class MasterScheduler extends Agent
{
	private MasterSchedulerBehaviourBasic MSBehaviour;
	private ChargerSystem chargerSystem;
	private ScheduleAlgorithm schAlgo;
	
	// Constructor for master scheduler
	public MasterScheduler(ChargerSystem aSys)
	{
		super();
		chargerSystem = aSys;
		
		// Pick scheduling algorithm
		schAlgo = new SAGreedy( aSys );
	}
	
	// Setup behaviour of scheduler
	protected void setup()
	{
		MSBehaviour =  new MasterSchedulerBehaviourBasic( this ) ;
		addBehaviour(MSBehaviour);
	}
	
	// Get behaviour of scheduler
	public MasterSchedulerBehaviourBasic getMSBehaviour()
	{
		return MSBehaviour;
	}
	
	// Get charger system
	public ChargerSystem getChargerSystem()
	{
		return chargerSystem;
	}
	
	// Get scheduling algorithm
	public ScheduleAlgorithm getScheduleAlgorithm()
	{
		return schAlgo;
	}
}
