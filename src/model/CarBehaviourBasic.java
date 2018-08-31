package model;

import jade.core.behaviours.Behaviour;

@SuppressWarnings("serial")
public class CarBehaviourBasic extends Behaviour
{
	private Car car;
	private int cycles;
	private int currentCycle;
	
	public CarBehaviourBasic( Car aCar, int aCycles )
	{
		car = aCar;
		cycles = aCycles;
		currentCycle = 0;
	}

	@Override
	public void action() 
	{
		System.out.println( "Car " + car.getID() + " executing cycle " + currentCycle++ );
		
	}

	@Override
	public boolean done()
	{
		return currentCycle == cycles;
	}

}
