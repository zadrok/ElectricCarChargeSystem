package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import model.*;
import java.util.Random;

@SuppressWarnings("serial")
public class Canvas extends JPanel
{
	private GUI gui;
	private CanvasLooper looper;
	private Color cellColorA;
	private Color cellColorB;
	private Color cellColorCurrent;
	private Color cellColorSelected;
	Random random = new Random();
	private Rectangle chargePointRect;
	private Rectangle carRect;
	private Color chargePointRectColor;
	private Color carRectColor;
	private double screenSplit;
	private int titleOffset;
	private int titlePadding;
	private double animateSpeed;
	private int strokeWidth;
	private double strokeScale;
	private double rotatingCircleScale;
	private int cellGap;
	
	public Canvas(GUI aGUI, int x, int y, int width, int height)
	{
		super();
		updateSize( x,y,width,height );
		setLayout(null);
		gui = aGUI;
		looper = new CanvasLooper(this);
		
		screenSplit = 3;
		titleOffset = 15;
		titlePadding = titleOffset+5;
		animateSpeed = 0.05;
		strokeWidth = 5;
		strokeScale = 1.0;
		rotatingCircleScale = 0.8;
		cellGap = 10;
		
		addMouseListener( mouseListener() );
		
		chargePointRectColor = new Color( 50, 100, 50, 50 );
		carRectColor = new Color( 100, 50, 50, 50 );
		
		int a = 240;
		cellColorA = new Color(a,a,a);
		int b = 230;
		cellColorB = new Color(b,b,b);
		cellColorCurrent = cellColorA;
		int c = 180;
		cellColorSelected = new Color(c,c,c);
	}
	
	public void updateSize(int x, int y, int width, int height)
	{
		setBounds( x,y,width,height );
		int partHeight = (int) (getHeight()/screenSplit);
		chargePointRect = new Rectangle( 0, 0, getWidth(), partHeight );
		carRect = new Rectangle( 0, partHeight, getWidth(), height-partHeight );
	}
	
	private MouseListener mouseListener()
	{
		return new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) { }
			@Override
			public void mousePressed(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) { }
			@Override
			public void mouseEntered(MouseEvent e) { }
			@Override
			public void mouseClicked(MouseEvent e)
			{
//				getGUI().getChargerSystem().getChargePoints().get(0).AddCar( getGUI().getChargerSystem().getCarAgents().get(0) );
				
				// limit search
				if ( chargePointRect.contains( e.getPoint() ) )
				{
					Rectangle drawAreaRect = makeDrawArea(chargePointRect);
					
					int numPoints = getGUI().getChargerSystem().getChargePoints().size();
					int[] info = makeRectInfo( drawAreaRect, numPoints );
					Rectangle rect = new Rectangle( info[0], info[1], info[2], info[2] );
					int i = info[3];
					int count = 0;
					
					for ( ChargePoint lCPoint : getGUI().getChargerSystem().getChargePoints() )
					{
						if ( rect.contains( e.getPoint() ) )
						{
							getGUI().setSelectedChargePoint( lCPoint );
							break;
						}
						
						rect.x += rect.width + cellGap;
						count++;
						if ( count >= i )
						{
							rect.x = cellGap;
							rect.y += rect.height + cellGap;
							count = 0;
						}
					}
					
					
				}
				else if ( carRect.contains( e.getPoint() ) )
				{
					
					Rectangle drawAreaRect = makeDrawArea( carRect );

					int numCars = getGUI().getChargerSystem().getCarAgents().size();
					int[] info = makeRectInfo( drawAreaRect, numCars );
					Rectangle rect = new Rectangle( info[0], info[1], info[2], info[2] );
					int i = info[3];
					int count = 0;

					for ( Car lCar : getGUI().getChargerSystem().getCarAgents() )
					{
						if ( getGUI().getChargerSystem().carOnCharge( lCar ) )
							continue;
						
						if ( rect.contains( e.getPoint() ) )
						{
							getGUI().setSelectedCar( lCar );
							break;
						}
						
						rect.x += rect.width + cellGap;
						count++;
						if ( count >= i )
						{
							rect.x = cellGap;
							rect.y += rect.height + cellGap;
							count = 0;
						}
					}
					
				}
				
			}
		};
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
		fillRect( g2, new Rectangle(0,0,getWidth(),getHeight()), Color.WHITE );
		
		chargePointController(g2,deltaTime);
		carController(g2,deltaTime);
    }
	
	private void chargePointController(Graphics2D g2, double aDeltaTime)
	{
		fillRect( g2, chargePointRect, chargePointRectColor );
		drawString( g2, "Charge Points", chargePointRect.x, chargePointRect.y+titleOffset, Color.BLACK );
		
		Rectangle drawAreaRect = makeDrawArea(chargePointRect);
		
		int numPoints = getGUI().getChargerSystem().getChargePoints().size();
		int[] info = makeRectInfo( drawAreaRect, numPoints );
		Rectangle rect = new Rectangle( info[0], info[1], info[2], info[2] );
		int i = info[3];
		int count = 0;
		
		for ( ChargePoint lCPoint : getGUI().getChargerSystem().getChargePoints() )
		{
			drawChargePoint( g2, aDeltaTime, lCPoint, rect );
			
			rect.x += rect.width + cellGap;
			count++;
			if ( count >= i )
			{
				rect.x = cellGap;
				rect.y += rect.height + cellGap;
				count = 0;
			}
		}
	}
	
	private void carController(Graphics2D g2, double aDeltaTime)
	{
		fillRect( g2, carRect, carRectColor );
		drawString( g2, "Cars", carRect.x, carRect.y+titleOffset, Color.BLACK );
		
		Rectangle drawAreaRect = makeDrawArea( carRect );

		int numCars = getGUI().getChargerSystem().getCarAgents().size();
		int[] info = makeRectInfo( drawAreaRect, numCars );
		Rectangle rect = new Rectangle( info[0], info[1], info[2], info[2] );
		int i = info[3];
		int count = 0;

		for ( Car lCar : getGUI().getChargerSystem().getCarAgents() )
		{
			if ( getGUI().getChargerSystem().carOnCharge( lCar ) )
				continue;
			
			drawCar( g2, aDeltaTime, lCar, rect );
			
			rect.x += rect.width + cellGap;
			count++;
			if ( count >= i )
			{
				rect.x = cellGap;
				rect.y += rect.height + cellGap;
				count = 0;
			}
		}
		
	}
	
	private void drawChargePoint(Graphics2D g2, double aDeltaTime, ChargePoint aPoint, Rectangle aRect)
	{
		if ( aPoint.getCar() != null )
		{
			if ( aPoint == getGUI().getSelectedChargePoint() )
				fillRect( g2, aRect, new Color( 200,100,200,100 ) );
			
			drawCar( g2, aDeltaTime, aPoint.getCar(), aRect );
		}
		else
		{
			// draw background
			if ( aPoint == getGUI().getSelectedChargePoint() )
				fillRect( g2, aRect, new Color( 200,100,200,100 ) );
			else
				fillRect( g2, aRect, new Color( 100,100,100,100 ) );
			// draw other
			int xCenter = aRect.x + (aRect.width/2);
			int yCenter = aRect.y + (aRect.height/2);
			int barWidth = (int) (aRect.width/1.5);
			int barHeight = aRect.height/4;
			Rectangle barRect = new Rectangle();
			barRect.x = xCenter - ( barWidth/2 );
			barRect.y = yCenter - ( barHeight/2 );
			barRect.width = barWidth;
			barRect.height = barHeight;
			fillRect( g2, barRect, new Color( 50, 200, 50, 200 ) );
		}
		
		
	}
	
	private void drawCar(Graphics2D g2, double aDeltaTime, Car aCar, Rectangle aRect)
	{
		// draw background
		if ( aCar == getGUI().getSelectedCar() )
			fillRect( g2, aRect, new Color( 200,100,200,100 ) );
		else
			fillRect( g2, aRect, new Color( 200,100,100,100 ) );
		
		// draw other
		if ( gui.getChargerSystem().isCharging() )
		{
			drawRotatingCircle(g2, aRect, (int) aCar.getStartAngle(), 70,  strokeWidth, strokeScale, Color.BLACK);
			
			aCar.setStartAngle( aCar.getStartAngle() + animateSpeed * aDeltaTime );
			
			if ( aCar.getStartAngle() >= 360 )
				aCar.setStartAngle( aCar.getStartAngle() - 360 );
		}
		else
		{
			drawRotatingCircle(g2, aRect, (int) aCar.getStartAngle(), 70,  strokeWidth, strokeScale, Color.RED);
		}
		
		drawStatusCircle( g2, aCar, aRect );
		drawChargeBar( g2, aCar, aRect );
		drawString( g2, ""+aCar.getID(), aRect.x, aRect.y+10, Color.DARK_GRAY );
	}
	
	private Rectangle makeDrawArea(Rectangle aRect)
	{
		return new Rectangle(aRect.x, aRect.y+titlePadding, aRect.width, aRect.height-titlePadding);
	}
	
	private int[] makeRectInfo(Rectangle aRect, int aNum)
	{
		int x = aRect.x + cellGap;
		int y = aRect.y;
		
		int wStart = 81;
		int w = wStart;
		
		boolean fit = false;
		int numColumns = 0;
		int numRows = 0;
		
		while ( !fit )
		{
			w -= 1;
			numColumns = (int) Math.floor( aRect.width / (w+cellGap) ) - 1;
			numRows = (int) Math.floor( aRect.height / (w+cellGap) ) - 1;
			if ( numColumns * numRows >= aNum )
				fit = true;
		}
		
		int wEnd = w;
		
		int[] data = { x, y, w, numColumns }; 
		return data;
	}
	
	private void drawString(Graphics2D g2, String aString, int x, int y, Color aColor)
	{
		g2.setColor(aColor);
		g2.drawString(aString, x, y);
	}
	
	private void fillOval(Graphics2D g2, Rectangle aRect, Color aColor)
	{
		g2.setColor( aColor );
		g2.fillOval( aRect.x, aRect.y, aRect.width, aRect.height );
	}
	
	private void fillRect(Graphics2D g2, Rectangle aRect, Color aColor)
	{
		g2.setColor( aColor );
		g2.fillRect( aRect.x, aRect.y, aRect.width, aRect.height );
	}
	
	private void drawRotatingCircle(Graphics2D g2, Rectangle aRect, int aStarAngle, int aArcAngle, int aStrokeWidth, double aStrokeScale, Color aColor)
	{
		g2.setColor( aColor );
		g2.setStroke( new BasicStroke( (int) ( aStrokeWidth * aStrokeScale ) ) );
		
		Rectangle sRect = new Rectangle( aRect.x, aRect.y, aRect.width, aRect.height );
		
		int width = (int) (aRect.width * rotatingCircleScale);
		int height = (int) (aRect.height * rotatingCircleScale);
		int xDif = aRect.width - width;
		int yDif = aRect.height - height;
		int x = aRect.x + ( xDif/2 );
		int y = aRect.y + ( yDif/2 );
		
		g2.drawArc(x, y, width, height, aStarAngle, aArcAngle);
		g2.drawArc(x, y, width, height, aStarAngle+90, aArcAngle);
		g2.drawArc(x, y, width, height, aStarAngle+180, aArcAngle);
		g2.drawArc(x, y, width, height, aStarAngle+270, aArcAngle);
	}
	
	private void drawStatusCircle(Graphics2D g2, Car aCar, Rectangle aRect)
	{
		Rectangle innerRect = new Rectangle();
		innerRect.width = aRect.width/2;
		innerRect.height = aRect.height/2;
		innerRect.x = aRect.x + (innerRect.width/2);
		innerRect.y = aRect.y + (innerRect.height/2);
		
		if ( aCar.getCarState() == Car.STATE.CHARGE )
		{
			fillOval( g2, innerRect, Color.ORANGE );
		}
		else if ( aCar.getCarState() == Car.STATE.IDLE )
		{
			fillOval( g2, innerRect, Color.YELLOW );
		}
		else if ( aCar.getCarState() == Car.STATE.CHARGING)
		{
			fillOval( g2, innerRect, Color.GREEN );
		}
		else if ( aCar.getCarState() == Car.STATE.BURN)
		{
			fillOval( g2, innerRect, Color.MAGENTA );
		}
		else
		{
			fillOval( g2, innerRect, Color.RED );
		}
	}
	
	private void drawChargeBar(Graphics2D g2, Car aCar, Rectangle aRect)
	{
		Color back = new Color( 100, 100, 100, 100 );
		Color fore = new Color( 100, 200, 100, 200 );
		int xCenter = aRect.x + (aRect.width/2);
		int yCenter = aRect.y + (aRect.height/2);
		int barWidth = aRect.width/5;
		int barHeight = aRect.height/3;
		Rectangle barRect = new Rectangle();
		barRect.x = xCenter - ( barWidth/2 );
		barRect.y = yCenter - ( barHeight/2 );
		barRect.width = barWidth;
		barRect.height = barHeight;
		fillRect( g2, barRect, back );
		double current = aCar.getCurrentCharge();
		double max = aCar.getMaxChargeCapacity();
		double chargePercentage = 1 - ( current / max );
		int remove = (int) (barRect.height * chargePercentage);
		barRect.y += remove;
		barRect.height -= remove;
		fillRect( g2, barRect, fore );
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
