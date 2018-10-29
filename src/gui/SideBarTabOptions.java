/*************************
 * SideBarTabOptions.java
 * 
 * 
 * 
 */

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import boot.GlobalVariables;

@SuppressWarnings("serial")
public class SideBarTabOptions extends SideBarTab
{
	private ActionListener al;
	
	private JCheckBox drawLoop;
	private JButton createNewCarSmall;
	private JButton createNewCarMedium;
	private JButton createNewCarLarge;
	private JButton createNewCarCustom;
	private ActionListener createNewCarActionListener;

	private JButton createNewChargePointSmall;
	private JButton createNewChargePointMedium;
	private JButton createNewChargePointLarge;
	private JButton createNewChargePointCustom;
	private ActionListener createNewChargePointActionListener;
	
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

	    initActionListeners();
		init();
		refresh();
	}
	
	protected void init()
	{
		int yOffset = 10;
		int yOffsetIncrement = 20;
		int width = getWidth()-20;
		int rightMargin = 10;
		int height = 20;
		
		int bttnWidth = (int)(width/1.2);

		
		drawLoop = new JCheckBox("drawLoop", GlobalVariables.drawLoop);
		drawLoop.setBounds(rightMargin, yOffset, width, height);
		drawLoop.addActionListener(al);

		yOffset += yOffsetIncrement;
		createNewCarSmall = new JButton( "Create New Car Small" );
		createNewCarSmall.setBounds(rightMargin, yOffset, bttnWidth, height);
		createNewCarSmall.addActionListener( createNewCarActionListener );

		yOffset += yOffsetIncrement;
		createNewCarMedium = new JButton( "Create New Car Medium" );
		createNewCarMedium.setBounds(rightMargin, yOffset, bttnWidth, height);
		createNewCarMedium.addActionListener( createNewCarActionListener );

		yOffset += yOffsetIncrement;
		createNewCarLarge = new JButton( "Create New Car Large" );
		createNewCarLarge.setBounds(rightMargin, yOffset, bttnWidth, height);
		createNewCarLarge.addActionListener( createNewCarActionListener );

		yOffset += yOffsetIncrement;
		createNewCarCustom = new JButton( "Create New Car Custom" );
		createNewCarCustom.setBounds(rightMargin, yOffset, bttnWidth, height);
		createNewCarCustom.addActionListener( createNewCarActionListener );

		yOffset += yOffsetIncrement;
		createNewChargePointSmall = new JButton( "Create New Charge Point Small" );
		createNewChargePointSmall.setBounds(rightMargin, yOffset, bttnWidth, height);
		createNewChargePointSmall.addActionListener( createNewChargePointActionListener );

		yOffset += yOffsetIncrement;
		createNewChargePointMedium = new JButton( "Create New Charge Point Medium" );
		createNewChargePointMedium.setBounds(rightMargin, yOffset, bttnWidth, height);
		createNewChargePointMedium.addActionListener( createNewChargePointActionListener );

		yOffset += yOffsetIncrement;
		createNewChargePointLarge = new JButton( "Create New Charge Point Large" );
		createNewChargePointLarge.setBounds(rightMargin, yOffset, bttnWidth, height);
		createNewChargePointLarge.addActionListener( createNewChargePointActionListener );

		yOffset += yOffsetIncrement;
		createNewChargePointCustom = new JButton( "Create New Charge Point Custom" );
		createNewChargePointCustom.setBounds(rightMargin, yOffset, bttnWidth, height);
		createNewChargePointCustom.addActionListener( createNewChargePointActionListener );
		
		
		add(drawLoop);
		add(createNewCarSmall);
		add(createNewCarMedium);
		add(createNewCarLarge);
		add(createNewCarCustom);
		add(createNewChargePointSmall);
		add(createNewChargePointMedium);
		add(createNewChargePointLarge);
		add(createNewChargePointCustom);
	}
	
	private void initActionListeners()
	{
		createNewCarActionListener = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if ( e.getSource() == createNewCarSmall )
				{
					getChargerSystem().createCarAgent(GlobalVariables.carBatterySizeSmall, 50);
				}
				else if ( e.getSource() == createNewCarMedium )
				{
					getChargerSystem().createCarAgent(GlobalVariables.carBatterySizeMedium, 50);
				}
				else if ( e.getSource() == createNewCarLarge )
				{
					getChargerSystem().createCarAgent(GlobalVariables.carBatterySizeLarge, 50);
				}
				else if ( e.getSource() == createNewCarCustom )
				{
					getSimulator().showDialogCreateCar();
				}
			}
		};
		
		createNewChargePointActionListener = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if ( e.getSource() == createNewChargePointSmall )
				{
					getChargerSystem().createChargePoint(GlobalVariables.chargePointChargeRateSizeSmall);
				}
				else if ( e.getSource() == createNewChargePointMedium )
				{
					getChargerSystem().createChargePoint(GlobalVariables.chargePointChargeRateSizeMedium);
				}
				else if ( e.getSource() == createNewChargePointLarge )
				{
					getChargerSystem().createChargePoint(GlobalVariables.chargePointChargeRateSizeLarge);
				}
				else if ( e.getSource() == createNewChargePointCustom )
				{
					getSimulator().showDialogCreateChargePoint();
				}
			}
		};
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
