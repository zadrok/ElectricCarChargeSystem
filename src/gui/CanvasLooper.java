/*************************
 * CanvasLooper.java
 * 
 * 
 * 
 */

package gui;

import java.util.concurrent.atomic.AtomicBoolean;

import boot.GlobalVariables;

public class CanvasLooper implements Runnable
{
	private Canvas canvas;
	private long lastTime;
	private long time;
	private double deltaTime;
	private AtomicBoolean stop;
	
	public CanvasLooper(Canvas aCanvas)
	{
		canvas = aCanvas;
		lastTime = System.nanoTime();
		time = System.nanoTime();
		deltaTime = makeDeltaTime();
		stop = new AtomicBoolean(false);
	}
	
	public long makeDeltaTime()
	{
		return (time - lastTime) / 1000000;
	}
	
	public void run()
	{
		while( !stop.get() )
		{
			time = System.nanoTime();
			deltaTime = makeDeltaTime();
			lastTime = time;
			
			if ( GlobalVariables.drawLoop )
			{
				canvas.refresh();
				canvas.getSimulator().refreshSideBar();
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
	
	public void stop()
	{
		stop.set(true);
	}
	
	public void start()
	{
		stop.set(false);
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