package model;

import jade.core.Agent;

public class MasterScheduler extends Agent
{
	private MasterSchedulerBehaviourBasic MSBehaviour;
	
	public MasterScheduler()
	{
		super();
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
}
