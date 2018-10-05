package gui;

import javax.swing.*;
import model.*;

@SuppressWarnings("serial")
public class Dialog extends JDialog
{
	private GUI gui;
	
	protected int xInfoOffset;
	protected int yOffset;
	protected int yOffsetIncrement;
	protected int width;
	protected int rightMargin;
	protected int height;
	
	public Dialog(GUI aGUI, String aTitle, boolean aIsModel)
	{
		super(aGUI.getFrame(), aTitle, aIsModel);
		setLayout(null);
		setVisible(false);
		gui = aGUI;
		
		xInfoOffset = 100;
		yOffset = 0;
		yOffsetIncrement = 25;
		width = 200;
		rightMargin = 20;
		height = 20;
	}
	
	public GUI getGUI()
	{
		return gui;
	}
	
	public ChargerSystem getChargerSystem()
	{
		return getGUI().getChargerSystem();
	}
}
