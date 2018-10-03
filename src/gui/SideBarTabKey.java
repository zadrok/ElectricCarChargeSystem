package gui;

import java.util.ArrayList;

import javax.swing.JLabel;

public class SideBarTabKey extends SideBarTab
{

	public SideBarTabKey(SideBar aSideBar)
	{
		super(aSideBar);
		ArrayList<String> data = new ArrayList<>();
		
		data.add( "Outer ring:" );
		data.add( "Black spinning: car agent is cycling (running)" );
		data.add( "Black not spinning: canvas told to not update" );
		data.add( "Red: car agent is not cycling (stopped)" );
		
		data.add( "" );
		
		data.add( "Inner circle:" );
		data.add( "Green: car agent is charging" );
		data.add( "Orange: car agent wants to charge" );
		data.add( "Yellow: car agent is ideling" );
		data.add( "Red: something is wrong with car agent (no state or other)" );
		
		data.add( "" );
		
		data.add( "Center bar:" );
		data.add( "current charge of car" );
		
		for ( String str : data )
		{
			yOffset += yOffsetIncrement;
			JLabel info = new JLabel( str );
			info.setBounds(rightMargin, yOffset, width, height);
			add( info );
		}
	}
	
	public void refresh()
	{
		
	}
	
	
}
