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
	
	public CarBehaviourBasic( Car aCar )
	{
		car = aCar;
		lastDischarge = System.currentTimeMillis();
	}

	@Override
	public void action() 
	{	
		ACLMessage msg = car.blockingReceive(100);

		if ( msg != null )
		{
			// Handle message
			System.out.println( car.getLocalName() + ": Received message " + msg.getContent() + " from " + msg.getSender().getLocalName() );
			
			// reply
			if ( msg.getContent().startsWith( "wantCharge" ) )
			{
				if ( msg.getContent().contains( "ok" ) )
				{
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
			
//			ACLMessage reply = msg.createReply();
//			reply.setPerformative( ACLMessage.INFORM );
//			reply.setContent( "Pong" );
//			car.send( reply );
		}
		
		if ( car.isWantCharge() || car.getCarState() == STATE.CHARGE )
		{
			ACLMessage newMSG = new ACLMessage( ACLMessage.INFORM );
			newMSG.setContent( "wantCharge --time 00:00 01:00" );
			newMSG.addReceiver( new AID( "Master Scheduler", AID.ISLOCALNAME ) );
			car.send( newMSG );
		}
    
		long pollDelay = (1000/GlobalVariables.chargeInterval) - (System.currentTimeMillis() - lastDischarge);
		if(pollDelay < 0)
		{
			lastDischarge = System.currentTimeMillis() + pollDelay;
			car.discharge();
		}
	}

}
