package starwars.entities.actors;


import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.Team;
import starwars.actions.Attack;
import starwars.actions.Move;
import starwars.entities.actors.behaviors.FindActor;

public class Droid extends SWActor {
	private String name;
	private boolean isImmobile;
	private boolean hasOwner;
	private SWActor owner;
	private Direction heading = null;
	
	/**
	 * Create a Droid.
	 * Droid will randomly move around the map if they are not immobile and they have an owner
	 * They are all members of NEUTRAL team
	 * Once their owner is in their neighbouring location, they move to that location 
	 * 
	 * @param hitpoints the health a droid has
	 * @param name the droid's name
	 * @param m <code>MessageRenderer</code> to display messages.
	 * @param world the <code>SWWorld</code> world to which this <code>Droid</code> belongs to
	 * @param owner Droid's owner, the one that droid will be looking for to follow
	 */
	public Droid(int hitpoints, String name, MessageRenderer m, SWWorld world, SWActor owner) {
		super(Team.NEUTRAL, 100, m, world);
		this.name = name;
		this.owner = owner;
		this.hasOwner = true;
		this.isImmobile = false;
		
		//Attack affordance is added in SWActor class so removing it for Droids
		SWAffordance attack = new Attack(this, m);
		this.removeAffordance(attack);
	}
	
	/**
	 * Droid will not move if the owner is dead or if it is immobile.
	 * if droid is moving throught badlands, it will take damage of 10 units
	 * If owner is not found, droid will keep moving in a randomly chosen direction till it cannot move further.
	 * If droid's owner is in neighbouring location, droid will move to that location
	 */
	@Override
	public void act() {
		if (isDead() ||  !(hasOwner) || isImmobile) {
			return;
		}
		
		//checking if owner is dead
		if (owner.isDead()) {
			say(getShortDescription() + " the droid's owner is no more! :(");
			hasOwner = false;
		}
		
		say(describeLocation());
		
		//checking if moving through badlands
		if (SWWorld.getEntitymanager().whereIs(this).getSymbol() == "b".charAt(0)) {
			takeDamage(10);
			say(getShortDescription() + " the droid is moving through bad lands");
			
			//checking if after moving through badlands the droid still have life or not.
			if (isDead()) {
				say(getShortDescription() + " the droid is not movable anymore!");
				isImmobile = true;
				return;
			}	
		}
		
		
		
		//getting next direction
		heading = FindActor.getDirection(this, owner, heading);
//		say(getShortDescription() + " is heading " + heading + " next.");
		Move myMove = new Move(heading, messageRenderer, world);

		scheduler.schedule(myMove, this, 1);
		
	}
	
	/**
	 * @return droid's short description
	 */
	@Override
	public String getShortDescription() {
		return name + " the droid";
	}
	
	/**
	 * @return droid's long description
	 */
	@Override
	public String getLongDescription() {
		return name + " a resourceful astromech droid";
	}

	/**
	 * 
	 * @return a string describing droid's location on the map
	 */
	private String describeLocation() {
		SWLocation location = this.world.getEntityManager().whereIs(this);
		return this.getShortDescription() + " [" + this.getHitpoints() + "] is at " + location.getShortDescription();

	}
	
}
