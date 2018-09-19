package gui;

import java.awt.Point;
import java.awt.Rectangle;

import model.Car;

public class Cell
{
	private int x;
	private int y;
	private int w;
	private int h;
	private Car car;
	
	public Cell()
	{
		x = 0;
		y = 0;
		w = 50;
		h = 50;
		car = null;
	}
	
	public Cell(int aX, int aY, int aW, int aH, Car aCar)
	{
		x = aX;
		y = aY;
		w = aW;
		h = aH;
		car = aCar;
	}
	
	public boolean containsPoint(Point aPoint)
	{
		return new Rectangle(x, y, w, h).contains(aPoint);
	}
	
	public int getX()
	{
		return x;
	}
	
	public void setX(int aX)
	{
		x = aX;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setY(int aY)
	{
		y = aY;
	}
	
	public int getW()
	{
		return w;
	}
	
	public void setW(int aW)
	{
		w = aW;
	}
	
	public int getH()
	{
		return h;
	}
	
	public void setH(int aH)
	{
		h = aH;
	}
	
	public Car getCar()
	{
		return car;
	}
	
	public void setCar(Car aCar)
	{
		car = aCar;
	}
	
}
