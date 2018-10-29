/*************************
 * DialogCreateChargePoint.java
 * 
 * 
 * 
 */

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class DialogCreateChargePoint extends Dialog
{
	public JButton confirmBttn;
	
	public JLabel chargeRateTitle;
	public JTextField chargeRateInfo;
	
	public DialogCreateChargePoint(Simulator aSimulator)
	{
		super(aSimulator, "Create Charge Point", false);
		setSize(360,200);
		
		yOffset += yOffsetIncrement;
		chargeRateTitle = new JLabel("Charge rate:");
		chargeRateTitle.setBounds(rightMargin, yOffset, width, height);
		chargeRateInfo = new JTextField("", 15);
		chargeRateInfo.setBounds(rightMargin+xInfoOffset, yOffset, width, height);
		
		yOffset += yOffsetIncrement;
		yOffset += yOffsetIncrement;
		confirmBttn = new JButton("Confirm");
		confirmBttn.addActionListener( makeCommitBttnListener() );
		confirmBttn.setBounds(rightMargin, yOffset, width+xInfoOffset, height*2);
		
		add(confirmBttn);
		add(chargeRateTitle);
		add(chargeRateInfo);
	}
	
	private ActionListener makeCommitBttnListener()
	{
		return new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					int chargeRate = Integer.parseInt( chargeRateInfo.getText() );
					
					getChargerSystem().createChargePoint(chargeRate);
					
					setVisible(false);
					
					chargeRateInfo.setText( "" );
				} 
				catch (Exception e2) 
				{
					System.out.println("Wrong information");
				}
			}
		};
	}
}
