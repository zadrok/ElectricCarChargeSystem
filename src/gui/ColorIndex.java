/*************************
 * ColorIndex.java
 * 
 * 
 * 
 */

package gui;

import java.awt.Color;
import java.util.Random;

public class ColorIndex
{
	public static Color window = new Color( 255, 255, 255, 255 );
	public static Color sideBarFill = Color.LIGHT_GRAY;
	public static Color canvasFill = Color.WHITE;
	public static Color chargePointRect = new Color( 50, 100, 50, 50 );
	public static Color carRect = new Color( 100, 50, 50, 50 );
	public static Color chargePointRectTitle = new Color( 0, 0, 0, 255 );
	public static Color carRectTitle = new Color( 0, 0, 0, 255 );
	
	public static Color selectedChargePointCell = new Color( 200,100,200,100 );
	public static Color chargePointCell = new Color( 100,100,100,100 );
	public static Color chargePointEmptyBar = new Color( 100, 200, 100, 200 );
	
	public static Color selectedCarCell = new Color( 200,100,200,100 );
	public static Color carCell = new Color( 200,100,100,100 );
	
	public static Color rotatingCircleNormal = Color.BLACK;
	public static Color rotatingCircleError = Color.RED;
	
	public static Color carID = Color.DARK_GRAY;
	
	public static Color carStateCharge = Color.ORANGE;
	public static Color carStateIdle = Color.YELLOW;
	public static Color carStateCharging = Color.GREEN;
	public static Color carStateBurn = Color.MAGENTA;
	public static Color carStateError = Color.RED;
	
	public static Color carChargeBarBackground = new Color( 100, 100, 100, 100 );
	public static Color carChargeBarForeground = new Color( 100, 200, 100, 200 );


	
	
	public static Random random = new Random();
	
	public static Color getRandomColor()
	{
		int a = 200;
		int b = 40;
		return new Color( random.nextInt(a)+b, random.nextInt(a)+b, random.nextInt(a)+b );
	}
}
