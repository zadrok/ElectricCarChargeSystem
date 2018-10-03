package gui;

import javax.swing.JPanel;

public class SideBarTab extends JPanel
{
	protected SideBar sideBar;
	
	protected int xInfoOffset;
	protected int yOffset;
	protected int yOffsetIncrement;
	protected int width;
	protected int rightMargin;
	protected int height;
	
	public SideBarTab(SideBar aSideBar)
	{
		super();
		sideBar = aSideBar;
		setLayout(null);
		setBounds(0, 0, sideBar.getWidth(), sideBar.getHeight());
		
		xInfoOffset = getSideBar().getWidth()/2;
		yOffset = 0;
		yOffsetIncrement = 20;
		width = getSideBar().getWidth();
		rightMargin = 20;
		height = 20;
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
