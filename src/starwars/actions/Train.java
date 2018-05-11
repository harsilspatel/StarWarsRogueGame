package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActionInterface;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.SWLegend;

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
