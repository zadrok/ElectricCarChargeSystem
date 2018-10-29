/*************************
 * DialogCreateCar.java
 * 
 * 
 * 
 */

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class DialogCreateCar extends Dialog
{
	private JButton confirmBttn;
	
	private JLabel maxChargeTitle;
	private JTextField maxChargeInfo;
	
	private JLabel currentChargeTitle;
	private JTextField currentChargeInfo;
	
	public DialogCreateCar(Simulator aSimulator)
	{
		super(aSimulator, "Create Car", false);
		setSize(360,250);
		
		yOffset += yOffsetIncrement;
		maxChargeTitle = new JLabel("Battery Size:");
		maxChargeTitle.setBounds(rightMargin, yOffset, width, height);
		maxChargeInfo = new JTextField("", 15);
		maxChargeInfo.setBounds(rightMargin+xInfoOffset, yOffset, width, height);
		
		yOffset += yOffsetIncrement;
		currentChargeTitle = new JLabel("Current Charge:");
		currentChargeTitle.setBounds(rightMargin, yOffset, width, height);
		currentChargeInfo = new JTextField("", 15);
		currentChargeInfo.setBounds(rightMargin+xInfoOffset, yOffset, width, height);
		
		yOffset += yOffsetIncrement;
		yOffset += yOffsetIncrement;
		confirmBttn = new JButton("Confirm");
		confirmBttn.addActionListener( makeCommitBttnListener() );
		confirmBttn.setBounds(rightMargin, yOffset, width+xInfoOffset, height*2);
		
		add(confirmBttn);
		add(maxChargeTitle);
		add(maxChargeInfo);
		add(currentChargeTitle);
		add(currentChargeInfo);
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
					long maxCharge = Long.parseLong( maxChargeInfo.getText() );
					long currentCharge = Long.parseLong( currentChargeInfo.getText() );
					
					getChargerSystem().createCarAgent(maxCharge, currentCharge);
					
					setVisible(false);
					
					maxChargeInfo.setText( "" );
					currentChargeInfo.setText( "" );
				} 
				catch (Exception e2) 
				{
					System.out.println("Wrong information");
				}
			}
		};
	}
}
