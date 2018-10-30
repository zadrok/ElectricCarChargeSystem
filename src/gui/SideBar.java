/*************************
 * SideBar.java
 * 
 * 
 * 
 */

package gui;

import java.awt.*;
import javax.swing.*;

import model.ChargerSystem;

@SuppressWarnings("serial")
public class SideBar extends JPanel
{
	private Simulator simulator;
	private JTabbedPane tabPane;
	
	private SideBarTabOptions tabOptions;	
	private SideBarTabMasterScheduler tabMasSch;
	private SideBarTabCar tabCar;
	private SideBarTabChargePoint tabChargePoint;
	private SideBarTabKey tabKey;
	private SideBarTabCarList tabCarList;
	
	public SideBar(Simulator aSimulator, int x, int y, int width, int height)
	{
		super();
		updateSize( x,y,width,height );
		setLayout(null);
		simulator = aSimulator;
		
		tabPane = new JTabbedPane();
		tabPane.setBounds(0, 0, getWidth(), getHeight());
		
		tabOptions = new SideBarTabOptions(this);
		tabMasSch = new SideBarTabMasterScheduler(this);
		tabCar = new SideBarTabCar(this);
		tabChargePoint = new SideBarTabChargePoint(this);
		tabCarList = new SideBarTabCarList(this);
		tabKey = new SideBarTabKey(this);
		
		tabPane.add("Options", tabOptions);
		tabPane.add("Master Scheduler", tabMasSch);
		tabPane.add("Car", tabCar);
		tabPane.add("Charge Point", tabChargePoint);
		tabPane.add("Car List", tabCarList);		
		tabPane.add("Key", tabKey);
		
		add( tabPane );
	}
	
	public void updateSize(int x, int y, int width, int height)
	{
		setBounds( x,y,width,height );
		if ( tabPane != null )
			tabPane.setBounds(0, 0, getWidth(), getHeight());
		
		if ( tabCarList != null )
			tabCarList.updateSize(0, 0, getWidth(), getHeight());
	}
	
	public void paintComponent(Graphics g)
    {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor( ColorIndex.sideBarFill );
		g2.fillRect(0, 0, getWidth(), getHeight());
    }
	
	public void refresh()
	{
		tabOptions.refresh();
		tabMasSch.refresh();
		tabCar.refresh();
		tabChargePoint.refresh();
	}
	
	public void refreshCarList()
	{
		tabCarList.refresh();
	}
	
	public GUI getGUI()
	{
		return simulator.getGUI();
	}

	public Simulator getSimulator()
	{
		return simulator;
	}

	public ChargerSystem getChargerSystem()
	{
		return getSimulator().getChargerSystem();
	}
	
}
