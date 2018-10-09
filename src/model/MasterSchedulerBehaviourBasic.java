package model;

import java.util.ArrayList;
import java.util.List;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import model.ChargerSystem.Tuple;

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
		ACLMessage msg = masSch.blockingReceive(200);
		if ( msg != null )
		{
			// Handle message
			System.out.println( masSch.getLocalName() + ": Received message " + msg.getContent() + " from " + msg.getSender().getLocalName() );
			
			// reply
			
			if ( msg.getContent().startsWith( "wantCharge" ) )
			{
				ACLMessage reply = msg.createReply();
				reply.setPerformative( ACLMessage.INFORM );
				reply.setContent( "wantCharge fail" );
				ArrayList<Car> cars = masSch.getChargerSystem().getCarAgents();
				for(Car car : cars)
				{
					if(car.getAID().getLocalName().equals(msg.getSender().getLocalName()))
					{
						String[] split = msg.getContent().split("\\s+");
						for(int i = 0; i < split.length; ++i)
						{
							if(split[i].equals("--time"))
							{
								String start = split.length >= i+1 ? split[i+1] : "00:00";
								String end = split.length >= i+2 ? split[i+2] : "06:00";
								String[] valStart = start.split(":");
								String[] valEnd = end.split(":");
								if(masSch.getChargerSystem().requestCharge(car, Integer.parseInt(valStart[0])*60 + Integer.parseInt(valStart[1]), Integer.parseInt(valEnd[0])*60 + Integer.parseInt(valEnd[1])))
								{
									reply.setContent( "wantCharge ok" );
								}
							}
						}
					}
				}
				
				masSch.send( reply );
			}
			
		}
		
		List<Tuple<Car, Long, Long>> queue = masSch.getChargerSystem().getChargeQueue();
		for(int i = queue.size()-1; i >= 0; --i)
		{
			if(masSch.getChargerSystem().getTimeSeconds() >= queue.get(i).timeStart())
			{
				if(masSch.getChargerSystem().tryChargeCar(queue.get(i).car(), queue.get(i).timeEnd()))
					queue.remove(i);
			}
		}
	}
	
	@Override
	public int onEnd()
	{
		return super.onEnd();
	}

}
