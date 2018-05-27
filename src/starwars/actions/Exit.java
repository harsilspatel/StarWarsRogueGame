package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;

public class Exit extends SWAffordance {

	public Exit(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canDo(SWActor a) {
		return !a.isDead();
	}

	@Override
	public void act(SWActor a) {
		int x = 3;
	}

	@Override
	public String getDescription() {
		return "Exit through " + this.target.getShortDescription();
	}
	
}
