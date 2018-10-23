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
import model.Car.STATE;

@SuppressWarnings("serial")
public class CarBehaviourBasic extends CyclicBehaviour
{
	private Car car;
	private long lastDischarge;
	
	// Constructor for behaviour
	public CarBehaviourBasic( Car aCar )
	{
		car = aCar;
		// We use a current timestamp for last discharge
		lastDischarge = System.currentTimeMillis();
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
			System.out.println( car.getLocalName() + ": Received message " + msg.getContent() + " from " + msg.getSender().getLocalName() );
			
			// Reply to message
			if ( msg.getContent().startsWith( "wantCharge" ) )
			{
				if ( msg.getContent().contains( "ok" ) )
				{
					// If the car wants to charge, start waiting for charge
					if ( car.isWantCharge() )
					{
						car.setWantCharge(false);
						car.setWaitingForCharge(true);
						System.out.println( car.getLocalName() + " is waiting for charge" );
					}
				}
				else
				{
					
				}
			}
			else if(msg.getContent().startsWith("mayCharge"))
			{
				
			}
		}
		
		// If the car wants to charge or has the state of CHARGE
		if ( car.isWantCharge() || car.getCarState() == STATE.CHARGE )
		{
			// Create a charge request
			ACLMessage newMSG = new ACLMessage( ACLMessage.INFORM );
			// Time is in the format HH:MM from NOW and HH:MM duration from start time
			// For this purpose of simulation, time is 60x quicker (minute -> second, hour -> minute
			newMSG.setContent( "wantCharge --time 00:00 01:00" );
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

}
