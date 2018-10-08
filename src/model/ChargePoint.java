package model;

import model.Car.STATE;

public class ChargePoint
{
	private Car connectedCar = null;
	private long chargeRate;
	
	public ChargePoint(long aChargeRate)
	{
		chargeRate = aChargeRate;
	}
	
	public void AddCar(Car aToAdd)
	{
		connectedCar = aToAdd;
		connectedCar.setCarState(STATE.CHARGING);
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
	
	public long getChargeRate()
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
	
	public void performCharge()
	{
		if(connectedCar != null)
		{
			long lTotalCharge = connectedCar.getCurrentCharge() + chargeRate;
			connectedCar.setCurrentCharge(lTotalCharge > connectedCar.getMaxChargeCapacity() ? connectedCar.getMaxChargeCapacity() : lTotalCharge);
			if(connectedCar.getCurrentCharge() == connectedCar.getMaxChargeCapacity())
			{
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
