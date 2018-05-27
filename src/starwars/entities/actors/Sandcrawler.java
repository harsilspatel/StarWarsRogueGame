package starwars.entities.actors;

import edu.monash.fit2099.gridworld.Grid;
import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.time.Scheduler;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWEntityInterface;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.Team;
import starwars.actions.Enter;
import starwars.actions.Exit;
import starwars.actions.Move;
import starwars.entities.Door;
import starwars.entities.actors.behaviors.Patrol;
import starwars.swinterfaces.SWGridController;

public class Sandcrawler extends SWActor {

	private String name;
	private Patrol path;
	private boolean skipSchedule;
	private SWWorld crawlerWorld;
	private Scheduler crawlerScheduler;
	private SWGridController crawlerUIController;
	private final SWLocation DOOR_LOCATION;
	private Droid capturedDroid = null;
	private boolean justReleasedDroid;
	
	public Sandcrawler(String name, int hitpoints, MessageRenderer m, SWWorld world, Direction [] moves) {
		super(Team.NEUTRAL, 100, m, world);
		this.name = name;
		this.path = new Patrol(moves);
		this.skipSchedule = true;
		
		this.crawlerWorld = new SWWorld(4,4);
		this.crawlerScheduler = new Scheduler(1, crawlerWorld);
		this.crawlerWorld.initializeWorld(name);
		this.addAffordance(new Enter(this, m));
		this.DOOR_LOCATION = this.crawlerWorld.getGrid().getLocationByCoordinates(0, 0);
		Door theDoor = new Door(m, this.name + " door");
		this.crawlerWorld.getEntityManager().setLocation(theDoor, this.DOOR_LOCATION);
	}

	@Override
	public void act() {
		if (isDead()) {
			return;
		}
		say(describeLocation());
		
		this.skipSchedule = !this.skipSchedule;
		
		//the javas will capture all the droids if they are on the same location as sandcrawler
		if (!justReleasedDroid && capturedDroid == null) {
			for (SWEntityInterface e: this.world.getEntityManager().contents(this.world.getEntityManager().whereIs(this))) {
				if (e instanceof Droid) {
					this.world.getEntityManager().remove(e);
					SWLocation droidLocation = this.crawlerWorld.getGrid().getLocationByCoordinates(0, 0);
					this.crawlerWorld.getEntityManager().setLocation(e, droidLocation);
					this.say(this.getShortDescription() + " has captured " + e.getShortDescription());
					capturedDroid = (Droid) e;
					break;
				}
			}
		} else {
			justReleasedDroid = false;
		}
		
//		for (SWEntityInterface e: this.world.getEntityManager().contents(this.world.getEntityManager().whereIs(this))) {
//			if (e instanceof SWActor) {
//				this.world.getEntityManager().remove(e);
//			}
//		} 
		
		
		//if skipSchedule==false, then add the next patrol move to the schedular
		if (!skipSchedule) {
			Direction newDirection = path.getNext();
			say(getShortDescription() + " moves " + newDirection);
			Move myMove = new Move(newDirection, messageRenderer, world);

			scheduler.schedule(myMove, this, 2);
		} else {
			this.skipSchedule = true;
		}
		
		//not using waittime() because sandcrawler needs to act on every tick() to capture the droid
	}
	
	public void enterCrawler(SWEntityInterface e) {
		Scheduler mainScheduler = SWActor.getScheduler();
		this.crawlerUIController = new SWGridController(crawlerWorld, false);
		SWLocation loc = this.crawlerWorld.getGrid().getLocationByCoordinates(0, 1);
		this.crawlerWorld.getEntityManager().setLocation(e, loc);
		SWActor a = (SWActor) e;
		a.resetMoveCommands(loc);
		SWActor.setScheduler(crawlerScheduler);
		while(true) {
			crawlerUIController.render();
			crawlerScheduler.tick();
			if (crawlerUIController.userDecision() instanceof Exit) {
				break;
			}

		}
		
		SWLocation droidLocation = crawlerWorld.getEntityManager().whereIs(capturedDroid);
		SWLocation actorLocation = crawlerWorld.getEntityManager().whereIs(a);
		boolean isDroidNearby = false;
		
		for (Grid.CompassBearing d : Grid.CompassBearing.values()) {
			if (crawlerWorld.getEntityManager().seesExit(a, d)) {
				//if owner is in a neighbouring location, return owner's direction
				if (droidLocation == actorLocation.getNeighbour(d)) {
					isDroidNearby = true;
				}
			}
		}
		
		if (droidLocation == actorLocation) {
			isDroidNearby = true;
		}
		
		
		SWActor.setScheduler(mainScheduler);
		loc = SWWorld.getEntitymanager().whereIs(this);
		SWWorld.getEntitymanager().setLocation(a, loc);
		a.resetMoveCommands(loc);
		
		if (isDroidNearby) {
			crawlerWorld.getEntityManager().remove(capturedDroid);
			SWWorld.getEntitymanager().setLocation(capturedDroid, loc);
			justReleasedDroid = true;
			capturedDroid.say(capturedDroid.getShortDescription() + " cheers as the master rescued him!");
			capturedDroid = null;
		}
		
		this.messageRenderer = new SWGridController(this.world, false);
		
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
