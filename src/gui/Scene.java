package gui;

import javax.swing.*;

@SuppressWarnings("serial")
public class Scene extends JPanel
{
	protected GUI gui;
	protected MenuBar menuBar;
	
	public Scene(GUI aGUI, int x, int y, int width, int height)
	{
		super();
		setLayout(null);
		gui = aGUI;
		setBounds( x, y, width, height );
	}
	
	public void updateSize(int x, int y, int width, int height)
	{
		setBounds( x,y,width,height );
	}
	
	public void destroy()
	{
		// if something needs to be done before changing scenes
		// like removing the menu bar
	}
	
	public void refresh()
	{
		validate();
		repaint();
	}
	
	public GUI getGUI()
	{
		return gui;
	}
	
	public MenuBar getMenuBar()
	{
		return menuBar;
	}
}
