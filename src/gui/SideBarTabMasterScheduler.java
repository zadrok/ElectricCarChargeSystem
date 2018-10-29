/*************************
 * SideBarTabMasterScheduler.java
 * 
 * 
 * 
 */

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import scheduleAlgorithm.ScheduleAlgorithm;

@SuppressWarnings("serial")
public class SideBarTabMasterScheduler extends SideBarTab
{
	private JLabel Title;
	private JLabel Info;
	
	private JLabel algorithmComboBoxTitle;
	private JComboBox<ScheduleAlgorithm> algorithmComboBox;
	private DefaultComboBoxModel<ScheduleAlgorithm> algorithms;
	private JScrollPane algorithmScrollPane;
	
	public SideBarTabMasterScheduler(SideBar aSideBar)
	{
		super(aSideBar);
		xInfoOffset = getSideBar().getWidth()/3;
		
		yOffset += yOffsetIncrement;
		Title = new JLabel("###");
		Title.setBounds(rightMargin, yOffset, width, height);
		Info = new JLabel("###");
		Info.setBounds(rightMargin+xInfoOffset, yOffset, width, height);
		
		
		yOffset += yOffsetIncrement;
		algorithmComboBoxTitle = new JLabel("Algorithm");
		algorithmComboBoxTitle.setBounds(rightMargin, yOffset, width, height*2);
		algorithms = new DefaultComboBoxModel<ScheduleAlgorithm>();
		for ( ScheduleAlgorithm sa : getChargerSystem().getMasterScheduler().getAllScheduleAlgorithms() )
			algorithms.addElement( sa );
		algorithmComboBox = new JComboBox<ScheduleAlgorithm>(algorithms);
		algorithmComboBox.setSelectedIndex( getChargerSystem().getMasterScheduler().getAllScheduleAlgorithms().indexOf( getChargerSystem().getMasterScheduler().getScheduleAlgorithm() ) );
		algorithmScrollPane = new JScrollPane( algorithmComboBox );
		algorithmScrollPane.setBounds(rightMargin+xInfoOffset, yOffset, width-xInfoOffset-rightMargin*2, height*2);
		algorithmComboBox.addActionListener( algorithmComboBoxListener() );
		yOffset += yOffsetIncrement;
		
		
		yOffset += yOffsetIncrement;
		// more stuff
		
		
		add(Title);
		add(Info);
		add(algorithmComboBoxTitle);
		add(algorithmScrollPane);
	}
	
	public void refresh()
	{
		
	}
	
	public ActionListener algorithmComboBoxListener()
	{
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if ( algorithmComboBox.getSelectedItem() != getChargerSystem().getMasterScheduler().getScheduleAlgorithm() && algorithmComboBox.getSelectedItem() instanceof ScheduleAlgorithm )
					getChargerSystem().getMasterScheduler().setScheduleAlgorithm( (ScheduleAlgorithm) algorithmComboBox.getSelectedItem() );
			}
		};
	}
	
}
