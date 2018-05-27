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
	/**
	 * A name lable of this Sandcrawler
	 */
	private String name;
	
	/**
	 * The patrol path of this Sandcrawler
	 */
	private Patrol path;
	
	/**
	 * An alternating boolean value to schedule this sandcrawler's Move action every alternating tick()
	 */
	private boolean skipSchedule;
	
	/**
	 * <code>SWWorld</code> of this <code>Sandcrawler</code>
	 */
	private SWWorld crawlerWorld;
	
	/**
	 * <code>Scheduler</code> of sandcrawler's crawlerWorld
	 */
	private Scheduler crawlerScheduler;
	
	/**
	 * <code>SWGridController</code> to interact with sandcrawler's crawlerWorld
	 */
	private SWGridController crawlerUIController;
	
	/**
	 * A fixed location of the door where the entities will enter to and go from the sandcrawler
	 */
	private final SWLocation DOOR_LOCATION;
	
	/**
	 * The variable to keep track of the captured droid 
	 */
	private Droid capturedDroid = null;
	
	/**
	 * A flag to track whether the droid was release just now, to prevent it from immediately capturing the Droid following its release.
	 */
	private boolean justReleasedDroid;
	
	/**
	 * Constructor for the <code>Sandcrawler</code> class. This constructor will,
	 * <ul>
	 * 	<li>Initialize the message renderer for the <code>Sandcrawler</code></li>
	 * 	<li>Set the hit points of this <code>Sandcrawler</code> to 100</li>
	 * 	<li>Add a <code>Enter</code> affordance to this <code>Sandcrawler</code> it can be entered-to</li> 
	 *	<li>Add a <code>Door entity</code> to this <code>Sandcrawler</code> so it can be used to <code>Exit</code></li>
	 * </ul>
	 * 
	 * @param name this crawler's name. Used in displaying descriptions.
	 * @param hitpoints the number of hit points of this sandcrawler. 
	 * @param m <code>MessageRenderer</code> to display messages.
	 * @param world the <code>SWWorld</code> world to which this <code>Sandcrawler</code> belongs to
	 * @param moves the sandcrawler's patrolling path
	 */
	public Sandcrawler(String name, int hitpoints, MessageRenderer m, SWWorld world, Direction [] moves) {
		super(Team.NEUTRAL, 100, m, world);
		this.name = name;
		this.path = new Patrol(moves);
		this.skipSchedule = true; //to schedule the patrol move every alternate turn.
		
		//creating and initializing a new world inside the crawler
		this.crawlerWorld = new SWWorld(4,4);
		this.crawlerScheduler = new Scheduler(1, crawlerWorld);
		this.crawlerWorld.initializeWorld(name);
		
		//adding Enter affordance to the Sandcrawler, so that player can enter it
		this.addAffordance(new Enter(this, m));
		
		//setting a fixed door location where all the entities will be "set to" when they enter the sandcrawler
		this.DOOR_LOCATION = this.crawlerWorld.getGrid().getLocationByCoordinates(0, 0);
		
		//creating and setting the door
		Door theDoor = new Door(m, this.name + " door");
		this.crawlerWorld.getEntityManager().setLocation(theDoor, this.DOOR_LOCATION);
	}

	/**
	 * The method which will command Sandcrawler
	 * @author harsilpatel
	 */
	@Override
	public void act() {
		//Don't perform act() if dead.
		if (isDead()) {
			return;
		}
		//describe the location within the SWWorld
		say(describeLocation());
		
		//skipSchedule will alternate to true and false every turn, so that if schedules Sandcrawler's Move command every alternate turn
		this.skipSchedule = !this.skipSchedule;
		
		//justReleasedDroid is to check if the droid is just released from the crawler, if so don't capture it immediately
		//capturedDroid is to check if there is already a droid captured, if null then capture the droid.
		if (!justReleasedDroid && capturedDroid == null) {
			
			//the javas will capture the droid if both are on the same location
			for (SWEntityInterface e: this.world.getEntityManager().contents(this.world.getEntityManager().whereIs(this))) {
				if (e instanceof Droid) {
					capturedDroid = (Droid) e;
					this.world.getEntityManager().remove(capturedDroid);	//remove the droid from the main world
					crawlerWorld.getEntityManager().setLocation(capturedDroid, DOOR_LOCATION); //set it into the crawler's world at DOOR_LOCATION
					say(this.getShortDescription() + " has captured " + capturedDroid.getShortDescription()); //describe the incident
					break;
				}
			}
		} else {
			//if droid was just released, change justReleasedDroid to false so it can be captured again on the next turn.
			justReleasedDroid = false;
		}
		
		
		//if skipSchedule == false, then add the next patrol move to the schedular
		if (!skipSchedule) {
			Direction newDirection = path.getNext();
			say(getShortDescription() + " moves " + newDirection);
			Move myMove = new Move(newDirection, messageRenderer, world);

			scheduler.schedule(myMove, this, 1);
		} else {
			this.skipSchedule = true;
		}
		
		//not using waittime() because sandcrawler needs to act on every tick() to capture the droid
	}
	
	/**
	 * Method that gets called when Player enters the Sandcrawler
	 * @param e the Entity entering Sandcrawler
	 * @author harsilpatel
	 */
	public void insideCrawler(SWEntityInterface e) {
		//save the main scheduler so that it can be restored when exiting
		Scheduler mainScheduler = SWActor.getScheduler();
		
		//setting up SWGridController to show Sandcrawler's UI. Setting showBanner to false
		this.crawlerUIController = new SWGridController(crawlerWorld, false);
		
		
		//setting actor's location to DOOR_LOCATION in the crawlerWorld
		SWActor a = (SWActor) e;
		crawlerWorld.getEntityManager().setLocation(a, DOOR_LOCATION);
		
		//resetting move commands for the actor
		a.resetMoveCommands(DOOR_LOCATION);
		
		//setting the crawlerScheduler for the gameplay
		SWActor.setScheduler(crawlerScheduler);
		
		while(true) {
			crawlerUIController.render();
			crawlerScheduler.tick();
			
			// will check if the user selected Exit action from the menu, if so, then break out of the infinite loop and carryout the exit protocol.
			if (SWGridController.userDecision() instanceof Exit) {
				break;
			}
		}
		
		//exit protocol begins
		
		//check if Droid is nearby the owner, if so, then the droid will be taken out from the crawlerWorld to the main world
		SWLocation droidLocation = crawlerWorld.getEntityManager().whereIs(capturedDroid);
		SWLocation actorLocation = crawlerWorld.getEntityManager().whereIs(a);
		
		//a boolean flag to see if droid is nearby
		boolean isDroidNearby = false;
		
		for (Grid.CompassBearing d : Grid.CompassBearing.values()) {
			if (crawlerWorld.getEntityManager().seesExit(a, d)) {
				//if droid is in a neighbouring location, set the flag to true
				if (droidLocation == actorLocation.getNeighbour(d)) {
					isDroidNearby = true;
				}
			}
		}
		
		//or if droid is in the same location, set the flag to true
		if (droidLocation == actorLocation) {
			isDroidNearby = true;
		}
		
		//restoring main scheduler
		SWActor.setScheduler(mainScheduler);
		
		//setting Player to the main world at crawler's location
		SWLocation crawlerLocation = getLocation();
		SWWorld.getEntitymanager().setLocation(a, crawlerLocation);
		a.resetMoveCommands(crawlerLocation);
		
		//if the droid is nearby, i.e. if droid was rescued, set the droid free to the main world
		if (isDroidNearby) {
			crawlerWorld.getEntityManager().remove(capturedDroid);
			SWWorld.getEntitymanager().setLocation(capturedDroid, crawlerLocation);
			
			//setting justReleasedDroid, because after setting the droid free, the crawler and droid might be in the same location
			//if they are in the same location, don't immediately capture droid back, so setting justReleasedDroid to true
			justReleasedDroid = true;
			
			//droid describing it's release
			capturedDroid.say(capturedDroid.getShortDescription() + " cheers as the master rescued him!");
			
			//setting capturedDroid to null, so that Sandcrawler can capture a droid again.
			capturedDroid = null;
		}
		
		// at last, change the UI back to the main SWWorld and set showBanner to false
		this.messageRenderer = new SWGridController(this.world, false);
	}
	
	/**
	 * This method returns Sandcrawler's short description
	 * @returns A string with Sandcrawler's short description
	 */
	@Override
	public String getShortDescription() {
		return name + " the Java Sandcrawler";
	}

	/**
	 * This method returns Sandcrawler's long description
	 * @returns A string with Sandcrawler's long description
	 */
	@Override
	public String getLongDescription() {
		return name + " the Droid capturing machine!";
	}

	/**
	 * This method will describe, <code>Sandcrawler</code>'s location and its hitpoints
	 * The output from this method would be through the <code>MessageRenderer</code>.
	 * @return a string describing location and hitpoints of sandcrawler
	 */
	private String describeLocation() {
		SWLocation location = getLocation();
		return this.getShortDescription() + " [" + this.getHitpoints() + "] is at " + location.getShortDescription();

	}

}
