package boot;

import gui.*;
import model.*;

public class Main 
{

	public static void main(String[] args)
	{	
		GlobalVariables.importSettings();
		
		jade.Boot.main(new String[] {"-gui"}); //start jade
		ChargerSystem charSys = new ChargerSystem();
		charSys.initJadeAgents();
		GUI gui = new GUI( charSys );
		gui.startDrawLoop();
	}

}



