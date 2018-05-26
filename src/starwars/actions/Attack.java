package starwars.actions;

import edu.monash.fit2099.simulator.matter.Entity;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWActionInterface;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.entities.Blaster;
import starwars.entities.Grenade;
import starwars.entities.LightSaber;
import starwars.entities.Reservoir;


/**
 * Command to attack entities.
 * 
 * This affordance is attached to all attackable entities
 * 
 * @author David.Squire@monash.edu (dsquire)
 */
/*
 * Change log
 * 2017/02/03	Fixed the bug where the an actor could attack another actor in the same team (asel)
 * 2017/02/08	Attack given a priority of 1 in constructor (asel)
 */
public class Attack extends SWAffordance implements SWActionInterface {

	
	/**
	 * Constructor for the <code>Attack</code> class. Will initialize the <code>messageRenderer</code> and
	 * give <code>Attack</code> a priority of 1 (lowest priority is 0).
	 * 
	 * @param theTarget the target being attacked
	 * @param m message renderer to display messages
	 */
	public Attack(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);	
		priority = 1;
	}


	/**
	 * Returns the time is takes to perform this <code>Attack</code> action.
	 * 
	 * @return The duration of the Attack action. Currently hard coded to return 1.
	 */
	@Override
	public int getDuration() {
		return 1;
	}

	
	/**
	 * A String describing what this <code>Attack</code> action will do, suitable for display on a user interface
	 * 
	 * @return String comprising "attack " and the short description of the target of this <code>Affordance</code>
	 */
	@Override
	public String getDescription() {
		return "attack " + this.target.getShortDescription();
	}


	/**
	 * Determine whether a particular <code>SWActor a</code> can attack the target.
	 * 
	 * @author 	dsquire
	 * @param 	a the <code>SWActor</code> being queried
	 * @return 	true any <code>SWActor</code> can always try an attack, it just won't do much 
	 * 			good unless this <code>SWActor a</code> has a suitable weapon.
	 */
	@Override
	public boolean canDo(SWActor a) {
		return true;
	}

	
	/**
	 * Perform the <code>Attack</code> command on an entity.
	 * <p>
	 * This method does not perform any damage (an attack) if,
	 * <ul>
	 * 	<li>The target of the <code>Attack</code> and the <code>SWActor a</code> are in the same <code>Team</code></li>
	 * 	<li>The <code>SWActor a</code> is holding an item without the <code>WEAPON Affordance</code></li>
	 * </ul>
	 * <p>
	 * else it would damage the entity attacked, tires the attacker, and blunts any weapon used for the attack.
	 * 
	 * TODO : check if the weapon has enough hitpoints and the attacker has enough energy before an attack.
	 * 
	 * @author 	dsquire -  adapted from the equivalent class in the old Eiffel version
	 * @author 	Asel - bug fixes.
	 * @param 	a the <code>SWActor</code> who is attacking
	 * @pre 	this method should only be called if the <code>SWActor a</code> is alive
	 * @pre		an <code>Attack</code> must not be performed on a dead <code>SWActor</code>
	 * @post	if a <code>SWActor</code>dies in an <code>Attack</code> their <code>Attack</code> affordance would be removed
	 * @see		starwars.SWActor#isDead()
	 * @see 	starwars.Team
	 */
	@Override
	public void act(SWActor a) {
		SWEntityInterface target = this.getTarget();
		boolean targetIsEntity = target instanceof Entity; //changed to instances of entity since non actors can also be attacked
		Entity targetEntity = null;
		int energyForAttackWithWeapon = 1;//the amount of energy required to attack with a weapon
		
		if (targetIsEntity) {
			targetEntity = (Entity) target;
		}
					
		//downcast targetEntity to SWActor to use the getTeam() method
		//this if statement is only for attacking actors
		if (targetIsEntity && targetEntity instanceof SWActor && a.getTeam() == ((SWActor) targetEntity).getTeam()) { //don't attack SWActors in the same team
			a.say("\t" + a.getShortDescription() + " says: Silly me! We're on the same team, " + target.getShortDescription() + ". No harm done");
		}
		else if (a.isHumanControlled() // a human-controlled player can attack anyone
			|| (targetIsEntity && a.getTeam() != ((SWActor) targetEntity).getTeam()) || targetEntity instanceof Reservoir) {  // others will only attack actors on different teams
				
			a.say(a.getShortDescription() + " is attacking " + target.getShortDescription() + "!");
			
			SWEntityInterface itemCarried = a.getItemCarried();
			if (itemCarried != null && itemCarried.hasCapability(Capability.WEAPON)) {//if the actor is carrying an item and if it's a weapon
				
	        	//separate if statements since each of these items have different results
				//e.g. take no damage, are destroyed completely upon use or take 1 damage
				
					if(itemCarried instanceof Blaster){
						target.takeDamage(itemCarried.getHitpoints() + 1); 
						a.takeDamage(energyForAttackWithWeapon); 
						itemCarried.takeDamage(1); // only the blaster takes 1 damage out of all weapons
					}
					
					//if item carried is grenade, the damage the grenade takes is it's own hitpoints since it can only be used once
					 if(itemCarried instanceof Grenade){
						target.takeDamage(itemCarried.getHitpoints()); 
						a.takeDamage(energyForAttackWithWeapon); 
						itemCarried.takeDamage(itemCarried.getHitpoints()); //grenade loses its weapon capability as it is a one time use
						a.setItemCarried(null); //player is no longer holding an item
					}
					
					//if item carried is saber, then check if force is equal to or greater than 80 to be able to wield it
					 if(itemCarried instanceof LightSaber && a.getForce() >= 80){
						target.takeDamage(itemCarried.getHitpoints());
						a.takeDamage(energyForAttackWithWeapon);
						//lightsaber is indestructible
					}
					
				else {//an attack with a none weapon
					if (targetEntity instanceof SWActor) { //non actor entities can't speak
						String itemDescription = itemCarried.getShortDescription();
						
						//attempt of attack with a lightsaber when the actor isn't powerful enough with the force
						if (itemCarried instanceof LightSaber) {
							itemDescription = "an unwielded saber";
						}
						targetEntity.say("\t" + targetActor.getShortDescription()
								+ " is amused by " + a.getShortDescription()
								+ "'s attempted attack with "
								+ itemDescription);
					}
				} 
			}
			else { // attack with bare hands
				target.takeDamage((a.getHitpoints()/20) + 1); // a bare-handed attack doesn't do much damage.
				a.takeDamage(2*energyForAttackWithWeapon); // actor uses energy. It's twice as tiring as using a weapon
			}
			
			
			//After the attack
			
			if (a.isDead()) {//the actor who attacked is dead after the attack
							
				a.setLongDescription(a.getLongDescription() + ", that died of exhaustion while attacking someone");
				
				//remove the attack affordance of the dead actor so it can no longer be attacked
				a.removeAffordance(this);
				
				
			}
			if (this.getTarget().getHitpoints() <= 0) {  // can't use isDead(), as we don't know that the target is an actor
				target.setLongDescription(target.getLongDescription() + ", was destroyed");
							
				//remove the attack affordance of the dead actor so it can no longer be attacked
				targetEntity.removeAffordance(this);

				
			}
		} // not game player and different teams
		
	}
}
