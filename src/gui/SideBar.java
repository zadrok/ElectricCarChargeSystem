package gui;

import java.awt.*;
import javax.swing.*;

public class SideBar extends JPanel
{
	private GUI gui;
	private JTabbedPane tabPane;
	
	private SideBarTabOptions tabOptions;	
	private SideBarTabMasterScheduler tabMasSch;
	private SideBarTabCar tabCar;
	
	public SideBar(GUI aGUI, int x, int y, int width, int height)
	{
		super();
		setBounds( x,y,width,height );
		setLayout(null);
		gui = aGUI;
		
		tabPane = new JTabbedPane();
		tabPane.setBounds(0, 0, getWidth(), getHeight());
		
		tabOptions = new SideBarTabOptions(this);
		tabMasSch = new SideBarTabMasterScheduler(this);
		tabCar = new SideBarTabCar(this);
		
		tabPane.add("Options", tabOptions);
		tabPane.add("Master Scheduler", tabMasSch);
		tabPane.add("Car", tabCar);
		
		add( tabPane );
	}
	
	public void paintComponent(Graphics g)
    {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		
    }
	
	public void refresh()
	{
		tabOptions.refresh();
		tabMasSch.refresh();
		tabCar.refresh();
		
	}
	
	public GUI getGUI()
	{
		return gui;
	}
	
}
