/*************************
 * CarBehaviourBasic.java
 * 
 * Contains the basic agent behaviour of
 * the given car agent.
 * 
 */

package model;

import boot.GlobalVariables;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class CarBehaviourBasic extends CyclicBehaviour
{
	private Car car;
	private long lastDischarge;
	private int numTimesResentWantCharge;
	private int maxTimesToResendWantCharge;
	
	// Constructor for behaviour
	public CarBehaviourBasic( Car aCar )
	{
		car = aCar;
		// We use a current timestamp for last discharge
		lastDischarge = System.currentTimeMillis();
		
		numTimesResentWantCharge = 0;
		maxTimesToResendWantCharge = 5;
	}

	@Override
	public void action() 
	{	
		// blockingReceive is used to prevent a busy-wait
		ACLMessage msg = car.blockingReceive(10);

		// Process following if we have a message
		if ( msg != null )
		{
			// Handle message
//			System.out.println( car.getLocalName() + ": Received message " + msg.getContent() + " from " + msg.getSender().getLocalName() );
			
			// Reply to message
			if ( msg.getContent().startsWith( "wantCharge" ) )
			{
				if ( numTimesResentWantCharge >= maxTimesToResendWantCharge )
				{
					try { Thread.sleep( 60 * 1000 ); } catch (InterruptedException e) { }
					numTimesResentWantCharge = 0;
				}
				
				wantCharge(msg);
			}
		}
		
		// If the car wants to charge or has the state of CHARGE
		if ( car.isWantCharge() ) // || car.getCarState() == STATE.CHARGE
		{
			// Create a charge request
			ACLMessage newMSG = new ACLMessage( ACLMessage.INFORM );
			// Time is in the format Long and Long duration from start time
			// For this purpose of simulation, time is 60x quicker (minute -> second, hour -> minute
			String[] time = car.getChargeTimes();
			newMSG.setContent( "wantCharge --time " + time[0] + " " + time[1] );
			newMSG.addReceiver( new AID( "Master Scheduler", AID.ISLOCALNAME ) );
			car.send( newMSG );
		}
    
		// Poll Delay determines if the car will charge this tick. A higher poll delay results in more
		// frequent charges, however the charge rate will remain the same
		long pollDelay = (1000/GlobalVariables.chargeInterval) - (System.currentTimeMillis() - lastDischarge);
		if(pollDelay < 0)
		{
			lastDischarge = System.currentTimeMillis() + pollDelay;
			car.discharge();
		}
	}
	
	private void wantCharge(ACLMessage msg)
	{
		if ( msg.getContent().contains( "ok" ) )
		{
			car.setWantCharge(false);
			car.setWaitingForCharge(true);
			System.out.println( car.getLocalName() + " is waiting for charge" );
			numTimesResentWantCharge = 0;
		}
		else if ( msg.getContent().contains( "alreadyInQueue" ) )
		{
			car.setWantCharge(false);
			car.setWaitingForCharge(true);
			numTimesResentWantCharge = 0;
		}
		else if ( msg.getContent().contains( "resend" ) )
		{
			car.setWantCharge(true);
			car.setWaitingForCharge(false);
			numTimesResentWantCharge += 1;
		}
		else if ( msg.getContent().contains( "couldNotFindTime" ) )
		{
			car.setWantCharge(true);
			car.setWaitingForCharge(false);
			numTimesResentWantCharge += 1;
			// TODO - find different time or try again with same time later
		}
		else
		{
			System.out.println( car.getLocalName() + " didn't recognise reply" );
			car.setWantCharge(true);
			car.setWaitingForCharge(false);
			numTimesResentWantCharge += 1;
		}
	}

}
