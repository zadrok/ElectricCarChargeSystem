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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	private List<QueueItem> chargeQueue;
	private List<QueueItem> chargeQueueOLD;
	
	private long clockStartTime;
	private long clockRunTime;
	
	private final Lock queueLock; 
	
	// Constructor for charger system
	public ChargerSystem()
	{
		queueLock = new ReentrantLock(); 
		random = new Random();
		clockStartTime = System.currentTimeMillis();
		clockRunTime = 0;
		
		// Create charge points, queue and thread
		chargePoints = Collections.synchronizedList(new ArrayList<ChargePoint>());
		chargeQueue = Collections.synchronizedList(new ArrayList<QueueItem>());
		chargeQueueOLD = Collections.synchronizedList(new ArrayList<QueueItem>());
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
	}
	
	// Creates charger points based on defaults
	public void initChargerPoints()
	{
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
	
	public int getRandomInt(int aUpperBounds)
	{
		if ( aUpperBounds == 0 )
			return 0;
		
		return random.nextInt( Math.abs( aUpperBounds ) );
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
	
	// Get car agent with matching name
	public Car getCarAgent(String aName)
	{
		for ( Car car : getCarAgents() )
		{
			if ( car.getLocalName().contentEquals( aName ) )
				return car;
		}
		
		return null;
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
	
	// charge points will switch out cars if there scheduled time is up
	public void checkChargePoints()
	{
		queueLock();
		try
		{
//			System.out.println( "getChargeQueue().size() " + getChargeQueue().size() );
//			System.out.println( "getChargeQueueOLD().size() " + getChargeQueueOLD().size() );
			
			// go through each charge point
			for ( ChargePoint point : getChargePoints() )
			{
				// find the current queue item that is active
				QueueItem item = getCurrentActiveQueueItem(point);
				if ( item == null )
				{
//					System.out.println( "item == null" );
					continue;
				}
				if ( point == null )
				{
//					System.out.println( "point == null" );
					continue;
				}
//				if ( point.getCar() == null )
//				{
//					System.out.println( "point.getCar() == null" );
//					continue;
//				}
				if ( item.getCar() == null )
				{
//					System.out.println( "item.getCar() == null" );
					continue;
				}
				
//				System.out.println( "not null" );
					
//				System.out.println( "QueueItem car " + item.getCar().toString() );
				// if null then there is no queued car
//				if ( item == null )
//				{
//					// remove item
//					getChargeQueueOLD().add( item );
//					getChargeQueue().remove( item );
//				}
				// check to see if the correct car is charging
//				System.out.println( " point car " + point.getCar().getID() + " - item car " + item.getCar().getID() );
				if ( point.getCar() == null )
				{
					point.AddCar(item.getCar(), item.timeEnd());
				}
				else if ( point.getCar() != item.getCar() )
				{
//					System.out.println( "not same car" );
//					System.out.println( "disconnectCar " );
					// disconnect current car
					point.disconnectCar();
					// put new car on
					point.AddCar(item.getCar(), item.timeEnd());
				}
			}
			
			// go through all queue items 
			// if an item time is over move it to old queue
			ArrayList<QueueItem> toMove = new ArrayList<>();
			for ( QueueItem item : getChargeQueue() )
			{
				if ( item.timeStart() + item.timeEnd() <= GlobalVariables.runTime )
				{
//					System.out.println( "moving to old" );
					// remove item
					toMove.add( item );
				}
			}
			
			for ( QueueItem item : toMove )
			{
				getChargeQueueOLD().add( item );
				getChargeQueue().remove( item );
			}
			
		}
		finally
		{
			queueUnlock();
		}
	}
	
	public void queueLock()
	{
		queueLock.lock();
	}
	
	public void queueUnlock()
	{
		queueLock.unlock();
		
	}
	
	// return current active queue item for a charge poing
	public QueueItem getCurrentActiveQueueItem(ChargePoint aPoint)
	{
		// go through all charge points
		for ( QueueItem item : getChargeQueue() )
		{
			// find charge point that is currently being looked for
			// and that is active
			if ( item.getChargePoint() == aPoint )
			{
//				System.out.println( "item.getChargePoint() == aPoint" );
				if ( item.isActive() )
				{
//					System.out.println( "item.isActive()" );
					return item;
				}
			}
		}
		
		return null;
	}
	
	// check if car is in queue
	public boolean inQueue(Car aCar)
	{
		for( QueueItem lItem : getChargeQueue() )
		{
			if ( lItem.getCar() == aCar )
				return true;
		}
		
		return false;
	}
	
	// Get all charge points
	public List<ChargePoint> getChargePoints()
	{
		return chargePoints;
	}
	
	// Get charge queue
	public List<QueueItem> getChargeQueue()
	{
		return chargeQueue;
	}
	
	// Get OLD charge queue
	public List<QueueItem> getChargeQueueOLD()
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
