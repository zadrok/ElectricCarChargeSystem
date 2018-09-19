package gui;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import boot.GlobalVariables;
import jade.wrapper.gateway.JadeGateway;
import model.*;

public class GUI 
{
	private JFrame frame;
	private String windowTitle;
	private int windowWidth;
	private int windowHegiht;
	private Color windowColor;
	
	private MenuBar menuBar;
	
	private ChargerSystem chargeSys;
	
	private Canvas canvas;
	private SideBar sideBar;
	
	private Car selectedCar;
	
	public GUI( ChargerSystem aChargeSystem )
	{
		chargeSys = aChargeSystem;
		
		windowTitle = "Electric Car Charge System";
		windowWidth = 1280;
		windowHegiht = 720;
		windowColor = Color.WHITE;
		selectedCar = null;
		
		frame = new JFrame(windowTitle);
		frame.setSize(windowWidth,windowHegiht);
		frame.setBackground(windowColor);
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
		
		
		frame.setLayout(null);
		frame.setVisible(true);
	}
	
	public void startDrawLoop()
	{
		canvas.startLooper();
	}
	
	public void refresh()
	{
		frame.validate();
		frame.repaint();
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
	
}
