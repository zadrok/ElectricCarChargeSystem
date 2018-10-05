package gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import jade.wrapper.gateway.JadeGateway;
import model.*;

public class GUI 
{
	private JFrame frame;
	private String windowTitle;
	private int windowWidth;
	private int windowHegiht;
	private MenuBar menuBar;
	private ChargerSystem chargeSys;
	private Canvas canvas;
	private SideBar sideBar;
	private Car selectedCar;
	private ChargePoint selectedChargePoint;
	private DialogCreateCar dialogCreateCar;
	private DialogCreateChargePoint dialogCreateChargePoint;
	
	public GUI( ChargerSystem aChargeSystem )
	{
		chargeSys = aChargeSystem;
		
		windowTitle = "Electric Car Charge System";
		windowWidth = 1280;
		windowHegiht = 720;
		selectedCar = null;
		selectedChargePoint = null;
		
		frame = new JFrame(windowTitle);
		frame.setSize(windowWidth,windowHegiht);
		frame.setBackground(ColorIndex.window);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    frame.addWindowListener( customWindowAdapter() );
	    
		menuBar = new MenuBar(this);
		frame.add( menuBar );
		frame.setJMenuBar(menuBar);
		
		int sideBarWidth = windowWidth/3;
		sideBar = new SideBar( this, 0, 0, sideBarWidth, windowHegiht );
		canvas = new Canvas( this, sideBarWidth, 0, windowWidth-sideBarWidth, windowHegiht );
		
		frame.add( sideBar );
		frame.add( canvas );
		
		dialogCreateCar = new DialogCreateCar(this);
		dialogCreateChargePoint = new DialogCreateChargePoint(this);
		
		frame.setLayout(null);
		frame.setVisible(true);
		
		frame.addComponentListener(new ComponentAdapter()
		{
		    public void componentResized(ComponentEvent componentEvent)
		    {
		    	windowHegiht = frame.getHeight();
		    	windowWidth = frame.getWidth();
		    	sideBar.updateSize( 0, 0, sideBarWidth, windowHegiht );
		        canvas.updateSize( sideBarWidth, 0, windowWidth-sideBarWidth, windowHegiht );
		    }
		});
		
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
	
	public void refresh()
	{
		frame.validate();
		frame.repaint();
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
	private WindowAdapter customWindowAdapter()
	{
		return new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e) 
			{
				exit();
			}
		};
	}
	
	public void exit()
	{
		frame.dispose();
		JadeGateway.shutdown();
		System.exit(0);
	}
	
	public ChargerSystem getChargerSystem()
	{
		return chargeSys;
	}
	
	public Car getSelectedCar()
	{
		return selectedCar;
	}
	
	public void setSelectedCar(Car aCar)
	{
		selectedCar = aCar;
	}
	
	public ChargePoint getSelectedChargePoint()
	{
		return selectedChargePoint;
	}
	
	public void setSelectedChargePoint(ChargePoint aChargePoint)
	{
		selectedChargePoint = aChargePoint;
	}
	
}
