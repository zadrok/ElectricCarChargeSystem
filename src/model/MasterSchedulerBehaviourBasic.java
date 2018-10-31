/*************************
 * MasterSchedulerBehaviourBasic.java
 * 
 * Behaviour attached to the master scheduler
 * 
 */

package model;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class MasterSchedulerBehaviourBasic extends CyclicBehaviour
{
	MasterScheduler masSch;
	
	// Constructor for behaviour
	public MasterSchedulerBehaviourBasic( MasterScheduler aMasSch )
	{
		masSch = aMasSch;
	}

	@Override
	public void action() 
	{
		// Blocking is used to prevent a busy-wait
		ACLMessage msg = masSch.blockingReceive(10);
		
		// Continue if we have a message
		if ( msg != null )
		{
			// Handle message
			System.out.println( masSch.getLocalName() + ": Received message " + msg.getContent() + " from " + msg.getSender().getLocalName() );
			
			// reply
			// wantCharge implies the car wants to schedule a charging time
			if ( msg.getContent().startsWith( "wantCharge" ) )
			{
				wantCharge( msg );
			}
			
		}
		
		// tell charge points to switch cars on them if it is time
		masSch.getChargerSystem().checkChargePoints();
	}
	
	private void wantCharge(ACLMessage msg)
	{
		// prepare reply
		ACLMessage reply = msg.createReply();
		reply.setPerformative( ACLMessage.INFORM );
		// Default assumption is a fail to charge
		reply.setContent( "wantCharge fail" );
		// Get car agent
		Car car = masSch.getChargerSystem().getCarAgent( msg.getSender().getLocalName() );
		// split message into parts
		String[] split = msg.getContent().split( "\\s+" );
		
		for ( int i = 0; i < split.length; i++ )
		{
			// Get requested time slice
			if ( split[i].equals("--time") )
			{
				// get next two values, if fail ask for resend
				long startTime = 0;
				long chargeTime = 0;
				try
				{
					// convert strings to longs
					String start = split[i+1];
					String end = split[i+2];
					startTime = Long.parseLong( start );
					chargeTime = Long.parseLong( end );
				}
				catch ( Exception e )
				{
					// ask for resend
					reply.setContent( "wantCharge resend" );
					break;
				}
				
				// Try to request a charge
				if ( masSch.getScheduleAlgorithm().requestCharge(car, startTime, chargeTime) )
				{
					// Charge is ok
					reply.setContent( "wantCharge ok" );
				}
				else
				{
					// if requestCharge was false send one of two replies
					// one: if car is already in the queue then tell it that
					if ( masSch.getChargerSystem().inQueue(car) )
					{
						reply.setContent( "wantCharge alreadyInQueue" );
					}
					// option 2, if not in queue already
					// there was a problem in finding a time for the car so tell it to ask again with a different time if possible
					else
					{
						reply.setContent( "wantCharge couldNotFindTime" );
					}
				}
			}
		}
		
		// send reply
		masSch.send( reply );
	}
	
	@Override
	public int onEnd()
	{
		return super.onEnd();
	}

}
