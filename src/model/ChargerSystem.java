package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
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
	
	private ArrayList<Car> carAgents;
	private MasterScheduler masterSchedulerAgent;
	
	private List<ChargePoint> chargePoints;
	private ChargeThread chargeThread;
	private Thread chargeThreadThread;
	
	public ChargerSystem()
	{
		chargePoints = Collections.synchronizedList(new ArrayList<ChargePoint>());
		chargeThread = new ChargeThread(chargePoints, 1000);
		chargeThreadThread = new Thread(chargeThread);
		chargeThread.add(new ChargePoint(50));
		chargeThread.add(new ChargePoint(50));
		chargeThreadThread.start();
		chargeThread.add(new ChargePoint(50));
		chargeThread.add(new ChargePoint(50));
	}
	
	public void initJadeAgents()
	{
		carAgents = new ArrayList<>();
		
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
			masterSchedulerAgent = new MasterScheduler();
			String name = "Master Scheduler";
			AgentController ac = containerMasterScheduler.acceptNewAgent(name, masterSchedulerAgent);
			ac.start();
		}
		catch (StaleProxyException e)
		{
			e.printStackTrace();
		}
	}
	
	public void createCarAgent( long aMaxchargeCapacity, long aCurrentcharge )
	{
		long id = getCarAgents().size();
		createCarAgent(id, aMaxchargeCapacity, aCurrentcharge);
	}
	
	public void createCarAgent( long aID, long aMaxchargeCapacity, long aCurrentcharge )
	{
		try
		{
			Car car = new Car( aID, aMaxchargeCapacity, aCurrentcharge );
			String name = "Car " + aID;
			carAgents.add( car );
			AgentController ac = containerCars.acceptNewAgent( name, car );
			ac.start();
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
					
					for (Agent lAgent : carAgents)
					{
						msg.addReceiver(new AID(lAgent.getName(), AID.ISLOCALNAME));
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
					
					msg.addReceiver(new AID(masterSchedulerAgent.getName(), AID.ISLOCALNAME));
					
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
	
	public ArrayList<Car> getCarAgents()
	{
		return carAgents;
	}
	
	public MasterScheduler getMasterScheduler()
	{
		return masterSchedulerAgent;
	}

	public boolean isCharging()
	{
		return chargeThread.chargeStatus();
	}
	
	public boolean isCarCharging(Car aCar)
	{
		return chargeThread.getCarChargeStatus(aCar);
	}
	
}
