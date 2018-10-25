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
	public JMenuItem exit;

	public JMenu edit;
	
	public JMenu blank;
	
	public JMenu clock;

	
	public MenuBar(GUI aGUI)
	{
		gui = aGUI;
		actionListener = new customActionListener(this);
		
		file = new JMenu("File");
		exit = new JMenuItem("Exit");

//		edit = new JMenu("Edit");
		
		blank = new JMenu( "           " );
		blank.setEnabled(false);
		
		clock = new JMenu( "CLOCK" );
		clock.setEnabled(false);

		add(file);
		file.add(exit);
//		add(edit);
		add(blank);
		add(clock);
		
		exit.addActionListener(actionListener);
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
		
	}
	
}
