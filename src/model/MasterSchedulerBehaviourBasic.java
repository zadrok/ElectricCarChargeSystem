package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class MasterSchedulerBehaviourBasic extends CyclicBehaviour
{
	private List<ChargePoint> chargePoints;
	private ChargeThread chargeThread;
	MasterScheduler masSch;
	
	public MasterSchedulerBehaviourBasic( MasterScheduler aMasSch )
	{
		chargePoints = Collections.synchronizedList(new ArrayList<ChargePoint>());
		chargePoints.add(new ChargePoint(50));
		chargeThread = new ChargeThread(chargePoints, 1000);
		chargePoints.add(new ChargePoint(50));
		chargeThread.run();
		chargePoints.add(new ChargePoint(50));
		chargePoints.add(new ChargePoint(50));
		chargePoints.add(new ChargePoint(50));
		chargePoints.add(new ChargePoint(50));
		chargePoints.add(new ChargePoint(50));
		chargePoints.add(new ChargePoint(50));
		chargePoints.add(new ChargePoint(50));
		masSch = aMasSch;
	}

	@Override
	public void action() 
	{
		ACLMessage msg = masSch.receive();
		if ( msg != null )
		{
			// Handle message
			System.out.println( masSch.getLocalName() + ": Received message " + msg.getContent() + " from " + msg.getSender().getLocalName() );
			
			// reply
			ACLMessage reply = msg.createReply();
			reply.setPerformative( ACLMessage.INFORM );
			reply.setContent( "Ping" );
			masSch.send( reply );
		}
	}
	
	@Override
	public int onEnd()
	{
		chargeThread.stop();
		chargeThread = null;
		return super.onEnd();
	}
	
	// ChargeThread to handle charging of a car
	public class ChargeThread implements Runnable
	{
		List<ChargePoint> chargePoints;
		int chargePeriod;
		boolean stop = false;
		
		public ChargeThread(List<ChargePoint> aChargers, int aChargePeriod)
		{
			chargePoints = aChargers;
			chargePeriod = aChargePeriod;
		}
		
		public void stop()
		{
			// Stop running
			stop = true;
		}
		
		public void run()
		{
			while(!stop)
			{
				// Sleep for given period of time
				try {
					Thread.sleep(chargePeriod);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				// Increment car charge
				for(int i = 0; i < chargePoints.size(); ++i)
				{
					System.out.println("Charging at point " + i);
					chargePoints.get(i).performCharge();
				}
			}
		}
	}

}
