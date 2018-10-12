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
	
	public MasterScheduler(ChargerSystem aSys)
	{
		super();
		chargerSystem = aSys;
		
		schAlgo = new SAGreedy( aSys );
	}
	
	protected void setup()
	{
		MSBehaviour =  new MasterSchedulerBehaviourBasic( this ) ;
		addBehaviour(MSBehaviour);
	}
	
	public MasterSchedulerBehaviourBasic getMSBehaviour()
	{
		return MSBehaviour;
	}
	
	public ChargerSystem getChargerSystem()
	{
		return chargerSystem;
	}
	
	public ScheduleAlgorithm getScheduleAlgorithm()
	{
		return schAlgo;
	}
}
