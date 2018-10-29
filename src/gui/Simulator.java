package gui;

import model.*;

@SuppressWarnings("serial")
public class Simulator extends Scene
{
	private int sideBarWidth;
	private Canvas canvas;
	private SideBar sideBar;
	private DialogCreateCar dialogCreateCar;
	private DialogCreateChargePoint dialogCreateChargePoint;
	
	
	private ChargerSystem chargeSys;
	
	public Simulator(GUI aGUI, int x, int y, int width, int height)
	{
		super(aGUI, x, y, width, height);
		
		chargeSys = new ChargerSystem();
		
		menuBar = new MenuBar( getGUI() );
		add( menuBar );
		getGUI().setJMenuBar(menuBar);
		
		sideBarWidth = getGUI().getWindowWidth()/3;
		sideBar = new SideBar( this, 0, 0, sideBarWidth, height );
		canvas = new Canvas( this, sideBarWidth, 0, width-sideBarWidth, height );
		
		dialogCreateCar = new DialogCreateCar(this);
		dialogCreateChargePoint = new DialogCreateChargePoint(this);
		
		add( sideBar );
		add( canvas );
    	
		updateSize(x, y, width, height);
	}
	
	public void updateSize(int x, int y, int width, int height)
	{
		setBounds( x,y,width,height );
//		sideBarWidth = getGUI().getWindowWidth()/3;
		sideBar.updateSize( 0, 0, sideBarWidth, height );
        canvas.updateSize( sideBarWidth, 0, width-sideBarWidth, height );
	}
	
	public void destroy()
	{
		// if something needs to be done before changing scenes
		getGUI().removeJMenuBar();
		canvas.stopLooper();
	}
	
	public void showDialogCreateCar()
	{
		dialogCreateCar.setVisible(true);
	}
	
	public void showDialogCreateChargePoint()
	{
		dialogCreateChargePoint.setVisible(true);
	}
	
	public void startDrawLoop()
	{
		canvas.startLooper();
	}
	
	public void refreshSideBar()
	{
		sideBar.refresh();
	}
	
	public ChargerSystem getChargerSystem()
	{
		return chargeSys;
	}
	
}
