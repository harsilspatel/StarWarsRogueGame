package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWActionInterface;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.entities.LightSaber;


/**
 * Command to use the force on entities.
 * 
 * In most ways identical to the attack class as they both serve the same function
 * 
 * This affordance is attached to all entities that the force can be applied to
 * 
 * @author David 
 */

public class Force extends SWAffordance implements SWActionInterface {
	
	/**
	 * Constructor for the <code>Force</code> class. Will initialize the <code>messageRenderer</code> and
	 * give <code>Force</code> a priority of 1 (lowest priority is 0).
	 * 
	 * @param theTarget the target the force is being used on
	 * @param m message renderer to display messages
	 */
	
	public Force(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);	
		priority = 1;
	}
	
	/**Same as the attack class
	 * 
	 */
	@Override
	public int getDuration() {
		return 1;
	}
	
	/**Same as the attack class
	 * 
	 */
	@Override
	public String getDescription() {
		return "use the force against " + this.target.getShortDescription();
	}
	
	/**
	 * Perform the <code>Force</code> command on an entity.
	 * <p>
	 * This method does not perform any damage (an force attack) if,
	 * <ul>
	 * 	<li>The target of the <code>Force</code> and the <code>SWActor a</code> are in the same <code>Team</code></li>
	 * 	<li>The <code>SWActor a</code> is holding an item without the <code>WEAPON Affordance</code></li>
	 * </ul>
	 * <p>
	 * else it would damage the entity attacked, tires the attacker, and blunts any weapon used for the attack.
	 * 
	 * TODO : check if the weapon has enough hitpoints and the attacker has enough energy before an attack.
	 * 
	 * Below is the same as the attack class
	 * @param 	a the <code>SWActor</code> who is attacking
	 * @pre 	this method should only be called if the <code>SWActor a</code> is alive
	 * @pre		a <code>Force</code> must not be performed on a dead <code>SWActor</code>
	 * @post	if a <code>SWActor</code>dies in an <code>Force</code> their <code>Force</code> affordance would be removed
	 * @see		starwars.SWActor#isDead()
	 * @see 	starwars.Team
	 */
	@Override
	public boolean canDo(SWActor a) {
		return true;
	}
	
	
	@Override
	public void act(SWActor a) {
		SWEntityInterface target = this.getTarget();
		boolean targetIsActor = target instanceof SWActor;
		SWActor targetActor = null;
		int energyForAttackWithWeapon = 1;
		
		if (targetIsActor) {
			targetActor = (SWActor) target;
		}
		
		if (targetIsActor && (a.getTeam() == targetActor.getTeam())) { 
			a.say("\t" + a.getShortDescription() + " says: Silly me! We're on the same team, " + a.getForce() + target.getShortDescription() + ". No harm done");
		}
		
		else if (a.isHumanControlled() 
			|| (targetIsActor && (a.getTeam() != targetActor.getTeam()))) { 
				
			a.say(a.getShortDescription() + " is attempting to use the force against " + target.getShortDescription() + "!");
						
			SWEntityInterface itemCarried = a.getItemCarried();
			//a force level of 60 or higher is needed to "attack" with a lightsaber
			//checking if the player is carrying an item AND if it's a lightsaber AND if the player's force level is equal or greater than 60
			if (a.getItemCarried() != null && a.getItemCarried() instanceof LightSaber && a.getForce()>=60) {
				if (itemCarried.hasCapability(Capability.WEAPON)) {
					target.takeDamage(itemCarried.getHitpoints() + 1); 
					a.takeDamage(energyForAttackWithWeapon); 
				}
			
			}
			//checking if the player is carrying an item AND if it's a lightsaber AND if the player's force level is less than 60
			//if the player's force level is too low, they can't "attack" with the lightsaber and a force push is used instead
			else if (a.getItemCarried() != null && a.getItemCarried() instanceof LightSaber && a.getForce()>=20 && a.getForce()<60) {
				target.takeDamage((a.getHitpoints()/20) + 5); //damage from a force push
				a.takeDamage(2*energyForAttackWithWeapon); 
				a.say(a.getShortDescription() + " your force level is too low to attack with a lightsaber!" + "\n" + "Luke uses a force push instead!");
			}
		    
		    //if the player is carrying a lightsaber item AND their force level is too low, they attack with their bare hands
		    else if(a.getItemCarried() != null && a.getItemCarried() instanceof LightSaber && a.getForce()<20){
				target.takeDamage((a.getHitpoints()/20) + 1); 
				a.takeDamage(2*energyForAttackWithWeapon);
				a.say(a.getShortDescription() + " your force level is too low to even use a force push! Luke attacks with his bare hands"
			}
		    
			//if the player is carrying an item AND it's not a lightsaber, they will use the weapon they're holding and attack with it
			else if (a.getItemCarried() != null && !(a.getItemCarried() instanceof LightSaber)){
					if(a.getItemCarried().hasCapability(Capability.WEAPON)){
						target.takeDamage(a.getItemCarried().getHitpoints() + 1);
						a.takeDamage(energyForAttackWithWeapon);
					}
					
				}
			
			  // attack with bare hands since the player's is not carrying an item
			else {
				target.takeDamage((a.getHitpoints()/20) + 1); 
				a.takeDamage(2*energyForAttackWithWeapon); 
			}
			
			if (a.isDead()) {
							
				a.setLongDescription(a.getLongDescription() + ", that died of exhaustion while attacking someone");
				
				a.removeAffordance(this);
				
			}
			
			if (this.getTarget().getHitpoints() <= 0) {  
				target.setLongDescription(target.getLongDescription() + ", that was killed in a fight");
				
				targetActor.removeAffordance(this);

			}
		}
		
	}
}
