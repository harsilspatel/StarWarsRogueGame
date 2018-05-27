package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.entities.actors.Sandcrawler;

public class Enter extends SWAffordance {

	/**
	 * Constructor for the <code>Enter</code> class. Will initialize the message renderer, the target 
	 * @param theTarget a <code>SWEntity</code> in which the actor Enters
	 * @param m the message renderer to display messages
	 */
	public Enter(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns if or not this <code>Enter</code> can be performed by the <code>SWActor a</code>.
	 * <p>
	 * This method returns true if and only if <code>a</code> is not dead
	 *  @author harsilpatel
	 * @param 	a the <code>SWActor</code> being queried
	 * @return 	true if the <code>SWActor</code> is alive, false otherwise
	 */
	@Override
	public boolean canDo(SWActor a) {
		return !a.isDead();
	}

	/**
	 * This method checks if the player has enough force, if it does, it will let the player go inside the crawler
	 * else it will displays a message describing the situation
	 */
	@Override
	public void act(SWActor a) {
		if (a.getForce() >= 40) {  //if force is enough, then "a" canDo the Action "Enter"
			((Sandcrawler) this.target).insideCrawler(a);	
		} else { //if force is not enough, display a message
			a.say("Uh-oh! " + a.getShortDescription() + "'s force is insufficient to enter " + this.target.getShortDescription() + "!");
		}
	}

	/**
	 * Returns a String describing this <code>Enter</code>, suitable for display to the user.
	 * 
	 * @author harsilpatel
	 * @return String comprising "Enter " and theTargets descriptions.
	 */
	@Override
	public String getDescription() {
		return "Enter " + this.target.getShortDescription();
	}
}
