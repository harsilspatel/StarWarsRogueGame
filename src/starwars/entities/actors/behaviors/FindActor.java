package starwars.entities.actors.behaviors;

import java.util.ArrayList;

import edu.monash.fit2099.gridworld.Grid;
import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.space.Location;
import starwars.SWActor;
import starwars.SWWorld;

public class FindActor {
	
	/**
	 * get the next direction in which the actor is supposed to move
	 * @param actor who is looking for the owner
	 * @param owner the actor that is being looked for
	 * @param currentLocation actor's current location
	 * @param lastDirection actor's last direction
	 * @return the next direction for the actor to move in
	 */
	public static Direction getDirection(SWActor actor, SWActor owner, Location currentLocation, Direction lastDirection) {
		
		// build a list of available directions
		ArrayList<Direction> possibleDirections = new ArrayList<Direction>();
		
		for (Grid.CompassBearing d : Grid.CompassBearing.values()) {
			if (SWWorld.getEntitymanager().seesExit(actor, d)) {
				//if owner is in a neighbouring location, return owner's direction
				if (SWWorld.getEntitymanager().whereIs(owner) == currentLocation.getNeighbour(d)) {
					return d;
				}
				//else add that direction into possible directions list
				possibleDirections.add(d);
			}
		}
		//since, if the owner is not found, the (droid) actor should move in the same direction until it cannot go further,
		//hence we check if the actor can still move in the same direction as previous, if it can, we return that direction
		if (possibleDirections.contains(lastDirection)) {
			return lastDirection;
		}
		
		//if the actor cannot move in that direction, we choose a random direction
		Direction heading = possibleDirections.get((int) (Math.floor(Math.random() * possibleDirections.size())));
		return heading;
		
		
	}
	
}
