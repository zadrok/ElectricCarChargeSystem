package gui;

import javax.swing.JLabel;

public class SideBarTabChargePoint extends SideBarTab
{
	private JLabel carIDTitle;
	private JLabel carIDInfo;
	
	private JLabel chargeRateIDTitle;
	private JLabel chargeRateIDInfo;
	
	public SideBarTabChargePoint(SideBar aSideBar)
	{
		super(aSideBar);

		yOffset += yOffsetIncrement;
		carIDTitle = new JLabel("Car ID:");
		carIDTitle.setBounds(rightMargin, yOffset, width, height);
		carIDInfo = new JLabel("###");
		carIDInfo.setBounds(rightMargin+xInfoOffset, yOffset, width, height);

		yOffset += yOffsetIncrement;
		chargeRateIDTitle = new JLabel("Charge rate:");
		chargeRateIDTitle.setBounds(rightMargin, yOffset, width, height);
		chargeRateIDInfo = new JLabel("###");
		chargeRateIDInfo.setBounds(rightMargin+xInfoOffset, yOffset, width, height);
		
		add(carIDTitle);
		add(carIDInfo);
		add(chargeRateIDTitle);
		add(chargeRateIDInfo);
		
	}
	
	public void refresh()
	{
		if ( getGUI().getSelectedChargePoint() != null )
		{
			carIDInfo.setText("" + getGUI().getSelectedChargePoint().GetConnectedCar());
			chargeRateIDInfo.setText("" + getGUI().getSelectedChargePoint().getChargeRate());

		}
	}
	
}
