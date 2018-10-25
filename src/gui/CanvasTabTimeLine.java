package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import model.Car;
import model.ChargerSystem.Tuple;

@SuppressWarnings("serial")
public class CanvasTabTimeLine extends CanvasTab
{

	public CanvasTabTimeLine(Canvas aCanvas)
	{
		super(aCanvas);
	}
	
	public void paintComponent(Graphics g)
    {
		Graphics2D g2 = (Graphics2D) g;
		double deltaTime = getLooper().getDeltaTime();
		// clear screen white
		fillRect( g2, new Rectangle(0,0,getWidth(),getHeight()), ColorIndex.canvasFill );
		
		drawGraphBack(g2,deltaTime);
		drawBlocks(g2,deltaTime);
		drawGraphFront(g2,deltaTime);
    }
	
	private void drawGraphBack(Graphics2D g2, double aDeltaTime)
	{
		
	}
	
	private void drawGraphFront(Graphics2D g2, double aDeltaTime)
	{
		int xMargin = 5;
		int yMargin = 5;
		int widthIncrement = 30;
		int heightIncrement = 30;
		
		// draw horizontal lines
		Rectangle rect = new Rectangle(xMargin, yMargin, getWidth() - xMargin - widthIncrement, 0);
		for ( int i = 0; i < getHeight() - heightIncrement - yMargin; i += heightIncrement )
		{
			drawLine( g2, rect, Color.BLACK );
			rect.y += heightIncrement;
		}
		
		// draw vertical lines
		rect = new Rectangle(xMargin, yMargin, 0, getHeight() - yMargin - heightIncrement);
		for ( int i = 0; i < getWidth() - widthIncrement - xMargin; i += widthIncrement )
		{
			drawLine( g2, rect, Color.BLACK );
			rect.x += widthIncrement;
		}
	}
	
	private void drawBlocks(Graphics2D g2, double aDeltaTime)
	{
		for ( Tuple<Car, Long, Long> lItem : getGUI().getChargerSystem().getChargeQueue() )
		{
			
		}
		
		String s = "" + getGUI().getChargerSystem().getChargeQueue().size();
		drawString(g2, s, 10, 10, Color.BLACK);
	}
	
}
