package model;

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
		ACLMessage msg = car.blockingReceive(1000);

		if ( msg != null )
		{
			// Handle message
			System.out.println( car.getLocalName() + ": Received message " + msg.getContent() + " from " + msg.getSender().getLocalName() );
			
			// reply
			ACLMessage reply = msg.createReply();
			reply.setPerformative( ACLMessage.INFORM );
			reply.setContent( "Pong" );
			car.send( reply );
		}
		
		if(System.currentTimeMillis() - lastDischarge > 1000)
		{
			lastDischarge = System.currentTimeMillis();
			car.discharge();
		}
	}

}
