package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.*;

@SuppressWarnings("serial")
public class CanvasTab extends JPanel
{
	private Canvas canvas;
	
	public CanvasTab(Canvas aCanvas)
	{
		super();
		canvas = aCanvas;
		setLayout(null);
		setBounds(canvas.getX(), canvas.getY(), canvas.getWidth(), canvas.getHeight());
	}
	
	public void updateSize(int x, int y, int width, int height)
	{
		setBounds( x,y,width,height );
	}
	
	public void drawString(Graphics2D g2, String aString, int x, int y, Color aColor)
	{
		g2.setColor(aColor);
		g2.drawString(aString, x, y);
	}
	
	public void fillOval(Graphics2D g2, Rectangle aRect, Color aColor)
	{
		g2.setColor( aColor );
		g2.fillOval( aRect.x, aRect.y, aRect.width, aRect.height );
	}
	
	public void fillRect(Graphics2D g2, Rectangle aRect, Color aColor)
	{
		g2.setColor( aColor );
		g2.fillRect( aRect.x, aRect.y, aRect.width, aRect.height );
	}
	
	public void drawLine(Graphics2D g2, Rectangle aRect, Color aColor)
	{
		g2.setColor( aColor );
		g2.drawLine( aRect.x, aRect.y, aRect.x+aRect.width, aRect.y+aRect.height );
	}
	
	public void drawPoint(Graphics2D g2, Point aPoint, int aRadius, Color aColor)
	{
		g2.setColor(aColor);
		g2.fillOval(aPoint.x-aRadius, aPoint.y-aRadius, aRadius*2, aRadius*2);
	}
	
	public void drawPolyLine(Graphics2D g2, int[] aXPoints, int[] aYPoints, Color aColor)
	{
		g2.setColor(aColor);
		g2.drawPolyline(aXPoints, aYPoints, aXPoints.length);
	}
	
	public CanvasLooper getLooper()
	{
		return getCanvas().getLooper();
	}
	
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	public GUI getGUI()
	{
		return getCanvas().getGUI();
	}
}
