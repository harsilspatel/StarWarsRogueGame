package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActionInterface;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.SWLegend;

public class Train extends SWAffordance implements SWActionInterface {
	
	/**
	 * Constructor for the <code>Train</code> class.
	 * 
	 * @param theTarget the target being trained
	 * @param m message renderer to display messages
	 */
	public Train(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns the time is takes to perform this <code>Train</code> action.
	 * 
	 * @return The duration of the Train action. Currently hard coded to return 1.
	 */
	@Override
	public int getDuration() {
		return 1;
	}
	
	/**
	 * A String describing what this <code>Train</code> action will do, suitable for display on a user interface
	 * 
	 * @return String comprising "train " and the short description of the target of this <code>Affordance</code>
	 */
	@Override
	public String getDescription() {
		return "train " + this.target.getShortDescription();
	}
	
	
	/**
	 * Returns if or not a <code>SWActor a</code> can perform a <code>Train</code> command.
	 * <p>
	 * This method returns true if and only if <code>a</code> is not dead.
	 * <p>
	 * 
	 * @author 	Harsil
	 * @param 	a the <code>SWActor</code> who will train
	 * @return 	true if and only if <code>a</code> is not dead, false otherwise.
	 * @see 	{@link starwars.SWActor#isDead()}
	 */
	@Override
	public boolean canDo(SWActor a) {
		return !a.isDead();
	}
	
	/**
	 * Perform the <code>Train</code> command on an entity.
	 * Train is performed if the teacher and disciple start from the same location
	 * <p>
	 * This method trains the actor and increases it's force by 20 points.
	 * So, for eg. if actor's force is 40, then he needs to be in the same spot as the teacher 3 times to increase it to 100.
	 * 
	 * @author 	Harsil
	 * @param 	a the <code>SWActor</code> who will be training
	 * @pre 		this method should only be called if the <code>SWActor a</code> is alive
	 * @post		if a <code>SWActor</code>dies in an <code>Attack</code> their <code>Attack</code> affordance would be removed
	 * @see		starwars.SWActor#isDead()
	 */
	public void act(SWActor a) {
		if (a instanceof SWLegend) {
			SWActor disciple = (SWActor) this.getTarget();
			disciple.setForce(disciple.getForce() + 20);
			if (disciple.getForce() < 100) {
				a.say(a.getShortDescription() + "'s training improved " + disciple.getShortDescription() + "'s force");
			}
			else {
				a.say(a.getShortDescription() + " has helped " + disciple.getShortDescription() + " master the force!");
				((SWLegend) a).trained(disciple);
			}
			
		} else {
			a.say("Uh oh!, " + a.getShortDescription() + " has to be a legend to train!");
		}
		
	}
}
