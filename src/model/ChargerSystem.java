/*************************
 * ChargerSystem.java
 * 
 * ChargerSystem handles all the charger functionality, including
 * chargers and the charge thread.
 * 
 */

package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import boot.GlobalVariables;
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
	
	private Random random;
	private ArrayList<QueueItem> chargeQueue;
	private ArrayList<QueueItem> chargeQueueOLD;
	
	private long clockStartTime;
	private long clockRunTime;
	
	// Constructor for charger system
	public ChargerSystem()
	{
		random = new Random();
		clockStartTime = System.currentTimeMillis();
		clockRunTime = 0;
		
		// Create charge points, queue and thread
		chargePoints = Collections.synchronizedList(new ArrayList<ChargePoint>());
		chargeQueue = new ArrayList<>();
		chargeQueueOLD = new ArrayList<>();
		chargeThread = new ChargeThread(chargePoints, GlobalVariables.chargeInterval);
		chargeThreadThread = new Thread(chargeThread);
		chargeThreadThread.start();
		

		initJadeAgents();
		initChargerPoints();
	}
	
	// stop all loopers and threads
	public void stopLoopers()
	{
		chargeThread.stop();
	}
	
	// kill all agents
	public void killAll()
	{
		masterSchedulerAgent.doDelete();
		
		for ( Car car : carAgents )
		{
			car.doDelete();
		}
	}
	
	// Agent initializer to create containers
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
		
		// Create Small car agents
		for ( int i = 0; i < GlobalVariables.numCarsSmall; i++ )
		{
			int max = GlobalVariables.carBatterySizeSmall;
			int base = 0;
			int current = random.nextInt( max - base ) + base;
			createCarAgent( max, current );
		}
		
		// Create Medium car agents
		for ( int i = 0; i < GlobalVariables.numCarsMedium; i++ )
		{
			int max = GlobalVariables.carBatterySizeMedium;
			int base = 0;
			int current = random.nextInt( max - base ) + base;
			createCarAgent( max, current );
		}
		
		// Create Large car agents
		for ( int i = 0; i < GlobalVariables.numCarsLarge; i++ )
		{
			int max = GlobalVariables.carBatterySizeLarge;
			int base = 0;
			int current = random.nextInt( max - base ) + base;
			createCarAgent( max, current );
		}

		// Creating car with extra flags
//		createCarAgent( getCarAgents().size(), GlobalVariables.carBatterySizeLarge, 10, (2 | 4) );
	}
	
	// Creates charger points based on defaults
	public void initChargerPoints()
	{
		// Creating charger points
		createChargePoint( GlobalVariables.chargePointChargeRateSizeLarge, 2 );
		
		// Create Small charge points
		for(int i = 0; i < GlobalVariables.numChargePointsSmall; ++i)
		{
			createChargePoint( GlobalVariables.chargePointChargeRateSizeSmall );
		}
		
		// Create Medium charge points
		for(int i = 0; i < GlobalVariables.numChargePointsMedium; ++i)
		{
			createChargePoint( GlobalVariables.chargePointChargeRateSizeMedium );
		}
		
		// Create Large charge points
		for(int i = 0; i < GlobalVariables.numChargePointsSmall; ++i)
		{
			createChargePoint( GlobalVariables.chargePointChargeRateSizeLarge );
		}
	}
	
	// Creates the master scheudler
	public void createMasterScheduler()
	{
		try
		{
			masterSchedulerAgent = new MasterScheduler(this);
			String name = "Master Scheduler";
			AgentController ac = containerMasterScheduler.acceptNewAgent(name, masterSchedulerAgent);
			ac.start();
		}
		catch (StaleProxyException e)
		{
			e.printStackTrace();
		}
	}
	
	// Create a charge point using chargeRate
	public void createChargePoint(int aChargeRate)
	{
		chargeThread.add( new ChargePoint( aChargeRate, masterSchedulerAgent, 0 ) );
	}
	
	// Create a charge point using chargeRate and flags
	public void createChargePoint(int aChargeRate, long flags)
	{
		chargeThread.add( new ChargePoint( aChargeRate, masterSchedulerAgent, flags ) );
	}
	
	// Create car agent using max capacity and current charge
	public void createCarAgent( long aMaxchargeCapacity, long aCurrentcharge )
	{
		long id = getCarAgents().size();
		createCarAgent(id, aMaxchargeCapacity, aCurrentcharge);
	}
	
	// Create car agent using id, max capacity and current charge
	private void createCarAgent( long aID, long aMaxchargeCapacity, long aCurrentcharge )
	{
		try
		{
			Car car = new Car( aID, aMaxchargeCapacity, aCurrentcharge, 0 );
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
	
	// Create car agent using id, max capacity, current charge and flags
	private void createCarAgent( long aID, long aMaxchargeCapacity, long aCurrentcharge, long aFlags )
	{
		try
		{
			Car car = new Car( aID, aMaxchargeCapacity, aCurrentcharge, aFlags );
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
	
	// Send a message to all car agents
	public void messageAllCars(int aMeggageType, String aMessage)
	{
		System.out.println("Sending message to all Cars: " + aMessage);
		
		try
		{
			@SuppressWarnings("serial")
			OneShotBehaviour behaviour = ( new OneShotBehaviour() 
			{
				public void action() 
				{
					ACLMessage msg = new ACLMessage(aMeggageType);
					msg.setContent(aMessage);
					
					for (Agent lAgent : carAgents)
					{
						msg.addReceiver(new AID(lAgent.getLocalName(), AID.ISLOCALNAME));
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
	
	// Send a message to the master scheduler
	public void messageMasterScheduler(int aMeggageType, String aMessage)
	{
		System.out.println("Sending message to Master Scheduler: " + aMessage);
		
		try
		{
			@SuppressWarnings("serial")
			OneShotBehaviour behaviour = ( new OneShotBehaviour() 
			{
				public void action() 
				{
					ACLMessage msg = new ACLMessage(aMeggageType);
					msg.setContent(aMessage);
					
					msg.addReceiver(new AID(masterSchedulerAgent.getLocalName(), AID.ISLOCALNAME));
					
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
	
	// Check if a car is currently charging
	public boolean carOnCharge(Car aCar)
	{
		for ( ChargePoint lCPoint : getChargePoints() )
		{
			if ( lCPoint.GetConnectedCar() == aCar.getID() )
				return true;
		}
		return false;
	}
	
	// Get all car agents
	public ArrayList<Car> getCarAgents()
	{
		return carAgents;
	}
	
	// Get master scheduler
	public MasterScheduler getMasterScheduler()
	{
		return masterSchedulerAgent;
	}

	// Check if the charge thread is running
	public boolean isCharging()
	{
		return chargeThread.chargeStatus();
	}
	
	// Check if a car is charging
	public boolean isCarCharging(Car aCar)
	{
		return chargeThread.getCarChargeStatus(aCar);
	}
	
	// Request a charge for a car without timestamps
	public boolean requestCharge(Car aCar)
	{
		return requestCharge(aCar, 0, 60);
	}
	
	// Request a charge using timestamp
	public boolean requestCharge(Car aCar, long aTimeStart, long aTimePeriod)
	{
		// Don't allow a car to be added to the queue twice
		for( QueueItem lItem : chargeQueue )
		{
			if( lItem.getCar() == aCar )
				return false;
		}
		
		long tryStartTime = aTimeStart + getTimeSeconds();
		System.out.println("Charging at " + tryStartTime + " for " + aTimePeriod);
		chargeQueue.add(new QueueItem(aCar, tryStartTime, aTimePeriod));
		return true;
	}
	
	// Try to attach car to charge point
	public boolean tryChargeCar(Car aCar, long chargeTime)
	{
		for(ChargePoint a : chargePoints)
		{
			if(a.GetConnectedCar() == -1 && (a.getFlags() & aCar.getFlags()) == a.getFlags())
			{
				a.AddCar(aCar, chargeTime);
				return true;
			}
		}
		return false;
	}
	
	// Get all charge points
	public List<ChargePoint> getChargePoints()
	{
		return chargePoints;
	}
	
	// Get charge queue
	public ArrayList<QueueItem> getChargeQueue()
	{
		return chargeQueue;
	}
	
	// Get OLD charge queue
	public ArrayList<QueueItem> getChargeQueueOLD()
	{
		return chargeQueueOLD;
	}
	
	// Get system time in seconds
	public long getTimeSeconds()
	{
		return System.currentTimeMillis()/1000;
	}
	
	public long getClockStartTime()
	{
		return clockStartTime;
	}
	
	public long getClockRunTime()
	{
		return clockRunTime;
	}
	
	public void updateClockRunTime(long aTimeToAdd)
	{
		clockRunTime += aTimeToAdd;
	}
	
}
