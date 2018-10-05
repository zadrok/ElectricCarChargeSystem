package model;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class MasterSchedulerBehaviourBasic extends CyclicBehaviour
{
	MasterScheduler masSch;
	
	public MasterSchedulerBehaviourBasic( MasterScheduler aMasSch )
	{
		masSch = aMasSch;
	}

	@Override
	public void action() 
	{
		ACLMessage msg = masSch.blockingReceive();
		if ( msg != null )
		{
			// Handle message
			System.out.println( masSch.getLocalName() + ": Received message " + msg.getContent() + " from " + msg.getSender().getLocalName() );
			
			// reply
			
			if ( msg.getContent().startsWith( "wantCharge" ) )
			{
				//boolean response = masSch.getChargerSystem().requestCharge(msg.getSender().getClass());
				ACLMessage reply = msg.createReply();
				reply.setPerformative( ACLMessage.INFORM );
				reply.setContent( "wantCharge ok" );
				masSch.send( reply );
			}
			
		}
		else
		{
			Thread.yield();
		}
	}
	
	@Override
	public int onEnd()
	{
		return super.onEnd();
	}

}
