package model;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class CarBehaviourBasic extends CyclicBehaviour
{
	private Car car;
	
	public CarBehaviourBasic( Car aCar )
	{
		car = aCar;
	}

	@Override
	public void action() 
	{	
		ACLMessage msg = car.receive();
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
	}

}
