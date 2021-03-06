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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	
	private JButton add30Minutes;
	
	private JSlider timeScaleSlider;
	private JLabel timeScaleSliderLabel;
	
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
		
		yOffset += yOffsetIncrement;
		yOffset += yOffsetIncrement;
		add30Minutes = new JButton("+30 minutes");
		add30Minutes.setBounds(rightMargin, yOffset, bttnWidth, height);
		add30Minutes.addActionListener( new ActionListener() { public void actionPerformed(ActionEvent e) { GlobalVariables.runTime += 30*60; } } );
		
		yOffset += yOffsetIncrement;
		yOffset += yOffsetIncrement;
		timeScaleSliderLabel = new JLabel("Time Scale 1 - 10 (agents don't run faster just system clock)");
		timeScaleSliderLabel.setBounds(rightMargin, yOffset, bttnWidth+20, height);
		
		yOffset += yOffsetIncrement;
		timeScaleSlider = new JSlider(1,10,1);
		timeScaleSlider.setBounds(rightMargin, yOffset, bttnWidth, height);
		timeScaleSlider.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				GlobalVariables.clockTimeScale = GlobalVariables.clockTimeScaleBase * ((JSlider)e.getSource()).getValue();
				}
			} );
		
		
		add(drawLoop);
		add(createNewCarSmall);
		add(createNewCarMedium);
		add(createNewCarLarge);
		add(createNewCarCustom);
		add(createNewChargePointSmall);
		add(createNewChargePointMedium);
		add(createNewChargePointLarge);
		add(createNewChargePointCustom);
		
		add(add30Minutes);
		add(timeScaleSliderLabel);
		add(timeScaleSlider);
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
				
				sideBar.refreshCarList();
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
				
				sideBar.refreshCarList();
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
