package gui;

import java.awt.*;
import javax.swing.*;
import boot.*;

public class Canvas extends JPanel
{
	private GUI gui;
	private CanvasLooper looper;
	
	private int startAngle = 0;
	private int arcAngle = 70;
	
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
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		g2.setColor( Color.BLACK );
		g2.setStroke( new BasicStroke( 10 ) );
		g2.drawArc(100, 100, 100, 100, startAngle, arcAngle);
		g2.drawArc(100, 100, 100, 100, startAngle+90, arcAngle);
		g2.drawArc(100, 100, 100, 100, startAngle+180, arcAngle);
		g2.drawArc(100, 100, 100, 100, startAngle+270, arcAngle);
		startAngle++;
		if ( startAngle == 360 )
			startAngle = 0;
		
    }
	
	public void startLooper()
	{
		looper.run();
	}
	
	public GUI getGUI()
	{
		return gui;
	}
	
	
	private class CanvasLooper implements Runnable
	{
		private Canvas canvas;
		
		public CanvasLooper(Canvas aCanvas)
		{
			canvas = aCanvas;
		}
		
		public void run()
		{
			while( true )
			{
				
				if ( GlobalVariables.drawLoop )
				{
					canvas.refresh();
				}
				
				// Sleep for given period of time
				try 
				{
					Thread.sleep( 1000 / GlobalVariables.drawFrameRate );
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
}
