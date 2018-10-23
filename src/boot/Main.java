/*************************
 * Main.java
 * 
 * Main entry point for program. Will setup agents, system
 * and GUI and allow the program to run.
 * 
 */

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
		charSys.initChargerPoints();
		GUI gui = new GUI( charSys );
		gui.startDrawLoop();
	}

}



