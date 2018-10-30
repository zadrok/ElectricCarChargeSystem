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
import java.util.ArrayList;

import boot.GlobalVariables;
import model.*;

@SuppressWarnings("serial")
public class CanvasTabTimeLine extends CanvasTab
{
	// offset from zero, for scrolling
	private double offsetVal;
	// general offset;
	private int margin;
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
	
	private int x;
	private int y;
	
	private int offScreenWidth;
	private int offScreenHeight;
	
	
	public CanvasTabTimeLine(Canvas aCanvas)
	{
		super(aCanvas);
		
		offsetVal = 0;
		margin = 20;
		xMargin = margin;
		yMargin = margin;
		widthIncrement = 30;
		heightIncrement = 30;
		block = 30;
		
		x = xMargin*2;
		y = yMargin;
		offScreenWidth = 35;
		offScreenHeight = 80;
		
		addMouseListener( mouseListener() );
		addMouseMotionListener( mouseMotionListener() );
		addMouseWheelListener( mouseWheelListener() );
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
		
		drawGraphBack(g2,deltaTime);
		drawBlocks(g2,deltaTime);
		drawGraphFront(g2,deltaTime);
    }
	
	private void drawGraphBack(Graphics2D g2, double aDeltaTime)
	{
		// timeLine draw area
		fillRect( g2, new Rectangle( x, y, getWidth()-offScreenWidth, getHeight()-offScreenHeight ), Color.LIGHT_GRAY );
	}
	
	private void drawGraphFront(Graphics2D g2, double aDeltaTime)
	{
		// draw horizontal lines
		int count = 0;
		Rectangle rect = new Rectangle( x, y, getWidth()-offScreenWidth, 0 );
		for ( int i = 0; i < getHeight() - heightIncrement - yMargin; i += heightIncrement )
		{
			drawLine( g2, rect, Color.BLACK );
			rect.y += heightIncrement;
			count++;
		}
		
		// draw time on horizontal lines
		// make this number the center value
		int centerLineNum = count/4;
		
		// draw horizontal lines test (time)
		rect = new Rectangle( x-35, y+5, 0, 0 );
		for ( int i = 0; i < count; i++ )
		{
			int numBlocks = count - ( centerLineNum - i );
			ChargeTime ct = new ChargeTime(0);
			long[] times = ct.addMinutes(block*(i-centerLineNum));
			
			String s = String.format("%02d:%02d", times[0], times[1]);
			drawString(g2, s, rect.x, rect.y, Color.BLACK);
			rect.y += heightIncrement;
		}
		
		// draw vertical lines
		rect = new Rectangle( x, y, 0, getHeight()-offScreenHeight );
		for ( int i = 0; i < getWidth() - widthIncrement - xMargin; i += widthIncrement )
		{
			drawLine( g2, rect, Color.BLACK );
			rect.x += widthIncrement;
		}
	}
	
	private ArrayList<TimeLineBlock> makeblocks()
	{
		ArrayList<TimeLineBlock> tlBlocks = new ArrayList<>();
		
		// for each ChargePoint find all queue items for it
		for ( ChargePoint chrPnt : getChargerSystem().getChargePoints() )
		{
			TimeLineBlock tlBlock = findItems( chrPnt );
			
			// if any items were added to the block then add it to list
			if ( tlBlock.items.size() > 0 )
				tlBlocks.add( tlBlock );
		}
				
		return tlBlocks;
	}
	
	private TimeLineBlock findItems(ChargePoint aChargePoint)
	{
		TimeLineBlock tlBlock = new TimeLineBlock( aChargePoint );
		
		// expired / used queue
		for ( QueueItem lItem : getChargerSystem().getChargeQueueOLD() )
		{
			if ( lItem.getChargePoint() == aChargePoint )
				tlBlock.add( lItem );
		}
		
		// current / future queue
		for ( QueueItem lItem : getChargerSystem().getChargeQueue() )
		{
			if ( lItem.getChargePoint() == aChargePoint )
				tlBlock.add( lItem );
		}
		
		return tlBlock;
	}
	
	private void drawBlocks(Graphics2D g2, double aDeltaTime)
	{
		// a list for each chargePoint and all of the Queue items that go to it
		ArrayList<TimeLineBlock> tlBlocks = makeblocks();
		// catch all for any item that didn't go to a chargePoint
		tlBlocks.add( findItems(null) );
		
		int xBlock = x;
		int yBlock = y;
		
		// draw each block as a row, each item is a column
		// order/time/length of time don't matter yet
		for ( int i = 0; i < tlBlocks.size(); i++ )
		{
			// current Time Line block
			TimeLineBlock tlBlock = tlBlocks.get(i);
			
			// draw name of chargePoint at top of column
			String name = "";
			if ( tlBlock.chargePoint != null )
				name += tlBlock.chargePoint.getChargeRate();
			else
				name += "null";
			drawString(g2, name, xBlock+(widthIncrement/4), yBlock, Color.BLACK);
			
			// draw blocks
			for ( int j = 0; j < tlBlock.items.size(); j++ )
			{
				// current item in block
				QueueItem item = tlBlock.items.get(j);
				
				int yItem = (int) (yBlock + item.timeStart());
				
//				ChargeTime ct = new ChargeTime( item.timeStart() );
//				ct.print();
//				System.out.println( yItem );
				
				fillRect(g2, new Rectangle(xBlock, yItem, widthIncrement, heightIncrement), Color.BLUE);
			}
			
			// go to next column
			xBlock += widthIncrement;
		}
		
	}
	
	public ChargerSystem getChargerSystem()
	{
		return getCanvas().getSimulator().getChargerSystem();
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
}

class TimeLineBlock
{
	public ChargePoint chargePoint;
	public ArrayList<QueueItem> items;
	
	public TimeLineBlock(ChargePoint aPoint)
	{
		chargePoint = aPoint;
		items = new ArrayList<>();
	}
	
	public void add(QueueItem aItem)
	{
		items.add( aItem );
	}
}

