/*************************
 * Dialog.java
 * 
 * 
 * 
 */

package gui;

import javax.swing.*;
import model.*;

@SuppressWarnings("serial")
public class Dialog extends JDialog
{
	private Simulator simulator;
	
	protected int xInfoOffset;
	protected int yOffset;
	protected int yOffsetIncrement;
	protected int width;
	protected int rightMargin;
	protected int height;
	
	public Dialog(Simulator aSimulator, String aTitle, boolean aIsModel)
	{
		super(aSimulator.getGUI().getFrame(), aTitle, aIsModel);
		setLayout(null);
		setVisible(false);
		simulator = aSimulator;
		
		xInfoOffset = 100;
		yOffset = 0;
		yOffsetIncrement = 25;
		width = 200;
		rightMargin = 20;
		height = 20;
	}
	
	public Simulator getSimulator()
	{
		return simulator;
	}
	
	public GUI getGUI()
	{
		return getSimulator().getGUI();
	}
	
	public ChargerSystem getChargerSystem()
	{
		return getSimulator().getChargerSystem();
	}
}
