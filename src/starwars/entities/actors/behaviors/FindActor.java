package starwars.entities.actors.behaviors;

import java.util.ArrayList;

import edu.monash.fit2099.gridworld.Grid;
import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.space.Location;
import starwars.SWActor;
import starwars.SWWorld;

public class FindActor {
	
	
	public static Direction getDirection(SWActor actor, SWActor owner, Location currentLocation, Direction lastDirection) {
		
		ArrayList<Direction> possibleDirections = new ArrayList<Direction>();

		// build a list of available directions
		for (Grid.CompassBearing d : Grid.CompassBearing.values()) {
			if (SWWorld.getEntitymanager().seesExit(actor, d)) {
				if (SWWorld.getEntitymanager().whereIs(owner) == currentLocation.getNeighbour(d)) {
					return d;
				}
				possibleDirections.add(d);
			}
		}
		
		if (possibleDirections.contains(lastDirection)) {
			return lastDirection;
		}
		
		Direction heading = possibleDirections.get((int) (Math.floor(Math.random() * possibleDirections.size())));
//		Location nextLocation = currentLocation.getNeighbour(heading);
		return heading;
		
		
	}
	
}
