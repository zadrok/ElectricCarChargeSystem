/*************************
 * MenuBar.java
 * 
 * 
 * 
 */

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar
{
	public GUI gui;
	public customActionListener actionListener;
	
	public JMenu file;
	public JMenuItem mainMenu;
	public JMenuItem exit;

	public JMenu edit;
	
	public JMenu blank;
	public JMenu blank2;
	
	public JMenu clock;
	public JMenu clockreal;

	
	public MenuBar(GUI aGUI)
	{
		gui = aGUI;
		actionListener = new customActionListener(this);
		
		file = new JMenu("File");
		mainMenu = new JMenuItem("Main Menu");
		exit = new JMenuItem("Exit");

//		edit = new JMenu("Edit");
		
		blank = new JMenu( "           " );
		blank.setEnabled(false);
		blank2 = new JMenu( "           " );
		blank2.setEnabled(false);
		
		clock = new JMenu( "CLOCK" );
		clock.setEnabled(false);
		
		clockreal = new JMenu( "CLOCK" );
		clockreal.setEnabled(false);

		add(file);
		file.add(mainMenu);
		file.addSeparator();
		file.add(exit);
//		add(edit);
		add(blank);
		add(clock);
		add(blank2);
		add(clockreal);
		
		exit.addActionListener(actionListener);
		mainMenu.addActionListener(actionListener);
	}

}

class customActionListener implements ActionListener
{
	public MenuBar menuBar;

	public customActionListener(MenuBar aMenuBar)
	{
		menuBar = aMenuBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == menuBar.exit)
		{
			menuBar.gui.exit();
		}
		else if (e.getSource() == menuBar.mainMenu)
		{
			menuBar.gui.runMainMenu();
		}
		
	}
	
}
