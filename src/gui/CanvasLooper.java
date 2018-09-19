package gui;

import boot.GlobalVariables;

public class CanvasLooper implements Runnable
{
	private Canvas canvas;
	private long lastTime;
	private long time;
	private double deltaTime;
	
	public CanvasLooper(Canvas aCanvas)
	{
		canvas = aCanvas;
		lastTime = System.nanoTime();
		time = System.nanoTime();
		deltaTime = (time - lastTime) / 1000000;
	}
	
	public void run()
	{
		while( true )
		{
			time = System.nanoTime();
			deltaTime = (time - lastTime) / 1000000;
			lastTime = time;
			
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
	
	public long getLastTime()
	{
		return lastTime;
	}
	
	public long getTime()
	{
		return time;
	}
	
	public double getDeltaTime()
	{
		return deltaTime;
	}
}