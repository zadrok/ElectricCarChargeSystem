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
	// short had to current car
	Car car;
	// short hand to number of points
	int numPoints;
	// width of space to fill
	int width;
	// height of space to fill
	int height;
	// distance between each point
	int pointDist;
	// x position count
	int x;
	int y;
	
	public CanvasTabCarProfile(Canvas aCanvas)
	{
		super(aCanvas);
		margin = 30;
	}
	
	public void paintComponent(Graphics g)
    {
		Graphics2D g2 = (Graphics2D) g;
		double deltaTime = getLooper().getDeltaTime();
		// clear screen white
		fillRect( g2, new Rectangle(0,0,getWidth(),getHeight()), ColorIndex.canvasFill );
		
		drawAxis(g2);
		
		if ( getGUI().getSelectedCar() == null )
			return;
		
		// collect and store data
		
		// short had to current car
		car = getGUI().getSelectedCar();
		// short hand to number of points
		numPoints = car.getProfile().getUsage().size();
		// width of space to fill
		width = getWidth() - margin*2;
		// height of space to fill
		height = getHeight() - margin*2;
		// distance between each point
		pointDist = width/numPoints;
		
		// x position count
		x = margin;
		y = height - margin*4;
		
		// return the points to draw the line between them
		ArrayList<Point> points = makeGraphPoints(g2);
		
		// draw graph points
		drawGraphPoints(g2, points);
		
		// draw line between points
		drawGraphLine(g2, points);
		
		// draw Horizontal axis markers
		drawAxisMarkers(g2, points);
		
		// draw line marking highest value
		drawHighestValue(g2, points);
    }
	
	private void drawHighestValue(Graphics2D g2, ArrayList<Point> aPoints)
	{
		// find highest value index
		int index = -1;
		int highest = -1;
		for ( int i = 0; i < car.getProfile().getUsage().size(); i++ )
		{
			if ( highest < car.getProfile().getUsage().get(i) )
			{
				highest = car.getProfile().getUsage().get(i);
				index = i;
			}
		}
		
		// get the point at that index
		Point p = aPoints.get(index);
		
		// draw line
		drawLine(g2, new Rectangle(margin, p.y, width, 0), Color.GREEN);
		//draw label for line
		drawString(g2, ""+highest, margin-20, p.y, Color.GREEN);
	}
	
	private void drawAxisMarkers(Graphics2D g2, ArrayList<Point> aPoints)
	{
		// take point x position and Horizontal axis y position and draw a mark
		// only draw points every hour
		// car profile knows how many points are per hour
		int count = 1;
		for ( int i = 0; i < aPoints.size(); i++ )
		{
			Point p = aPoints.get(i);
			
			if ( i == 0 || i % car.getProfile().getTimesPerHour() != 0 )
				continue;
			
			drawPoint(g2, new Point( p.x, y ), 4, Color.MAGENTA);
			
			// draw number under marker
			drawString(g2, ""+count, p.x-5, y+15, Color.BLACK);
			count++;
		}
	}
	
	private void drawGraphLine(Graphics2D g2, ArrayList<Point> aPoints)
	{
		// convert arraylist points to int[] x, int[] y for line
		int s = aPoints.size();
		int[] xPoints = new int[s];
		int[] yPoints = new int[s];
		
		for ( int i = 0; i < s; i++ )
		{
			xPoints[i] = aPoints.get(i).x;
			yPoints[i] = aPoints.get(i).y;
		}
		
		drawPolyLine(g2, xPoints, yPoints, Color.BLACK);
	}
	
	private void drawGraphPoints(Graphics2D g2, ArrayList<Point> aPoints)
	{
		for ( Point p : aPoints )
			drawPoint(g2, p, 2, Color.RED);
	}
	
	private ArrayList<Point> makeGraphPoints(Graphics2D g2)
	{
		// point save for line
		ArrayList<Point> points = new ArrayList<>();
		
		// draw each point
		for ( Integer i : car.getProfile().getUsage() )
		{
//			System.out.println( "i " + i );
			double p = i / 100.0;
			int u = (int) ( y - ( p * height ) );
			Point point = new Point( x, u );
			x += pointDist;
			points.add( point );
//			System.out.println( point );
		}
		return points;
	}
	
	private void drawAxis(Graphics2D g2)
	{
		int y = getHeight() - margin*6;
		int width = getWidth() - margin*2;
		// Vertical
		drawLine(g2, new Rectangle(margin, margin, 0, y-margin), Color.BLACK);
		// label
		drawString(g2, "0", margin-10, y, Color.BLACK);
		drawString(g2, "100", margin-20, (int) (margin*1.5), Color.BLACK);
		
		// Horizontal
		drawLine(g2, new Rectangle(margin, y, width, 0), Color.BLACK);
		// label
		drawString(g2, "Car usage over 24 hours, 8 points per hour", margin*4, y+30, Color.BLACK);
	}
	
}
