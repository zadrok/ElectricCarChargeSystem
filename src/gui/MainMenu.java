package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.*;

@SuppressWarnings("serial")
public class MainMenu extends Scene
{
	private File folder;
	private File[] files;
	
	private File selectedFile;
	
	private DefaultListModel<File> fileListModel;
	private JList<File> fileList;
	private JScrollPane fileScrollPane;
	
	public MainMenu(GUI aGUI, int x, int y, int width, int height)
	{
		super(aGUI, x, y, width, height);
		
		// find all files in config folder
		folder = new File("configs/");
		// make a list if them
		files = folder.listFiles();
		
		// turn list into usable list (model) for JList
		fileListModel = new DefaultListModel<>();
		
		for ( File f : files )
		{
			if ( f.isFile() )
				fileListModel.addElement( f );
		}
		
		// create jList and set first file to selected
		fileList = new JList<File>(fileListModel);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.setSelectedIndex(0);
		fileList.setVisibleRowCount(3);
		
		selectedFile = fileList.getSelectedValue();
		
		fileScrollPane = new JScrollPane(fileList);
		
		// set the name on the button to match the file name
		String s = "Start with ";
		if ( fileList.getSelectedValue() != null )
			s += fileList.getSelectedValue().getName();
		else
			s += "... (select one)";
		JButton bttn = new JButton( s );
		// action for button 
		bttn.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				getGUI().runSimulator( selectedFile );
			}
		});
		
		// action for selecting different file in list
		fileList.addMouseListener( new MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
				selectedFile = fileList.getSelectedValue();
				if ( selectedFile != null )
					bttn.setText( "Start with " + selectedFile.getName() );
				
				if ( e.getClickCount() >= 2 )
				{
					getGUI().runSimulator( selectedFile );
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		} );

		// place list and button
		fileScrollPane.setBounds(10, 50, 300, 400);
		bttn.setBounds(10, 10, 300, 30);
		
		// add to window
		add(bttn);
		add(fileScrollPane);
	}
	
}
