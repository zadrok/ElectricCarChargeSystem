package gui;

import java.awt.Point;
import java.awt.Rectangle;

import model.Car;

public class Cell
{
	private Rectangle rect;
	private Car car;
	private int strokeWidth;
	private double strokeScale;
	
	public Cell()
	{
		rect = new Rectangle(0, 0, 50, 50);
		car = null;
		strokeWidth = 10;
		strokeScale = 1.0;
	}
	
	public Cell(int aX, int aY, int aW, int aH, Car aCar, int aStrokeWidth, double aStrokeScale)
	{
		this();
		rect.x = aX;
		rect.y = aY;
		rect.width = aW;
		rect.height = aH;
		car = aCar;
		strokeWidth = aStrokeWidth;
		strokeScale = aStrokeScale;
	}
	
	public boolean containsPoint(Point aPoint)
	{
		return rect.contains(aPoint);
	}
	
	public int getX()
	{
		return rect.x;
	}
	
	public void setX(int aX)
	{
		rect.x = aX;
	}
	
	public int getY()
	{
		return rect.y;
	}
	
	public void setY(int aY)
	{
		rect.y = aY;
	}
	
	public int getW()
	{
		return rect.width;
	}
	
	public void setW(int aW)
	{
		rect.width = aW;
	}
	
	public int getH()
	{
		return rect.height;
	}
	
	public void setH(int aH)
	{
		rect.height = aH;
	}
	
	public Rectangle getRect()
	{
		return rect;
	}
	
	public void setRect(Rectangle aRect)
	{
		rect = aRect;
	}
	
	public Car getCar()
	{
		return car;
	}
	
	public void setCar(Car aCar)
	{
		car = aCar;
	}
	
	public int getStrokeWidth()
	{
		return strokeWidth;
	}
	
	public void setStrokeWidth(int aStrokeWidth)
	{
		strokeWidth = aStrokeWidth;
	}
	
	public double getStrokeScale()
	{
		return strokeScale;
	}
	
	public void setStrokeScale(double aStrokeScale)
	{
		strokeScale = aStrokeScale;
	}
	
}
