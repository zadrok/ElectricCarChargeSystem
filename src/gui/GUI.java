package gui;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class GUI 
{
	private JFrame frame;
	private String windowTitle;
	private int windowWidth;
	private int windowHegiht;
	private Color windowColor;
	
	private MenuBar menuBar;
	
	public GUI()
	{
		windowTitle = "Electric Car Charge System";
		windowWidth = 1280;
		windowHegiht = 720;
		windowColor = Color.WHITE;
		
		frame = new JFrame(windowTitle);
		frame.setSize(windowWidth,windowHegiht);
		frame.setBackground(windowColor);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    frame.addWindowListener( customWindowAdapter() );
	    
		menuBar = new MenuBar(this);
		frame.add( menuBar );
		frame.setJMenuBar(menuBar);
		
		frame.setLayout(null);
		frame.setVisible(true);
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
		System.exit(0);
	}
	
}