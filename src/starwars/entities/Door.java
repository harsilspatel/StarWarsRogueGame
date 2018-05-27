package starwars.entities;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWEntity;
import starwars.actions.Exit;

public class Door extends SWEntity {

	private String doorLable;
	public Door(MessageRenderer m, String doorLable) {
		super(m);
		this.doorLable = doorLable;
		this.addAffordance(new Exit(this, m));
	}
	
	public String getSymbol() {
		return "d";
	}
	
	@Override 
	public String getShortDescription() {
		return this.doorLable;
	}
	
	@Override
	public String getLongDescription () {
		return this.getShortDescription();
	}

}
