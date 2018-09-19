package gui;

import javax.swing.JPanel;

public class SideBarTab extends JPanel
{
	protected SideBar sideBar;
	
	public SideBarTab(SideBar aSideBar)
	{
		super();
		sideBar = aSideBar;
		setLayout(null);
		setBounds(0, 0, sideBar.getWidth(), sideBar.getHeight());
	}
	
	protected void init()
	{
				
	}

	public void commit()
	{
		
	}

	public void refresh()
	{
		
	}
	
	public SideBar getSideBar()
	{
		return sideBar;
	}
	
	public GUI getGUI()
	{
		return getSideBar().getGUI();
	}
}
