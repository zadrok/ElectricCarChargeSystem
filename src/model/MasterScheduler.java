package model;

import jade.core.Agent;

@SuppressWarnings("serial")
public class MasterScheduler extends Agent
{
	private MasterSchedulerBehaviourBasic MSBehaviour;
	private ChargerSystem chargerSystem;
	
	public MasterScheduler(ChargerSystem aSys)
	{
		super();
		chargerSystem = aSys;
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
}
