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
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
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
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		drawCells(g2,deltaTime);
		
		drawKey(g2);
    }
	
	private void drawKey(Graphics2D g2)
	{
		g2.setColor( Color.BLACK );

		String outerCircle1 = "black dotted spinning outer circle = car agent is cycling (running)";
		String outerCircle2 = "black dotted outer circle not spinning = canvas told to not update (option in options tab)";
		String outerCircle3 = "red dotted outer circle = car agent is not cycling (stopped)";

		String innerCircle1 = "green inner circle = car agent is charging";
		String innerCircle2 = "yellow inner circle = car agent is ideling";
		String innerCircle3 = "red inner circle = something is wrong with car agent (no state or other)";
		
		int x = 20;
		int y = 500;
		int yIncrement = 20;
		
		g2.drawString(outerCircle1, x, y);
		y += yIncrement;
		g2.drawString(outerCircle2, x, y);
		y += yIncrement;
		g2.drawString(outerCircle3, x, y);
		y += yIncrement;
		g2.drawString(innerCircle1, x, y);
		y += yIncrement;
		g2.drawString(innerCircle2, x, y);
		y += yIncrement;
		g2.drawString(innerCircle3, x, y);
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
			
			g2.setColor( useColor );
			g2.fillRect( cell.getX(), cell.getY(), cell.getW(), cell.getH() );
			
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
	
	private void drawAgent(Graphics2D g2, double aDeltaTime, int x, int y, int w, int h, Car aCar, Cell aCell)
	{
		if ( gui.getChargerSystem().isCharging() )
		{
			double speed = 0.05;
			int arcAngle = 70;
			
			g2.setColor( Color.BLACK );
			g2.setStroke( new BasicStroke( (int) ( aCell.getStrokeWidth() * aCell.getStrokeScale() ) ) );
			
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle(), arcAngle);
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle()+90, arcAngle);
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle()+180, arcAngle);
			g2.drawArc(x, y, w, h, (int) aCar.getStartAngle()+270, arcAngle);
			
			aCar.setStartAngle( aCar.getStartAngle() + speed * aDeltaTime );
			
			if ( aCar.getStartAngle() >= 360 )
				aCar.setStartAngle( aCar.getStartAngle() - 360 );
			
			int width = w/2;
			int height = h/2;
			int xCenter = x + width;
			int yCenter = y + height;
			
			if ( aCar.getCarState() == Car.STATE.CHARGE )
			{
				// green circle
				g2.setColor( Color.GREEN );
				g2.fillOval( xCenter-(width/2), yCenter-(height/2), width, height );
				
			}
			else if ( aCar.getCarState() == Car.STATE.IDEL )
			{
				// yellow circle
				g2.setColor( Color.YELLOW );
				g2.fillOval( xCenter-(width/2), yCenter-(height/2), width, height );
			}
			else
			{
				// red circle
				g2.setColor( Color.RED );
				g2.fillOval( xCenter-(width/2), yCenter-(height/2), width, height );
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
