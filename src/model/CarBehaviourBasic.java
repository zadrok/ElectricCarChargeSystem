package model;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

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
		
		if ( car.isWantCharge() )
		{
			ACLMessage newMSG = new ACLMessage( ACLMessage.INFORM );
			newMSG.setContent( "wantCharge --time 00:00 06:00" );
			newMSG.addReceiver( new AID( "Master Scheduler", AID.ISLOCALNAME ) );
			car.send( newMSG );
		}
    
		if(System.currentTimeMillis() - lastDischarge > 500)
		{
			lastDischarge = System.currentTimeMillis();
			car.discharge();
		}
	}

}
