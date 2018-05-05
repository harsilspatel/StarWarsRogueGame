package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWAction;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;

public class Leave extends SWAffordance {
	
	/**
	 * Constructor for the <code>Leave</code> Class. Will initialize the message renderer, the target and 
	 * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
	 * 
	 * @param theTarget a <code>SWEntity</code> that is being dropped
	 * @param m the message renderer to display messages
	 */
	public Leave(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		priority = 1;
	}
	
	/**
	 * Returns if or not this <code>Leave</code> can be performed by the <code>SWActor a</code>.
	 * <p>
	 * This method returns true if and only if <code>a</code> is carrying any item already.
	 *  
	 * @author 	Harsil
	 * @param 	a the <code>SWActor</code> being queried
	 * @return 	true if the <code>SWActor</code> is can leave this item, false otherwise
	 * @see		{@link starwars.SWActor#getItemCarried()}
	 */
	@Override
	public boolean canDo(SWActor a) {
		return a.getItemCarried()!=null;
	}
	
	/**
	 * Perform the <code>Leave</code> action by setting the item carried by the <code>SWActor</code> to the <code>null</code> (
	 * the <code>SWActor a</code>'s item dropped would be the target of this <code>Leave</code>).
	 * <p>
	 * This method should only be called if the <code>SWActor a</code> is alive.
	 * 
	 * @author 	Harsil
	 * @param 	a the <code>SWActor</code> that is leaving the target
	 * @see 		{@link #theTarget}
	 * @see		{@link starwars.SWActor#isDead()}
	 */
	@Override
	public void act(SWActor a) {
		if (target instanceof SWEntityInterface) {
			SWEntityInterface theItem = (SWEntityInterface) target;
			a.setItemCarried(null);
			SWAction.getEntitymanager().setLocation(theItem, SWAction.getEntitymanager().whereIs(a));//remove the target from the entity manager since it's now held by the SWActor
			
			//remove the leave affordance
			target.removeAffordance(this);
			//add the take affordance
			target.addAffordance(new Take(theItem, messageRenderer));
		}
	}
	
	/**
	 * A String describing what this action will do, suitable for display in a user interface
	 * 
	 * @author Harsil
	 * @return String comprising "leave " and the short description of the target of this <code>Leave</code>
	 */
	@Override
	public String getDescription() {
		return "leave " + target.getShortDescription();
	}
	
}
