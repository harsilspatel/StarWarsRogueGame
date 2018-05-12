package starwars;

import java.util.HashSet;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;

/**
 * This class represents "legends" - major characters - in the Star Wars universe.  
 * They use a variation of the Singleton
 * pattern to ensure that only ONE of each legend can exist.
 * 
 * Subclasses are intended to contain a static instance which represents the one
 * and only instance of the subclass.  
 * 
 * Subclasses should implement their own "getLegendClass" method that returns 
 * the single instance. There is no abstract method for this to avoid an 
 * unnecessary downcast.
 * 
 * To prevent SWLegends acting until intended, this abstract class implements
 * an API for activating them when getInstance is called.
 * 
 * Rather than implement act() like regular SWActors, Legends should implement
 * legendAct().  
 * 
 * @author Robert Merkel
 *
 */

	
public abstract class SWLegend extends SWActor {

	private boolean isActivated;

	/**An arrayList that represents the actors that this actor will be training if they're in the same location*/
	private HashSet<SWActor> canTrain;
	
	/** 
	 * Protected constructor to prevent random other code from creating 
	 * SWLegends or their descendents.
	 * @param team
	 * @param hitpoints
	 * @param m
	 * @param world
	 */
	
	protected SWLegend(Team team, int hitpoints, MessageRenderer m, SWWorld world) {
		super(team, hitpoints, m, world);
		isActivated = false;
		/**
		 * Initializing the canTrain HashSet
		 */
		this.canTrain = new HashSet<SWActor>();
	}
	
	/**
	 * Adds <code>a</code> to the Set so that actor can be trained when in the same location as the teacher
	 * @param a Actor to be trained
	 */
	public void trainActor(SWActor a) {
		this.canTrain.add(a);
	}
	
	/**
	 * Removes <code>a</code> from the Set after the actor is trained
	 * @param a Actor that has been trained
	 */
	public void trained(SWActor a) {
		this.canTrain.remove(a);
	}
	
	/**
	 * a hashSet of actors is returned that are yet to be trained
	 * @return a hashSet of actors that are to be trained
	 */
	public HashSet<SWActor> getDisciples() {
		return this.canTrain;
	}
	
	protected boolean isActive() {
		return isActivated;
	}
	
	protected void activate() {
		isActivated = true;
	}
	
	@Override
	public void act() {
		if (isActive()) {
			this.legendAct();
		}
		return;
	}

	protected abstract void legendAct();
}
