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
	private GUI gui;
	private CanvasLooper looper;
	private JTabbedPane tabPane;
	
	private CanvasTabNodes tabNodes;
	private CanvasTabTimeLine tabTimeLine;
	
	public Canvas(GUI aGUI, int x, int y, int width, int height)
	{
		super();
		updateSize( x,y,width,height );
		setLayout(null);
		gui = aGUI;
		looper = new CanvasLooper(this);
		
		tabPane = new JTabbedPane();
		tabPane.setBounds(x, y, width, height);
		
		tabNodes = new CanvasTabNodes(this);
		tabTimeLine = new CanvasTabTimeLine(this);
		
		tabPane.add("Nodes", tabNodes);
		tabPane.add("Time Line", tabTimeLine);
		
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
		looper.run();
	}
	
	public GUI getGUI()
	{
		return gui;
	}
	
}
