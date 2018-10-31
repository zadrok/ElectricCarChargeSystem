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
	public static double timeScaleBase;
	
	public static String masterSchedulerAlgorithm;
	
	public static long runTime;
	public static long runTimeIncrement;
	
	public static int numCarsSmall;
	public static int numCarsMedium;
	public static int numCarsLarge;
	
	public static int numChargePointsSmall;
	public static int numChargePointsMedium;
	public static int numChargePointsLarge;
	
	
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
		
		timeScale = obj.get("timeScale").getAsInt();
		timeScaleBase = obj.get("timeScale").getAsInt();
		
		masterSchedulerAlgorithm = obj.get("masterSchedulerAlgorithm").getAsString();
		
		runTime = 0;
		
//		System.out.println( "outlets " + outlets );
//		System.out.println( "chargeInterval " + chargeInterval );
//		System.out.println( "drawLoop " + drawLoop );
//		System.out.println( "drawFrameRate " + drawFrameRate );
//		System.out.println( "carBatterySizeSmall " + carBatterySizeSmall );
//		System.out.println( "carBatterySizeMedium " + carBatterySizeMedium );
//		System.out.println( "carBatterySizeLarge " + carBatterySizeLarge );
//		System.out.println( "chargePointChargeRateSizeSmall " + chargePointChargeRateSizeSmall );
//		System.out.println( "chargePointChargeRateSizeMedium " + chargePointChargeRateSizeMedium );
//		System.out.println( "chargePointChargeRateSizeLarge " + chargePointChargeRateSizeLarge );
//		System.out.println( "timeScale " + timeScale );
		
		JsonArray jArrayCars = obj.get("cars").getAsJsonArray();
//		System.out.println( jArrayCars );
		for ( int i = 0; i < jArrayCars.size(); i++ )
		{
//			System.out.println( jArrayCars.get(i) );
			JsonObject jObjCar = jArrayCars.get(i).getAsJsonObject();
			
			String type = jObjCar.get( "type" ).getAsString();
			int number = jObjCar.get( "number" ).getAsInt();
			if ( type.contains( "small" ) )
			{
				numCarsSmall = number;
			}
			else if ( type.contains( "medium" ) )
			{
				numCarsMedium = number;
			}
			else if ( type.contains( "large" ) )
			{
				numCarsLarge = number;
			}
		}
		
		
		JsonArray jArrayChargePoints = obj.get("chargePoints").getAsJsonArray();
//		System.out.println( jArrayChargePoints );
		for ( int i = 0; i < jArrayCars.size(); i++ )
		{
//			System.out.println( jArrayChargePoints.get(i) );
			JsonObject jObjChargePoint = jArrayChargePoints.get(i).getAsJsonObject();
			
			String type = jObjChargePoint.get( "type" ).getAsString();
			int number = jObjChargePoint.get( "number" ).getAsInt();
			if ( type.contains( "small" ) )
			{
				numChargePointsSmall = number;
			}
			else if ( type.contains( "medium" ) )
			{
				numChargePointsMedium = number;
			}
			else if ( type.contains( "large" ) )
			{
				numChargePointsLarge = number;
			}
		}
		
//		System.out.println( "numCarsSmall " + numCarsSmall );
//		System.out.println( "numCarsMedium " + numCarsMedium );
//		System.out.println( "numCarsLarge " + numCarsLarge );
//		
//		System.out.println( "numChargePointsSmall " + numChargePointsSmall );
//		System.out.println( "numChargePointsMedium " + numChargePointsMedium );
//		System.out.println( "numChargePointsLarge " + numChargePointsLarge );
		
		
	}
	
	
	public static void importSettings(File aConfigFile)
	{
		String s = "";
		try
		{
			s = readFile(aConfigFile, Charset.defaultCharset());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		JsonObject obj = new JsonParser().parse( s ).getAsJsonObject();
		
		applySettings(obj);
		
	}
	
	static String readFile(File aConfigFile, Charset encoding) throws IOException 
	{
	  byte[] encoded = Files.readAllBytes( aConfigFile.toPath() );
	  return new String(encoded, encoding);
	}
}
