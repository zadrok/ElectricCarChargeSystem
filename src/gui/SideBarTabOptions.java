package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import boot.GlobalVariables;

public class SideBarTabOptions extends SideBarTab
{
	private ActionListener al;
	
	private JCheckBox draw;
	
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

		
		draw = new JCheckBox("draw", GlobalVariables.draw);
		draw.setBounds(rightMargin, yOffset, width, height);
		draw.addActionListener(al);

		yOffset += yOffsetIncrement;
		// next option
		
		
		add(draw);		
	}

	public void commit()
	{
		GlobalVariables.draw = draw.isSelected();
	}

	public void refresh()
	{
		draw.setSelected( GlobalVariables.draw );
	}
	
}
