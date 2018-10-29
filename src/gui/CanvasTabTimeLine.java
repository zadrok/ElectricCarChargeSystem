package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import boot.GlobalVariables;
import model.Car;
import model.ChargerSystem;
import model.ChargerSystem.Tuple;

@SuppressWarnings("serial")
public class CanvasTabTimeLine extends CanvasTab
{
	// offset from zero, for scrolling
	private double offsetVal;
	// distance from left wall
	private int xMargin;
	// distance from top
	private int yMargin;
	// horizontal gap size
	private int widthIncrement;
	// vertical gap size
	private int heightIncrement;
	// block of time size
	private int block;
	
	public CanvasTabTimeLine(Canvas aCanvas)
	{
		super(aCanvas);
		
		offsetVal = 0;
		xMargin = 5;
		yMargin = 5;
		widthIncrement = 30;
		heightIncrement = 30;
		block = 30;
		
		addMouseListener( mouseListener() );
		addMouseMotionListener( mouseMotionListener() );
		addMouseWheelListener( mouseWheelListener() );
	}
	
	private MouseWheelListener mouseWheelListener()
	{
		return new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if ( e.getWheelRotation() >= 1 )
				{
					offsetVal--;
				}
				else if ( e.getWheelRotation() <= -1 )
				{
					offsetVal++;
				}
			}
		};
	}
	
	private MouseListener mouseListener()
	{
		return new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		};
	}
	
	private MouseMotionListener mouseMotionListener()
	{
		return new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				offsetVal++;
			}
		};
	}
	
	public void paintComponent(Graphics g)
    {
		Graphics2D g2 = (Graphics2D) g;
		// time since last run, use to smooth animations
		double deltaTime = getLooper().getDeltaTime();
		// current simulation run time
		ChargeTime ct = new ChargeTime(GlobalVariables.runTime);
		// clear screen white
		fillRect( g2, new Rectangle(0,0,getWidth(),getHeight()), ColorIndex.canvasFill );
		
		drawGraphBack(g2,deltaTime,ct);
		drawBlocks(g2,deltaTime,ct);
		drawGraphFront(g2,deltaTime,ct);
    }
	
	private void drawGraphBack(Graphics2D g2, double aDeltaTime, ChargeTime aCT)
	{
		
	}
	
	private void drawGraphFront(Graphics2D g2, double aDeltaTime, ChargeTime aCT)
	{
		
		
		// draw horizontal lines
		int count = 0;
		Rectangle rect = new Rectangle(xMargin, yMargin, getWidth() - xMargin - widthIncrement, 0);
		for ( int i = 0; i < getHeight() - heightIncrement - yMargin; i += heightIncrement )
		{
			drawLine( g2, rect, Color.BLACK );
			rect.y += heightIncrement;
			count++;
		}
		
		// draw time on horizontal lines
		// make this number the center value
		int centerLineNum = count/4;
		
//		------------------
//		-02:00------------
//		------------------
//		00:00------------- center point is focus
//		------------------
//		+02:00------------
//		------------------
		
		
		// draw horizontal lines test (time)
		rect = new Rectangle(xMargin, yMargin, getWidth() - xMargin - widthIncrement, 0);
		for ( int i = 0; i < count; i++ )
		{
			int numBlocks = count - ( centerLineNum - i );
			long[] times = aCT.addMinutes(block*(i-centerLineNum));
			
			String s = String.format("%02d:%02d", times[0], times[1]);
			drawString(g2, s, rect.x, rect.y, Color.BLACK);
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
	
	private void drawBlocks(Graphics2D g2, double aDeltaTime, ChargeTime aCT)
	{
		int block = 30;
		
		// expired / used queue
		for ( Tuple<Car, Long, Long> lItem : getChargerSystem().getChargeQueueOLD() )
		{
			
		}
		
		// current / future queue
		for ( Tuple<Car, Long, Long> lItem : getChargerSystem().getChargeQueue() )
		{
			
		}
	}
	
	public ChargerSystem getChargerSystem()
	{
		return getCanvas().getSimulator().getChargerSystem();
	}
}

