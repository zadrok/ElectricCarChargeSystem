package model;

import jade.core.Agent;

public class MasterScheduler extends Agent
{
	
	
	public MasterScheduler()
	{
		
	}
	
	protected void setup()
	{
		Object[] args = getArguments();
		
		addBehaviour( new MasterSchedulerBehaviourBasic( this, 4 ) );
	}
}
