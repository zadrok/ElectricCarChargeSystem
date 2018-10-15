package gui;

import javax.swing.*;

@SuppressWarnings("serial")
public class SideBarTabCar extends SideBarTab
{
	private JLabel idTitle;
	private JLabel idInfo;
	
	private JLabel maxChargeCapacityTitle;
	private JLabel maxChargeCapacityInfo;
	
	private JLabel currentChargeTitle;
	private JLabel currentChargeInfo;
	
	private JLabel carStateTitle;
	private JLabel carStateInfo;
	
	public SideBarTabCar(SideBar aSideBar)
	{
		super(aSideBar);

		yOffset += yOffsetIncrement;
		idTitle = new JLabel("ID:");
		idTitle.setBounds(rightMargin, yOffset, width, height);
		idInfo = new JLabel("###");
		idInfo.setBounds(rightMargin+xInfoOffset, yOffset, width, height);

		yOffset += yOffsetIncrement;
		maxChargeCapacityTitle = new JLabel("maxChargeCapacity");
		maxChargeCapacityTitle.setBounds(rightMargin, yOffset, width, height);
		maxChargeCapacityInfo = new JLabel("###");
		maxChargeCapacityInfo.setBounds(rightMargin+xInfoOffset, yOffset, width, height);

		yOffset += yOffsetIncrement;
		currentChargeTitle = new JLabel("currentCharge:");
		currentChargeTitle.setBounds(rightMargin, yOffset, width, height);
		currentChargeInfo = new JLabel("###");
		currentChargeInfo.setBounds(rightMargin+xInfoOffset, yOffset, width, height);

		yOffset += yOffsetIncrement;
		carStateTitle = new JLabel("carState:");
		carStateTitle.setBounds(rightMargin, yOffset, width, height);
		carStateInfo = new JLabel("###");
		carStateInfo.setBounds(rightMargin+xInfoOffset, yOffset, width, height);
		
		add(idTitle);
		add(idInfo);
		add(maxChargeCapacityTitle);
		add(maxChargeCapacityInfo);
		add(currentChargeTitle);
		add(currentChargeInfo);
		add(carStateTitle);
		add(carStateInfo);
		
	}
	
	public void refresh()
	{
		if ( getGUI().getSelectedCar() != null )
		{
			idInfo.setText("" + getGUI().getSelectedCar().getID());
			maxChargeCapacityInfo.setText(String.format("%.3f", getGUI().getSelectedCar().getMaxChargeCapacity()));
			currentChargeInfo.setText(String.format("%.3f", getGUI().getSelectedCar().getCurrentCharge()));
			carStateInfo.setText("" + getGUI().getSelectedCar().getCarStateString() );

		}
	}
	
}
