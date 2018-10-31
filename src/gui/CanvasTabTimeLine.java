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
	
	private int xContent;
	private int yContent;
	private int wContent;
	private int hContent;
	
	private int xContentOffset;
	private int yContentOffset;
	
	private int offScreenWidth;
	private int offScreenHeight;
	
	private Rectangle contentDrawArea;
	
	
	public CanvasTabTimeLine(Canvas aCanvas)
	{
		super(aCanvas);
		
		margin = 20;
		xMargin = margin;
		yMargin = margin;
		widthIncrement = 30;
		heightIncrement = 30;
		block = 30;
		
		xContent = xMargin*2;
		yContent = yMargin;
		wContent = getWidth()-offScreenWidth;
		hContent = getHeight()-offScreenHeight;
		
		wContent = (int) Math.floor( widthIncrement * ( wContent / widthIncrement ) );
		hContent = (int) Math.floor( heightIncrement * ( hContent / heightIncrement ) );
		
		xContentOffset = 0;
		yContentOffset = 0;
		
		offScreenWidth = 60;
		offScreenHeight = 110;
		
		contentDrawArea = new Rectangle( xContent, yContent, wContent, hContent );
		
		addMouseListener( mouseListener() );
		addMouseMotionListener( mouseMotionListener() );
		addMouseWheelListener( mouseWheelListener() );
	}
	
	public void updateSize(int x, int y, int width, int height)
	{
		setBounds( x,y,width,height );
		wContent = getWidth()-offScreenWidth;
		hContent = getHeight()-offScreenHeight;
		wContent = (int) Math.floor( widthIncrement * ( wContent / widthIncrement ) );
		hContent = (int) Math.floor( heightIncrement * ( hContent / heightIncrement ) );
		contentDrawArea = new Rectangle( xContent, yContent, wContent, hContent );
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
		fillRect( g2, contentDrawArea, Color.LIGHT_GRAY );
	}
	
	private void drawGraphFront(Graphics2D g2, double aDeltaTime)
	{
		// draw horizontal lines
		int count = 0;
		int j = 0;
		for ( int i = 0; i < yContent+hContent - heightIncrement; i += heightIncrement )
		{
			Rectangle rect = new Rectangle( xContent, yContent-yContentOffset, wContent, 0 );
			rect.y += heightIncrement*j;
			
			if ( rect.y > yContent+hContent )
			{
				while ( rect.y > yContent+hContent )
					rect.y -= hContent;
			}
			else if ( rect.y < yContent )
			{
				while ( rect.y < yContent )
					rect.y += hContent;
			}
			
			drawLine( g2, rect, Color.BLACK );
			count++;
			j++;
		}
		
		// draw time on horizontal lines
		
		// draw horizontal lines text (time)
		//draw white back ground for text
		fillRect(g2, new Rectangle(0, 0, xContent, getHeight()), Color.WHITE);
		for ( int i = 0; i < count; i++ )
		{
			Rectangle rect = new Rectangle( xContent-35, yContent+5-yContentOffset, 0, 0 );
			rect.y += heightIncrement*i;
			
			int timesWrapped = 0;
			
			if ( rect.y > yContent+hContent )
			{
				while ( rect.y > yContent+hContent )
				{
					rect.y -= hContent;
					timesWrapped--;
				}
			}
			else if ( rect.y < yContent )
			{
				while ( rect.y < yContent )
				{
					rect.y += hContent;
					timesWrapped++;
				}
			}
			
			int numBlocks = count - i;
			ChargeTime ct = new ChargeTime( 0 );
			long[] times = ct.addMinutes( ( timesWrapped * count * block ) + ( block * i ) );
			
			String s = String.format("%02d:%02d", times[0], times[1]);
			drawString(g2, s, rect.x, rect.y, Color.BLACK);
		}
		
		// draw vertical lines
		j = 0;
		for ( int i = 0; i < getWidth() - widthIncrement - xMargin; i += widthIncrement )
		{
			Rectangle rect = new Rectangle( xContent+xContentOffset, yContent, 0, hContent );
			rect.x += widthIncrement*j;
			
			if ( rect.x > xContent+wContent )
			{
				while ( rect.x > xContent+wContent )
					rect.x -= wContent;
			}
			else if ( rect.x < xContent )
			{
				while ( rect.x < xContent )
					rect.x += wContent;
			}
			
			drawLine( g2, rect, Color.BLACK );
			j++;
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
			if ( lItem == null || aChargePoint == null )
			{
				continue;
			}
			else if ( lItem.getChargePoint() == aChargePoint )
			{
				tlBlock.add( lItem );
			}
		}
		
		// current / future queue
		for ( QueueItem lItem : getChargerSystem().getChargeQueue() )
		{
			if ( lItem == null || aChargePoint == null )
			{
				continue;
			}
			else if ( lItem.getChargePoint() == aChargePoint )
			{
				tlBlock.add( lItem );
			}
		}
		
		return tlBlock;
	}
	
	private void drawBlocks(Graphics2D g2, double aDeltaTime)
	{
		// a list for each chargePoint and all of the Queue items that go to it
		ArrayList<TimeLineBlock> tlBlocks = makeblocks();
		
		int xBlock = xContent;
		
		// draw each block as a row, each item is a column
		// order/time/length of time don't matter yet
		for ( int i = 0; i < tlBlocks.size(); i++ )
		{
			// current Time Line block
			TimeLineBlock tlBlock = tlBlocks.get(i);
			
			// draw blocks
			int h = 0;
			for ( int j = 0; j < tlBlock.items.size(); j++ )
			{
				// current item in block
				QueueItem item = tlBlock.items.get(j);

				if ( item == null )
					continue;
				
				if ( item.color == null )
					item.color = ColorIndex.getRandomColor();
				
				int minutes = 60;
				
				int numBlocks = (int) ( item.timeStart() / minutes / block ) ;
				int yItem = (int) ( yContent + ( numBlocks * heightIncrement ) ) - yContentOffset;
				int height = (int) ( heightIncrement * ( item.timeEnd() / minutes / block ) );
				h += height;
				
//				ChargeTime ct = new ChargeTime( item.timeStart() );
//				ct.print();
//				System.out.println( yItem );
				
				fillRect(g2, new Rectangle(xBlock+xContentOffset, yItem, widthIncrement, height), item.color);
			}
			
			// draw name of chargePoint at top of column
			String name = "";
			if ( tlBlock.chargePoint != null )
				name += String.format( "%1.0f", tlBlock.chargePoint.getChargeRate() );
			else
				name += "n";
			fillRect(g2, new Rectangle(xBlock+xContentOffset, yContent, widthIncrement, -heightIncrement), Color.WHITE);
			drawString(g2, name, xBlock+xContentOffset, yContent, Color.BLACK);
			
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
				int scrollAmount = 10;
				if ( e.isShiftDown() )
					scrollAmount *= 3;
				
				if ( e.isControlDown() )
				{
					if ( e.getWheelRotation() >= 1 )
					{
						xContentOffset -= scrollAmount;
					}
					else if ( e.getWheelRotation() <= -1 )
					{
						xContentOffset += scrollAmount;
					}
				}
				else
				{
					if ( e.getWheelRotation() >= 1 )
					{
						yContentOffset -= scrollAmount;
					}
					else if ( e.getWheelRotation() <= -1 )
					{
						yContentOffset += scrollAmount;
					}
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
			public void mouseClicked(MouseEvent e)
			{
				
				// click in block
				
				// a list for each chargePoint and all of the Queue items that go to it
				ArrayList<TimeLineBlock> tlBlocks = makeblocks();
				
				int xBlock = xContent;
				
				// draw each block as a row, each item is a column
				// order/time/length of time don't matter yet
				for ( int i = 0; i < tlBlocks.size(); i++ )
				{
					// current Time Line block
					TimeLineBlock tlBlock = tlBlocks.get(i);
					
					// draw blocks
					int h = 0;
					for ( int j = 0; j < tlBlock.items.size(); j++ )
					{
						// current item in block
						QueueItem item = tlBlock.items.get(j);
						
						if ( item == null )
							continue;
						
						int minutes = 60;
						
						int numBlocks = (int) ( item.timeStart() / minutes / block ) ;
						int yItem = (int) ( yContent + ( numBlocks * heightIncrement ) ) - yContentOffset;
						int height = (int) ( heightIncrement * ( item.timeEnd() / minutes / block ) );
						h += height;
						
						Rectangle rect = new Rectangle(xBlock+xContentOffset, yItem, widthIncrement, height);
						
						if ( rect.contains( e.getPoint() ) )
						{
							System.out.println( "QueueItem Car: " + item.getCar().getID() + ", ChargePoint: " + item.getChargePoint().getChargeRate() + ", start time: " + item.timeStart() + ", duration: " + item.timeEnd() );
						}
						
					}
					
					// go to next column
					xBlock += widthIncrement;
				}
				
				
			}
		};
	}
	
	private MouseMotionListener mouseMotionListener()
	{
		return new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {}
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

