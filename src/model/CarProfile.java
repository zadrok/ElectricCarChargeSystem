package model;

import java.util.ArrayList;
import java.util.Random;

public class CarProfile
{
	private Car car;
	private Random random = new Random();
	private ArrayList<Integer> usage;
	
	// hours for profile to be for
	private int hours;
	// number of time polled per hour
	// we would only use 2 values per hour but this gives a slightly better looking graph
	// going to take the average of 4 values
	private int timesPerHour; // 8
	private int range;
	private int lowerBracket; // anything below this is 0
	private int upperBracket; // anything above this is 100
	private int smoothAmount; // number times to smooth
	
	public CarProfile(Car aCar)
	{
		car = aCar;
		usage = new ArrayList<>();
		
		hours = 24;
		timesPerHour = 8;
		range = 100;
		lowerBracket = 50;
		upperBracket = 75;
		smoothAmount = 1;
		
		// generate int list
		for ( int i = 0; i <= hours*timesPerHour; i++ )
			usage.add( random.nextInt(range) );
		
//		System.out.println( "a " + usage.toString() );
		
		// smooth int list
		for ( int i = 0; i < smoothAmount; i++ )
			usage = smooth(usage);
		
//		System.out.println( "b " + usage.toString() );
		
		// clamp ints
		for ( int i = 0; i < usage.size(); i++ )
		{
			if ( usage.get(i) >= upperBracket )
				usage.set(i, 100);
			
			if ( usage.get(i) <= lowerBracket )
				usage.set(i, 0);
		}
		
//		System.out.println( "c " + usage.toString() );
	}
	
	private ArrayList<Integer> smooth(ArrayList<Integer> aList)
	{
		ArrayList<Integer> newList = new ArrayList<>();
		
		for ( int i = 0; i < aList.size(); i++ )
		{
			
			int x1 = 0;
			int x2 = 0;
			int x3 = 0;
			
			// try and get int at index before, if can't get int at array end
			try
			{
				x1 = aList.get( i-1 );
			}
			catch( IndexOutOfBoundsException e )
			{
				x1 = aList.get( aList.size()-1 );
			}
			
			// get current int
			x2 = aList.get(i);
			
			// try and get next int, if can't get first int
			try
			{
				x2 = aList.get( i+1 );
			}
			catch( IndexOutOfBoundsException e )
			{
				x2 = aList.get( 0 );
			}
			
			// average of ints
			int n = ( x1 + x2 + x3 ) / 3;
			
//			System.out.println( "x1 " + x1 + " x2 " + x2 + " x3 " + x3 + " n " + n );
			
			newList.add( n );
			
		}
		
		return newList;
	}
	
	public Car getCar()
	{
		return car;
	}
	
	public ArrayList<Integer> getUsage()
	{
		return usage;
	}
}
