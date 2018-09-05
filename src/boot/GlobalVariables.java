package boot;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.*;

public class GlobalVariables 
{
	public static long outlets;
	public static long chargeInterval; // in minutes
	public static boolean draw;
	
	
	// add variavles here to get information from settings file
	private static void applySettings(JsonObject obj)
	{
		outlets = obj.get("outlets").getAsLong();
		chargeInterval = obj.get("chargeInterval").getAsLong();
		draw = obj.get("draw").getAsBoolean();
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
