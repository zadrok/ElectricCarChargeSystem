package model;

import jade.core.behaviours.Behaviour;

@SuppressWarnings("serial")
public class MasterSchedulerBehaviourBasic extends Behaviour
{
	MasterScheduler masSch;
	private int cycles;
	private int currentCycle;
	
	public MasterSchedulerBehaviourBasic( MasterScheduler aMasSch, int aCycles )
	{
		masSch = aMasSch;
		cycles = aCycles;
		currentCycle = 0;
	}

	@Override
	public void action() 
	{
		System.out.println( "MasterScheduler executing cycle " + currentCycle++ );
		
	}

	@Override
	public boolean done()
	{
		return currentCycle == cycles;
	}

}
