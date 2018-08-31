package boot;

import gui.GUI;

public class Main 
{

	public static void main(String[] args) 
	{
		jade.Boot.main(new String[] {"-gui"}); //start jade
		GUI gui = new GUI();
	}

}
