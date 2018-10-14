package gui;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class SideBarTabChargePoint extends SideBarTab
{
	private JLabel carIDTitle;
	private JLabel carIDInfo;
	
	private JLabel chargeRateIDTitle;
	private JLabel chargeRateIDInfo;
	
	private JLabel chargeTimeLeftTitle;
	private JLabel chargeTimeLeftInfo;
	
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

		yOffset += yOffsetIncrement;
		chargeTimeLeftTitle = new JLabel("Charge time remaininge:");
		chargeTimeLeftTitle.setBounds(rightMargin, yOffset, width, height);
		chargeTimeLeftInfo = new JLabel("###");
		chargeTimeLeftInfo.setBounds(rightMargin+xInfoOffset, yOffset, width, height);
		
		add(carIDTitle);
		add(carIDInfo);
		add(chargeRateIDTitle);
		add(chargeRateIDInfo);
		add(chargeTimeLeftTitle);
		add(chargeTimeLeftInfo);
		
	}
	
	public void refresh()
	{
		if ( getGUI().getSelectedChargePoint() != null )
		{
			carIDInfo.setText(String.format("%d", getGUI().getSelectedChargePoint().GetConnectedCar()));
			chargeRateIDInfo.setText(String.format("%.3f", getGUI().getSelectedChargePoint().getChargeRate()));
			if(getGUI().getSelectedChargePoint().GetConnectedCar() != -1)
				chargeTimeLeftInfo.setText(String.format("%d minutes", getGUI().getSelectedChargePoint().GetTimeRemaining()));
			else
				chargeTimeLeftInfo.setText("0");

		}
	}
	
}
