/*************************
 * GlobalVariables.java
 * 
 * Global variables are used for behaviour that exists throughout
 * the entire program. This also will parse the settings Json file
 * 
 */

package boot;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.*;

public class GlobalVariables 
{
	public static long outlets;
	public static int chargeInterval; // in minutes
	public static boolean drawLoop;
	public static int drawFrameRate;
	
	public static int carBatterySizeSmall;
	public static int carBatterySizeMedium;
	public static int carBatterySizeLarge;
	
	public static int chargePointChargeRateSizeSmall;
	public static int chargePointChargeRateSizeMedium;
	public static int chargePointChargeRateSizeLarge;
	
	public static double timeScale;
	
	
	// add variables here to get information from settings file
	private static void applySettings(JsonObject obj)
	{
		outlets = obj.get("outlets").getAsLong();
		chargeInterval = obj.get("chargeInterval").getAsInt();
		drawLoop = obj.get("drawLoop").getAsBoolean();
		drawFrameRate = obj.get("drawFrameRate").getAsInt();
		
		carBatterySizeSmall = obj.get("carBatterySizeSmall").getAsInt();
		carBatterySizeMedium = obj.get("carBatterySizeMedium").getAsInt();
		carBatterySizeLarge = obj.get("carBatterySizeLarge").getAsInt();
		
		chargePointChargeRateSizeSmall = obj.get("chargePointChargeRateSizeSmall").getAsInt();
		chargePointChargeRateSizeMedium = obj.get("chargePointChargeRateSizeMedium").getAsInt();
		chargePointChargeRateSizeLarge = obj.get("chargePointChargeRateSizeLarge").getAsInt();
		
		timeScale = 60.0;
	}
	
	
	public static void importSettings()
	{
		String s = "";
		try
		{
			s = readFile("settings.json", Charset.defaultCharset());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		JsonObject obj = new JsonParser().parse( s ).getAsJsonObject();
		
		applySettings(obj);
		
	}
	
	static String readFile(String path, Charset encoding) throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
}
