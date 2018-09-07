package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import boot.GlobalVariables;

public class SideBarTabOptions extends SideBarTab
{
	private ActionListener al;
	
	private JCheckBox drawLoop;
	
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


		init();
		refresh();
	}
	
	protected void init()
	{
		int yOffset = 10;
		int yOffsetIncrement = 20;
		int width = getWidth()-10;
		int rightMargin = 10;
		int height = 20;

		
		drawLoop = new JCheckBox("drawLoop", GlobalVariables.drawLoop);
		drawLoop.setBounds(rightMargin, yOffset, width, height);
		drawLoop.addActionListener(al);

		yOffset += yOffsetIncrement;
		// next option
		
		
		add(drawLoop);		
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
