/*************************
 * SideBarTabMasterScheduler.java
 * 
 * 
 * 
 */

package gui;

import javax.swing.*;

@SuppressWarnings("serial")
public class SideBarTabMasterScheduler extends SideBarTab
{
	private JLabel Title;
	private JLabel Info;
	
	public SideBarTabMasterScheduler(SideBar aSideBar)
	{
		super(aSideBar);

		yOffset += yOffsetIncrement;
		Title = new JLabel("###");
		Title.setBounds(rightMargin, yOffset, width, height);
		Info = new JLabel("###");
		Info.setBounds(rightMargin+xInfoOffset, yOffset, width, height);
		
		add(Title);
		add(Info);
	}
	
	public void refresh()
	{
		
	}
	
}
