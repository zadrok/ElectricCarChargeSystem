package model;

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
	}
	
	public Car DisconnectCar()
	{
		Car lReturn = connectedCar;
		connectedCar = null;
		return lReturn;
	}
	
	public void performCharge()
	{
		if(connectedCar != null)
		{
			long lTotalCharge = connectedCar.getCurrentCharge() + chargeRate;
			connectedCar.setCurrentCharge(lTotalCharge > connectedCar.getMaxChargeCapacity() ? connectedCar.getMaxChargeCapacity() : lTotalCharge);
			System.out.println("Charged car " + connectedCar.getID() + " to charge " + connectedCar.getCurrentCharge());
		}
		else
		{
			System.out.println("No car attached to charge point");
		}
	}
}
