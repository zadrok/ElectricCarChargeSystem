package model;

import boot.GlobalVariables;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import jade.wrapper.gateway.JadeGateway;
import model.Car.STATE;

// The rate of a charge will typically vary from 25kW to 135kW at public
// fast-charger or super-charger stations. Typical AC outlets charge at
// approx 2.5kW to 7kW

public class ChargePoint
{
	private Car connectedCar = null;
	private double chargeRate;
	private double expireTime;
	private MasterScheduler masSch;
	
	public ChargePoint(double aChargeRate, MasterScheduler aMasSch)
	{
		chargeRate = aChargeRate;
		masSch = aMasSch;
	}
	
	public void AddCar(Car aToAdd, double aExpireTime)
	{
		connectedCar = aToAdd;
		connectedCar.setCarState(STATE.CHARGING);
		expireTime = (aExpireTime * 1000) + System.currentTimeMillis();
	}
	
	public long GetConnectedCar()
	{
		if(connectedCar == null)
			return -1;
		return connectedCar.getID();
	}
	
	public Car getCar()
	{
		return connectedCar;
	}
	
	public double getChargeRate()
	{
		return chargeRate;
	}
	
	public Car DisconnectCar()
	{
		Car lReturn = connectedCar;
		connectedCar.setCarState(STATE.IDLE);
		connectedCar = null;
		return lReturn;
	}
	
	public long GetTimeRemaining()
	{
		return (long) ((expireTime - System.currentTimeMillis())/1000);
	}
	
	public void performCharge()
	{
		if(connectedCar != null)
		{
			double lTotalCharge = connectedCar.getCurrentCharge() + (chargeRate/60.0)/GlobalVariables.chargeInterval;
			connectedCar.setCurrentCharge(lTotalCharge > connectedCar.getMaxChargeCapacity() ? connectedCar.getMaxChargeCapacity() : lTotalCharge);
			if(connectedCar.getCurrentCharge() >= connectedCar.getMaxChargeCapacity())
			{	
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent("Your battery is now full. Disconnecting.");
				msg.addReceiver(new AID(connectedCar.getLocalName(), AID.ISLOCALNAME));
				masSch.send(msg);
				
				DisconnectCar();
			}
			if(expireTime <= System.currentTimeMillis())
			{
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent("Your charge period has expired. Disconnecting.");
				msg.addReceiver(new AID(connectedCar.getLocalName(), AID.ISLOCALNAME));
				masSch.send(msg);
				
				DisconnectCar();
			}
//			System.out.println("Charged car " + connectedCar.getID() + " to charge " + connectedCar.getCurrentCharge());
		}
		else
		{
//			System.out.println("No car attached to charge point");
		}
	}
}
