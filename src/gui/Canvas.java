package gui;

import java.awt.*;
import javax.swing.*;

import model.Car;

import java.util.Random;

public class Canvas extends JPanel
{
	private GUI gui;
	private CanvasLooper looper;
	
	Random random = new Random();
	
	public Canvas(GUI aGUI, int x, int y, int width, int height)
	{
		super();
		setBounds( x,y,width,height );
		setLayout(null);
		gui = aGUI;
		looper = new CanvasLooper(this);
	}
	
	public void refresh()
	{
		validate();
		repaint();
	}
	
	public void paintComponent(Graphics g)
    {
		Graphics2D g2 = (Graphics2D) g;
		
		double deltaTime = looper.getDeltaTime();
		
		// clear screen white
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		drawCells(g2,deltaTime);
    }
	
	private void drawCells(Graphics2D g2, double aDeltaTime)
	{
		int width = getWidth();
		int height = getHeight();
		int numAgents = getGUI().getChargerSystem().getCarAgents().size();
		int wantedCellWidth = 160;
		double cellSizeIncrement = 0.75;
		
		int numColumns = (int) Math.floor( width / wantedCellWidth );
		int numRows = (int) Math.floor( height / wantedCellWidth );
		
		while ( numColumns * numRows < numAgents )
		{
			wantedCellWidth *= cellSizeIncrement;
			numColumns = (int) Math.floor( width / wantedCellWidth );
			numRows = (int) Math.floor( height / wantedCellWidth );
		}
		
		int x = 0;
		int y = 0;
		int w = wantedCellWidth;
		int h = wantedCellWidth;
		int count = 0;
		
		int a = 240;
		Color colorA = new Color(a,a,a);
		int b = 230;
		Color colorB = new Color(b,b,b);
		Color colorCurrent = colorA;
		
		for ( int row = 0; row < numRows; row++ )
		{
			for ( int col = 0; col < numColumns; col++ )
			{
				g2.setColor( colorCurrent );
				g2.fillRect(x, y, w, h);
				
				int padding = (int) (wantedCellWidth*0.1);
				Car carAgent = getGUI().getChargerSystem().getCarAgents().get(count);
				drawAgent( g2, aDeltaTime, x+padding, y+padding, w-(padding*2), h-(padding*2), carAgent );
				
				x += w;
				count++;
				
				if ( colorCurrent == colorA )
					colorCurrent = colorB;
				else
					colorCurrent = colorA;
				
				if ( count >= numAgents )
					break;
			}
			y += h;
			x = 0;
			if ( count >= numAgents )
				break;
		}
		
	}
	
	private void drawAgent(Graphics2D g2, double aDeltaTime, int x, int y, int w, int h, Car aCar)
	{
		if ( aCar.isRunning() )
		{
			double speed = 0.05;
			int arcAngle = 70;
			
			g2.setColor( Color.BLACK );
			g2.setStroke( new BasicStroke( 10 ) );
			
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle(), arcAngle);
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle()+90, arcAngle);
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle()+180, arcAngle);
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle()+270, arcAngle);
			
			aCar.setStartAngle( aCar.getStartAngle() + speed * aDeltaTime );
			
			if ( aCar.getStartAngle() >= 360 )
				aCar.setStartAngle( aCar.getStartAngle() - 360 );
			
			int xCenter = x += ( w/2 );
			int yCenter = y += ( h/2 );
			int width = w/2;
			int height = h/2;
			
			if ( aCar.getCarState() == Car.STATE.CHARGE )
			{
				// green vertical bar
				g2.setColor( Color.GREEN );
				g2.fillRect(x, y, w, h);
				
			}
			else if ( aCar.getCarState() == Car.STATE.IDEL )
			{
				// yellow circle
				g2.setColor( Color.YELLOW );
				g2.fillOval(xCenter, yCenter, width, height);
			}
			else
			{
				// red bar
				g2.setColor( Color.RED );
			}
			
		}
		else
		{
			int arcAngle = 70;
			
			g2.setColor( Color.RED );
			g2.setStroke( new BasicStroke( 10 ) );
			
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle(), arcAngle);
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle()+90, arcAngle);
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle()+180, arcAngle);
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle()+270, arcAngle);
		}
		
	}
	
	public Color getRandomColor()
	{
		int a = 200;
		int b = 40;
		return new Color( random.nextInt(a)+b, random.nextInt(a)+b, random.nextInt(a)+b );
	}
	
	public void startLooper()
	{
		looper.run();
	}
	
	public GUI getGUI()
	{
		return gui;
	}
	
}
