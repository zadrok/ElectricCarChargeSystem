/*************************
 * ChargePoint.java
 * 
 * Cars can be connected to a charge point, which will increase their
 * available charge over time
 * 
 */

package model;

import boot.GlobalVariables;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
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
	private long flags;
	
	// Charge Point constructor
	public ChargePoint(double aChargeRate, MasterScheduler aMasSch, long aFlags)
	{
		chargeRate = aChargeRate;
		masSch = aMasSch;
		// Like cars, flags allow us to restrict the charger use to only
		// specific cars. Cars must have all flags listed for the charger
		// including 1, the default flag
		flags = 1;
		flags |= aFlags;
	}
	
	// Attach a car to the charging point
	public void AddCar(Car aToAdd, double aExpireTime)
	{
		connectedCar = aToAdd;
		connectedCar.setCarState(STATE.CHARGING);
		expireTime = (aExpireTime * 1000) + System.currentTimeMillis();
	}
	
	// Get currently connected car as ID
	public long GetConnectedCar()
	{
		if(connectedCar == null)
			return -1;
		return connectedCar.getID();
	}
	
	// Get current connected car
	public Car getCar()
	{
		return connectedCar;
	}
	
	// Get charge rate of charger
	public double getChargeRate()
	{
		return chargeRate;
	}
	
	// Get flags of charger
	public long getFlags()
	{
		return flags;
	}
	
	// Disconnect car from charger
	public Car disconnectCar()
	{
		Car lReturn = connectedCar;
		if ( connectedCar == null )
			return null;
		connectedCar.setCarState(STATE.IDLE);
		connectedCar = null;
		return lReturn;
	}
	
	// Get time remaining for current attached car
	public long GetTimeRemaining()
	{
		return (long) ((expireTime - System.currentTimeMillis())/1000);
	}
	
	// Perform a charge on the car
	public void performCharge()
	{
		// Only continue if we have a car connected
		if(connectedCar != null)
		{
			// Determine amount to charge based on charge rate and the charge interval.
			// Divided by 60 to convert time from hours to minutes and minutes to seconds
			double lTotalCharge = connectedCar.getCurrentCharge() + (chargeRate/GlobalVariables.timeScale)/GlobalVariables.chargeInterval;
			connectedCar.setCurrentCharge(lTotalCharge > connectedCar.getMaxChargeCapacity() ? connectedCar.getMaxChargeCapacity() : lTotalCharge);
			// Determine if car is fully charged
			if(connectedCar.getCurrentCharge() >= connectedCar.getMaxChargeCapacity())
			{	
				// Disconnect car if fully charged
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent("Your battery is now full. Disconnecting.");
				msg.addReceiver(new AID(connectedCar.getLocalName(), AID.ISLOCALNAME));
				masSch.send(msg);
				
				disconnectCar();
			}
			// Determine if car charge time has expired
			if(expireTime <= System.currentTimeMillis())
			{
				// Disconnect car if time has expired
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent("Your charge period has expired. Disconnecting.");
				msg.addReceiver(new AID(connectedCar.getLocalName(), AID.ISLOCALNAME));
				masSch.send(msg);
				
				disconnectCar();
			}
		}
	}
}
