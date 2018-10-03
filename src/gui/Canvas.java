package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import model.*;
import java.util.ArrayList;
import java.util.Random;

public class Canvas extends JPanel
{
	private GUI gui;
	private CanvasLooper looper;
	
	private ArrayList<Cell> cells;
	
	private Color cellColorA;
	private Color cellColorB;
	private Color cellColorCurrent;
	private Color cellColorSelected;
	
	private MouseListener ml;
	
	Random random = new Random();
	
	public Canvas(GUI aGUI, int x, int y, int width, int height)
	{
		super();
		setBounds( x,y,width,height );
		setLayout(null);
		gui = aGUI;
		looper = new CanvasLooper(this);
		
		cells = new ArrayList<Cell>();
		
		initMouseListener();
		
		addMouseListener(ml);
		
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
	}
	
	private void initMouseListener()
	{
		ml = new MouseListener() {
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
				// find out what cell was clicked in
				for ( Cell cell : cells )
				{
					if ( cell.containsPoint(e.getPoint()) )
					{
						getGUI().setSelectedCar( cell.getCar() );
						getGUI().refreshSideBar();
						break;
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
		
		drawCells(g2,deltaTime);
    }
	
	private void drawCells(Graphics2D g2, double aDeltaTime)
	{
		// only recalculate cells if something has changed of this is the first loop
		// if the number of cells does not match the number of agents is on case of this
		// TODO a flag will have to be set to say if something else might have changed
		
		int numAgents = getGUI().getChargerSystem().getCarAgents().size();
		int numCells = cells.size();
		
		if ( numAgents != numCells )
		{
			populateCells(numAgents);
		}
		
		cellColorCurrent = cellColorA;
		
		// draw each cell
		for ( Cell cell : cells )
		{
			Color useColor = cellColorCurrent;
			if ( cell.getCar() != null && getGUI().getSelectedCar() != null && cell.getCar() == getGUI().getSelectedCar() )
				useColor = cellColorSelected;
			
			fillRect( g2, cell.getRect(), useColor );
			
			int paddingW = (int) (cell.getW()*0.1);
			int paddingH = (int) (cell.getH()*0.1);
			
			drawAgent( g2, aDeltaTime, cell.getX()+paddingW, cell.getY()+paddingH, cell.getW()-(paddingW*2), cell.getH()-(paddingH*2), cell.getCar(), cell );
			
			if ( cellColorCurrent == cellColorA )
				cellColorCurrent = cellColorB;
			else
				cellColorCurrent = cellColorA;
		}
		
		
	}
	
	public void populateCells(int aNumAgents)
	{
		cells = new ArrayList<>();
		
		int width = getWidth();
		int height = getHeight();
		int wantedCellWidth = 160;
		double cellSizeIncrement = 0.75;
		
		int numColumns = (int) Math.floor( width / wantedCellWidth );
		int numRows = (int) Math.floor( height / wantedCellWidth );
		
		int num = numColumns * numRows;
		boolean a = num < aNumAgents;
		
		while ( a )
		{
			num = numColumns * numRows;
			a = num < aNumAgents;
			
			wantedCellWidth *= cellSizeIncrement;
			numColumns = (int) Math.floor( width / wantedCellWidth );
			numRows = (int) Math.floor( height / wantedCellWidth );
		}
		
		int x = 0;
		int y = 0;
		int w = wantedCellWidth;
		int h = wantedCellWidth;
		int count = 0;
		
		for ( int row = 0; row < numRows; row++ )
		{
			for ( int col = 0; col < numColumns; col++ )
			{
				Car carAgent = getGUI().getChargerSystem().getCarAgents().get(count);
				
				cells.add( new Cell( x, y, w, h, carAgent, 10, wantedCellWidth/160 ) );
				
				x += w;
				count++;
				
				if ( count >= aNumAgents )
					break;
			}
			y += h;
			x = 0;
			if ( count >= aNumAgents )
				break;
		}
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
	
	private void drawRotatingCircle(Graphics2D g2, int x, int y, int w, int h, int aStarAngle, int aArcAngle, int aStrokeWidth, double aStrokeScale, Color aColor)
	{
		g2.setColor( aColor );
		g2.setStroke( new BasicStroke( (int) ( aStrokeWidth * aStrokeScale ) ) );
		
		g2.drawArc(x, y, w, h, aStarAngle, aArcAngle);
		g2.drawArc(x, y, w, h, aStarAngle+90, aArcAngle);
		g2.drawArc(x, y, w, h, aStarAngle+180, aArcAngle);
		g2.drawArc(x, y, w, h, aStarAngle+270, aArcAngle);
	}
	
	private void drawAgent(Graphics2D g2, double aDeltaTime, int x, int y, int w, int h, Car aCar, Cell aCell)
	{
		if ( gui.getChargerSystem().isCharging() )
		{
			double speed = 0.05;
			
			drawRotatingCircle(g2, x, y, w, h, (int) aCar.getStartAngle(), 70,  aCell.getStrokeWidth(), aCell.getStrokeScale(), Color.BLACK);
			drawStatusCircle( g2, aCar, x, y, w, h );
			drawChargeBar( g2, aCar, x, y, w, h );
			
			aCar.setStartAngle( aCar.getStartAngle() + speed * aDeltaTime );
			
			if ( aCar.getStartAngle() >= 360 )
				aCar.setStartAngle( aCar.getStartAngle() - 360 );
		}
		else
		{
			drawRotatingCircle(g2, x, y, w, h, (int) aCar.getStartAngle(), 70,  aCell.getStrokeWidth(), aCell.getStrokeScale(), Color.RED);
		}
		
	}
	
	private void drawStatusCircle(Graphics2D g2, Car aCar, int x, int y, int w, int h)
	{
		Rectangle innerRect = new Rectangle();
		innerRect.width = w/2;
		innerRect.height = h/2;
		innerRect.x = x + (innerRect.width/2);
		innerRect.y = y + (innerRect.height/2);
		
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
		else
		{
			fillOval( g2, innerRect, Color.RED );
		}
	}
	
	private void drawChargeBar(Graphics2D g2, Car aCar, int x, int y, int w, int h)
	{
		Color back = new Color( 100, 100, 100, 100 );
		Color fore = new Color( 100, 200, 100, 200 );
		int xCenter = x + (w/2);
		int yCenter = y + (h/2);
		int barWidth = w/5;
		int barHeight = h/3;
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
