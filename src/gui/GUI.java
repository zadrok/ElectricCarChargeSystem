/*************************
 * GUI.java
 * 
 * 
 * 
 */

package gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.*;

import boot.GlobalVariables;
import jade.wrapper.gateway.JadeGateway;
import model.*;

public class GUI 
{
	private JFrame frame;
	private String windowTitle;
	private int windowWidth;
	private int windowHeight;
	private Car selectedCar;
	private ChargePoint selectedChargePoint;
	private Scene scene;
	
	public GUI()
	{
		windowTitle = "Electric Car Charge System";
		windowWidth = 1280;
		windowHeight = 720;
		selectedCar = null;
		selectedChargePoint = null;
		
		frame = new JFrame(windowTitle);
		frame.setSize(windowWidth,windowHeight);
		frame.setBackground(ColorIndex.window);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    scene = new MainMenu( this, 0, 0, getWindowWidth(), getWindowHeight() );
	    frame.add(scene);
	    
	    frame.addWindowListener( customWindowAdapter() );
		
		frame.setLayout(null);
		frame.setVisible(true);
		
		frame.addComponentListener(new ComponentAdapter()
		{
		    public void componentResized(ComponentEvent componentEvent)
		    {
		    	windowHeight = frame.getHeight();
		    	windowWidth = frame.getWidth();
		    	scene.updateSize(0, 0, windowWidth, windowHeight);
		    }
		});
		
	}
	
	public int getWindowWidth()
	{
		return windowWidth;
	}
	
	public int getWindowHeight()
	{
		return windowHeight;
	}
	
	public void setJMenuBar(JMenuBar aMenuBar)
	{
		frame.setJMenuBar(aMenuBar);
	}
	
	public void removeJMenuBar()
	{
		frame.setJMenuBar(null);
	}
	
	public void runSimulator(File aConfigFile)
	{
		frame.remove( scene );
		scene.destroy();
		
		GlobalVariables.importSettings( aConfigFile );
		
		scene = new Simulator(this, 0, 0, windowWidth, windowHeight);
		frame.add(scene);
		
		refresh();
		
		((Simulator)scene).startDrawLoop();
	}
	
	public void runMainMenu()
	{
		frame.remove( scene );
		scene.destroy();
		
		scene = new MainMenu(this, 0, 0, windowWidth, windowHeight);
		frame.add(scene);
		
		refresh();
	}
	
	public void updateMenuBarClock()
	{
		if ( scene.getMenuBar() == null )
			return;
		
		ChargeTime ct = new ChargeTime(GlobalVariables.runTime);

		String time = String.format("DAY: %02d - TIME: %02d:%02d:%02d", ct.day, ct.hour, ct.minute, ct.second);
		
		scene.getMenuBar().clock.setText( time );
		scene.getMenuBar().clockreal.setText( "Cycle: " + ct.durationInMillis );
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
		if(aChargePoint.GetConnectedCar() != -1)
			setSelectedCar(aChargePoint.getCar());
	}
	
}
