package model;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Profile;
import jade.core.Runtime;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import jade.wrapper.gateway.JadeGateway;

public class ChargerSystem 
{
	private Runtime runtime;
	private Profile profileCars;
	private ContainerController containerCars;
	
	private Profile profileMasterScheduler;
	private ContainerController containerMasterScheduler;
	
	private ArrayList<String> carAgentNames;
	private String masterSchedulerName;
	
	public ChargerSystem()
	{
		
	}
	
	public void initJadeAgents()
	{
		carAgentNames = new ArrayList<>();
		
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
		
		messageAllCars(ACLMessage.INFORM, "HI CAR");
		messageMasterScheduler(ACLMessage.INFORM, "Master Scheduler");
	}
	
	public void createMasterScheduler()
	{
		try
		{
			ArrayList<Object> objList = new ArrayList<>();
			
			masterSchedulerName = "Master Scheduler";
			
			AgentController ac = containerMasterScheduler.createNewAgent( masterSchedulerName, "model.MasterScheduler", objList.toArray() );
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
			
			String name = "Car " + aID;
			
			AgentController ac = containerCars.createNewAgent( name, "model.Car", objList.toArray() );
			ac.start();
			
			carAgentNames.add( name );
		}
		catch (StaleProxyException e)
		{
			e.printStackTrace();
		}
	}
	
	public void messageAllCars(int aMeggageType, String aMessage)
	{
		System.out.println("Sending message to all Cars: " + aMessage);
		
		try
		{
			OneShotBehaviour behaviour = ( new OneShotBehaviour() 
			{
				public void action() 
				{
					ACLMessage msg = new ACLMessage(aMeggageType);
					msg.setContent(aMessage);
					
					for (String lName : carAgentNames)
					{
						msg.addReceiver(new AID(lName, AID.ISLOCALNAME));
					}
					
					
					myAgent.send(msg);
				}
			} );
			
			JadeGateway.execute(behaviour);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Done sending message to all Cars");
	}
	
	public void messageMasterScheduler(int aMeggageType, String aMessage)
	{
		System.out.println("Sending message to Master Scheduler: " + aMessage);
		
		try
		{
			OneShotBehaviour behaviour = ( new OneShotBehaviour() 
			{
				public void action() 
				{
					ACLMessage msg = new ACLMessage(aMeggageType);
					msg.setContent(aMessage);
					
					msg.addReceiver(new AID(masterSchedulerName, AID.ISLOCALNAME));
					
					myAgent.send(msg);
				}
			} );
			
			JadeGateway.execute(behaviour);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Done sending message to Master Scheduler");
	}
	
}
