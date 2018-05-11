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
	Direction heading = null;
	
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
	
	@Override
	public void act() {
		if (isDead() ||  !(hasOwner) || isImmobile) {
			return;
		}
		say(describeLocation());
		
		if (SWWorld.getEntitymanager().whereIs(this).getSymbol() == "b".charAt(0)) {
			takeDamage(10);
			say(getShortDescription() + " the droid is moving through bad lands");
			
			if (isDead()) {
				say(getShortDescription() + " the droid is not movable anymore!");
				isImmobile = true;
				return;
			}	
		}
		
		if (owner.isDead()) {
			say(getShortDescription() + " the droid's owner is no more! :(");
		}
		
		
		heading = FindActor.getDirection(this, owner, this.world.getEntityManager().whereIs(this), heading);
//		say(getShortDescription() + " is heading " + heading + " next.");
		Move myMove = new Move(heading, messageRenderer, world);

		scheduler.schedule(myMove, this, 1);
		
	}
	
	@Override
	public String getShortDescription() {
		return name + " the droid";
	}

	@Override
	public String getLongDescription() {
		return this.getShortDescription();
	}

	private String describeLocation() {
		SWLocation location = this.world.getEntityManager().whereIs(this);
		return this.getShortDescription() + " [" + this.getHitpoints() + "] is at " + location.getShortDescription();

	}
	
}
