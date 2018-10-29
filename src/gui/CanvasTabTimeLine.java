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
	private double topVal;
	
	public CanvasTabTimeLine(Canvas aCanvas)
	{
		super(aCanvas);
		
		topVal = 0;
		
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
					topVal--;
				}
				else if ( e.getWheelRotation() <= -1 )
				{
					topVal++;
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
				topVal++;
			}
		};
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
		ChargeTime ct = new ChargeTime(GlobalVariables.runTime);
		// block time
		int block = 30;
		
		int centerLineNum = count/2;
		
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
			long[] times = ct.addMinutes(block*(i-centerLineNum));
			
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
	
	public ChargerSystem getChargerSystem()
	{
		return getCanvas().getSimulator().getChargerSystem();
	}
	
	private void drawBlocks(Graphics2D g2, double aDeltaTime)
	{
		// expired / used queue
		for ( Tuple<Car, Long, Long> lItem : getChargerSystem().getChargeQueueOLD() )
		{
			
		}
		
		// current / future queue
		for ( Tuple<Car, Long, Long> lItem : getChargerSystem().getChargeQueue() )
		{
			
		}
	}
}

