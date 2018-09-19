package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import boot.GlobalVariables;

public class SideBarTabOptions extends SideBarTab
{
	private ActionListener al;
	
	private JCheckBox drawLoop;
	private JButton createNewCarAgent;
	private ActionListener createNewCarAgentActionListener;
	
	public SideBarTabOptions(SideBar aSideBar)
	{
		super(aSideBar);
		
		al = new ActionListener()
		{
	        @Override
	        public void actionPerformed(ActionEvent e)
	        {
	            commit();
	        }
	    };

	    initActionListeners();
		init();
		refresh();
	}
	
	protected void init()
	{
		int yOffset = 10;
		int yOffsetIncrement = 20;
		int width = getWidth()-20;
		int rightMargin = 10;
		int height = 20;

		
		drawLoop = new JCheckBox("drawLoop", GlobalVariables.drawLoop);
		drawLoop.setBounds(rightMargin, yOffset, width, height);
		drawLoop.addActionListener(al);

		yOffset += yOffsetIncrement;
		createNewCarAgent = new JButton( "Create New Car Agent" );
		createNewCarAgent.setBounds(rightMargin, yOffset, width/2, height);
		createNewCarAgent.addActionListener( createNewCarAgentActionListener );
		
		
		add(drawLoop);
		add(createNewCarAgent);
	}
	
	private void initActionListeners()
	{
		createNewCarAgentActionListener = new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				getGUI().getChargerSystem().createCarAgent(1000, 50);
			}
		};
	}

	public void commit()
	{
		GlobalVariables.drawLoop = drawLoop.isSelected();
	}

	public void refresh()
	{
		drawLoop.setSelected( GlobalVariables.drawLoop );
	}
	
}
