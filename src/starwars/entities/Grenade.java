package starwars.entities;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWEntity;
import starwars.actions.Take;

 
public class Grenade extends SWEntity {
	
	public Grenade(MessageRenderer m) {
		super(m);
		
		this.shortDescription = "a grenade";
		this.longDescription = "A small dangerous grenade";
		this.hitpoints = 20; 
		
		this.addAffordance(new Take(this, m));		
		this.capabilities.add(Capability.WEAPON);   
	}
	

	public String getSymbol() {
		return "g"; 
	}

	@Override
	public void takeDamage(int damage) {
		super.takeDamage(damage);
		
			this.shortDescription = "a used grenade";
			this.longDescription  = "A grenade that can no longer be used";			
			this.capabilities.remove(Capability.WEAPON);
	}
	
}
