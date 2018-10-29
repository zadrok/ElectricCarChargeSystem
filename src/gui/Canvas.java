/*************************
 * Canvas.java
 * 
 * 
 * 
 */

package gui;

import javax.swing.*;

@SuppressWarnings("serial")
public class Canvas extends JPanel
{
	private Simulator simulator;
	private CanvasLooper looper;
	private Thread looperThread;
	private JTabbedPane tabPane;
	
	private CanvasTabNodes tabNodes;
	private CanvasTabTimeLine tabTimeLine;
	private CanvasTabCarProfile tabCarProfile;
	
	public Canvas(Simulator aSimulator, int x, int y, int width, int height)
	{
		super();
		updateSize( x,y,width,height );
		setLayout(null);
		simulator = aSimulator;
		looper = new CanvasLooper(this);
		
		tabPane = new JTabbedPane();
		tabPane.setBounds(x, y, width, height);
		
		tabNodes = new CanvasTabNodes(this);
		tabTimeLine = new CanvasTabTimeLine(this);
		tabCarProfile = new CanvasTabCarProfile(this);
		
		tabPane.add("Nodes", tabNodes);
		tabPane.add("Time Line", tabTimeLine);
		tabPane.add("Car Profile", tabCarProfile);
		
		add( tabPane );
	}
	
	public void updateSize(int x, int y, int width, int height)
	{
		setBounds( x,y,width,height );
		
		if ( tabPane != null )
			tabPane.setBounds(0, 0, getWidth(), getHeight());
		
		if ( tabNodes != null )
			tabNodes.updateSize(x, y, width, height);
		
		if ( tabTimeLine != null )
			tabTimeLine.updateSize(x, y, width, height);
		
		if ( tabCarProfile != null )
			tabCarProfile.updateSize(x, y, width, height);
	}
	
	public void refresh()
	{
		validate();
		repaint();
		
		// update clock
		getGUI().updateMenuBarClock();
	}
	
	public CanvasLooper getLooper()
	{
		return looper;
	}
	
	public void startLooper()
	{
		looper.start();
		looperThread = new Thread(looper);
		looperThread.start();
	}
	
	public void stopLooper()
	{
		looper.stop();
	}
	
	public GUI getGUI()
	{
		return getSimulator().getGUI();
	}
	
	public Simulator getSimulator()
	{
		return simulator;
	}
	
}
