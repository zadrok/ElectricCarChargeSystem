package gui;

import java.awt.*;
import javax.swing.*;

public class Canvas extends JPanel
{
	private GUI gui;
	
	public Canvas(GUI aGUI, int x, int y, int width, int height)
	{
		super();
		setBounds( x,y,width,height );
		setLayout(null);
		gui = aGUI;
		
	}
	
	public void paintComponent(Graphics g)
    {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		
    }
	
	public GUI getGUI()
	{
		return gui;
	}
	
}
