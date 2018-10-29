package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import model.Car;

@SuppressWarnings("serial")
public class CanvasTabCarProfile extends CanvasTab
{
	// padding from top, bottom, left and right
	private int margin;
	
	public CanvasTabCarProfile(Canvas aCanvas)
	{
		super(aCanvas);
		
		margin = 20;
		
	}
	
	public void paintComponent(Graphics g)
    {
		Graphics2D g2 = (Graphics2D) g;
		double deltaTime = getLooper().getDeltaTime();
		// clear screen white
		fillRect( g2, new Rectangle(0,0,getWidth(),getHeight()), ColorIndex.canvasFill );
		
		if ( getGUI().getSelectedCar() == null )
		{
			// draw some fake bars
			
			int y = 572;
			int width = 809;
			// Vertical
			drawLine(g2, new Rectangle(margin, margin, 0, y-margin), Color.BLACK);
			// Horizontal
			drawLine(g2, new Rectangle(margin, y, width, 0), Color.BLACK);
			
			return;
		}
		
		// short had to current car
		Car car = getGUI().getSelectedCar();
		// short hand to number of points
		int numPoints = car.getProfile().getUsage().size();
		// width of space to fill
		int width = getWidth() - margin*2;
		// height of space to fill
		int height = getHeight() - margin*2;
		// distance between each point
		int pointDist = width/numPoints;
		
		// x position count
		int x = margin;
		int y = height - margin*4;
		
		// point save for line
		ArrayList<Point> points = new ArrayList<>();
		
		// draw each point
		for ( Integer i : car.getProfile().getUsage() )
		{
//			System.out.println( "i " + i );
			double p = i / 100.0;
			int u = (int) ( y - ( p * height ) );
			Point point = new Point( x, u );
			drawPoint(g2, point, 2, Color.RED);
			x += pointDist;
			
			points.add( point );
			
//			System.out.println( point );
		}
		
		// convert arraylist points to int[] x, int[] y for line
		int s = points.size();
		int[] xPoints = new int[s];
		int[] yPoints = new int[s];
		
		
		for ( int i = 0; i < s; i++ )
		{
			xPoints[i] = points.get(i).x;
			yPoints[i] = points.get(i).y;
		}
		
		drawPolyLine(g2, xPoints, yPoints, Color.BLACK);
		
		// draw x, y bars
		// Vertical
		drawLine(g2, new Rectangle(margin, margin, 0, y-margin), Color.BLACK);
		
		// Horizontal
		drawLine(g2, new Rectangle(margin, y, width, 0), Color.BLACK);
		// label
		drawString(g2, "Car usage over 24 hours, 8 points per hour", margin*4, y+margin, Color.BLACK);
		
    }
	
}
