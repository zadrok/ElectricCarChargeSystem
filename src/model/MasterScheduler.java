package model;

import jade.core.Agent;

public class MasterScheduler extends Agent
{
	
	
	public MasterScheduler()
	{
		super();
	}
	
	protected void setup()
	{
		addBehaviour( new MasterSchedulerBehaviourBasic( this ) );
	}
}
