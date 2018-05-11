package starwars.actions;

import java.util.List;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActionInterface;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.SWLegend;
import starwars.SWLocation;
import starwars.SWWorld;

public class Train extends SWAffordance implements SWActionInterface {
	
	public Train(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int getDuration() {
		return 1;
	}
	
	@Override
	public String getDescription() {
		return "train " + this.target.getShortDescription();
	}
	
	@Override
	public boolean canDo(SWActor a) {
		return true;
	}
	
	public void act(SWActor a) {
		if (a instanceof SWLegend) {
			SWActor disciple = (SWActor) this.getTarget();
			disciple.setForce(100);
			a.say(a.getShortDescription() + " trained " + disciple.getShortDescription());
		} else {
			a.say("Uh oh!, " + a.getShortDescription() + " has to be a legend to train!");
		}
		
	}
}
