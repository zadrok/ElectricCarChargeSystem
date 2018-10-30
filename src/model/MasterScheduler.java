/*************************
 * MasterScheduler.java
 * 
 * Handles the scheduling of cars for charge points
 * 
 */

package model;

import java.util.ArrayList;

import boot.GlobalVariables;
import jade.core.Agent;
import scheduleAlgorithm.*;

@SuppressWarnings("serial")
public class MasterScheduler extends Agent
{
	private MasterSchedulerBehaviourBasic MSBehaviour;
	private ChargerSystem chargerSystem;
	private ScheduleAlgorithm schAlgo;
	private ArrayList<ScheduleAlgorithm> algos;
	
	// Constructor for master scheduler
	public MasterScheduler(ChargerSystem aSys)
	{
		super();
		chargerSystem = aSys;
		
		// add any new algorithms to this list so it can be selected in the GUI
		algos = new ArrayList<>();
		algos.add( new SAGreedy( aSys, "greedy" ) );
		algos.add( new SAGreedy2( aSys, "greedy2" ) );
		
		// Pick scheduling algorithm
		// pick based on what was in the config file
		if ( GlobalVariables.masterSchedulerAlgorithm != null )
		{
			for ( ScheduleAlgorithm sa : algos )
			{
				if ( sa.getName().contentEquals( GlobalVariables.masterSchedulerAlgorithm ) )
				{
					schAlgo = sa;
					break;
				}
			}
		}
		
		// if no match (null) set to first algorithm in list
		if ( schAlgo == null )
			schAlgo = algos.get(0);
	}
	
	// Setup behaviour of scheduler
	protected void setup()
	{
		MSBehaviour =  new MasterSchedulerBehaviourBasic( this ) ;
		addBehaviour(MSBehaviour);
	}
	
	// Get behaviour of scheduler
	public MasterSchedulerBehaviourBasic getMSBehaviour()
	{
		return MSBehaviour;
	}
	
	// Get charger system
	public ChargerSystem getChargerSystem()
	{
		return chargerSystem;
	}
	
	// Get scheduling algorithm
	public ScheduleAlgorithm getScheduleAlgorithm()
	{
		return schAlgo;
	}
	
	// Set scheduling algorithm
	public void setScheduleAlgorithm(ScheduleAlgorithm aSchAlgo)
	{
		schAlgo = aSchAlgo;
	}
	
	// Get all scheduling algorithms
	public ArrayList<ScheduleAlgorithm> getAllScheduleAlgorithms()
	{
		return algos;
	}
}
