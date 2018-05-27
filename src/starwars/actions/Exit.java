package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;

public class Exit extends SWAffordance {
	
	/**
	 * Constructor for the <code>Exit</code> class. Will initialize the message renderer, the target 
	 * @param theTarget a <code>SWEntity</code> from where the actor exits
	 * @param m the message renderer to display messages
	 */
	public Exit(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
	}

	/**
	 * Returns if or not this <code>Exit</code> can be performed by the <code>SWActor a</code>.
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
	 * The act() of Exit does not do anything by itself, because we should break the infinite while loop in the insideCrawler()
	 * which can only be done from inside the while loop. The way it break the loop is, it will check if the userDecision was to Exit
	 * and if it was then the while loop will be broken and the exit protocol will be initialised inside the insideCrawler() method of Sandcrawler
	 */
	@Override
	public void act(SWActor a) {
		return;
	}

	/**
	 * Returns a String describing this <code>Exit</code>, suitable for display to the user.
	 * 
	 * @author harsilpatel
	 * @return String comprising "Exit though " and theTargets descriptions.
	 */
	@Override
	public String getDescription() {
		return "Exit through " + this.target.getShortDescription();
	}
	
}
