/*************************
 * MasterSchedulerBehaviourBasic.java
 * 
 * Behaviour attached to the master scheduler
 * 
 */

package model;

import java.util.ArrayList;

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
				ACLMessage reply = msg.createReply();
				reply.setPerformative( ACLMessage.INFORM );
				// Default assumption is a fail to charge
				reply.setContent( "wantCharge fail" );
				// Get all car agents
				ArrayList<Car> cars = masSch.getChargerSystem().getCarAgents();
				for(Car car : cars)
				{
					// Find specific car
					if(car.getAID().getLocalName().equals(msg.getSender().getLocalName()))
					{
						String[] split = msg.getContent().split("\\s+");
						for(int i = 0; i < split.length; ++i)
						{
							// Get requested time slice
							if(split[i].equals("--time"))
							{
								// If no valid values, assume 00:00 and 06:00
								String start = split.length >= i+1 ? split[i+1] : "00:00";
								String end = split.length >= i+2 ? split[i+2] : "06:00";
								String[] valStart = start.split(":");
								String[] valEnd = end.split(":");
								// Try to request a charge
								if(masSch.getChargerSystem().requestCharge(car, Integer.parseInt(valStart[0])*60 + Integer.parseInt(valStart[1]), Integer.parseInt(valEnd[0])*60 + Integer.parseInt(valEnd[1])))
								{
									// Charge is ok
									reply.setContent( "wantCharge ok" );
								}
							}
						}
					}
				}
				
				masSch.send( reply );
			}
			
		}
		
		masSch.getScheduleAlgorithm().run();
	}
	
	@Override
	public int onEnd()
	{
		return super.onEnd();
	}

}
