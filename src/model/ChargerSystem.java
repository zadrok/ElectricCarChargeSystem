package model;

import java.util.ArrayList;

import jade.core.Profile;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class ChargerSystem 
{
	private Runtime runtime;
	private Profile profileCars;
	private ContainerController containerCars;
	
	private Profile profileMasterScheduler;
	private ContainerController containerMasterScheduler;
	
	public ChargerSystem()
	{
		
	}
	
	public void initJadeAgents()
	{
		runtime = Runtime.instance();
		
		profileMasterScheduler = new jade.core.ProfileImpl();
		profileMasterScheduler.setParameter(Profile.CONTAINER_NAME, "Master Scheduler");
		profileMasterScheduler.setParameter(Profile.MAIN_HOST, "localhost");
		containerMasterScheduler = runtime.createAgentContainer(profileMasterScheduler);
		
		profileCars = new jade.core.ProfileImpl();
		profileCars.setParameter(Profile.CONTAINER_NAME, "Cars");
		profileCars.setParameter(Profile.MAIN_HOST, "localhost");
		containerCars = runtime.createAgentContainer(profileCars);
		
		createMasterScheduler();
		
		for ( int i = 0; i < 10; i++ )
		{
			createCarAgent( i, 1000, 0 );
		}
	}
	
	public void createMasterScheduler()
	{
		try
		{
			ArrayList<Object> objList = new ArrayList<>();
			
			AgentController ac = containerMasterScheduler.createNewAgent( "Master Scheduler", "model.MasterScheduler", objList.toArray() );
			ac.start();
		}
		catch (StaleProxyException e)
		{
			e.printStackTrace();
		}
	}
	
	public void createCarAgent( long aID, long aMaxchargeCapacity, long aCurrentcharge )
	{
		try
		{
			ArrayList<Object> objList = new ArrayList<>();
			objList.add( aID );
			objList.add( aMaxchargeCapacity );
			objList.add( aCurrentcharge );
			
			AgentController ac = containerCars.createNewAgent( "Car" + aID, "model.Car", objList.toArray() );
			ac.start();
		}
		catch (StaleProxyException e)
		{
			e.printStackTrace();
		}
	}
	
}
