package starwars.entities.actors;

import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWEntityInterface;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.Team;
import starwars.actions.Move;
import starwars.entities.actors.behaviors.Patrol;

public class Sandcrawler extends SWActor {

	private String name;
	private Patrol path;
	boolean skipSchedule;

	public Sandcrawler(String name, int hitpoints, MessageRenderer m, SWWorld world, Direction [] moves) {
		super(Team.NEUTRAL, 100, m, world);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.path = new Patrol(moves);
		this.skipSchedule = true;
	}

	@Override
	public void act() {
		if (isDead()) {
			return;
		}
		say(describeLocation());
		
		this.skipSchedule = !this.skipSchedule;
		
		//the javas will capture all the droids if they are on the same location as sandcrawler
		for (SWEntityInterface e: this.world.getEntityManager().contents(this.world.getEntityManager().whereIs(this))) {
			if (e instanceof Droid) {
//				this.world.getEntityManager().remove(e);
				System.out.println("We're at the same locaiton bruhhh!");
			}
		}
		
		
		//if skipSchedule==false, then add the next patrol move to the schedular
		if (!skipSchedule) {
			Direction newDirection = path.getNext();
			say(getShortDescription() + " moves " + newDirection);
			Move myMove = new Move(newDirection, messageRenderer, world);

			scheduler.schedule(myMove, this, 2);
		} else {
			this.skipSchedule = true;
		}
		//not using waittime() because sandcrawler needs to act on every tcik() to capture the droid
	}
	
	@Override
	public String getShortDescription() {
		return name + " the Java Sandcrawler";
	}

	@Override
	public String getLongDescription() {
		return name + " the Droid capturing machine!";
	}

	private String describeLocation() {
		SWLocation location = this.world.getEntityManager().whereIs(this);
		return this.getShortDescription() + " [" + this.getHitpoints() + "] is at " + location.getShortDescription();

	}

}
