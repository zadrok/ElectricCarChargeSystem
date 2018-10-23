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

	
	public MenuBar(GUI aGUI)
	{
		gui = aGUI;
		actionListener = new customActionListener(this);
		
		file = new JMenu("File");
		exit = new JMenuItem("Exit");

		edit = new JMenu("Edit");

		add(file);
		file.add(exit);
		add(edit);
		
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
