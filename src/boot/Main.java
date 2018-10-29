/*************************
 * Main.java
 * 
 * Main entry point for program. Will setup agents, system
 * and GUI and allow the program to run.
 * 
 */

package boot;

import gui.*;

public class Main 
{

	public static void main(String[] args)
	{
		jade.Boot.main(new String[] {"-gui"}); //start jade
		GUI gui = new GUI();
	}

}



