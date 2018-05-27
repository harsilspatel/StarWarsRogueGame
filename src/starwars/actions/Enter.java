package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.entities.actors.Sandcrawler;

public class Enter extends SWAffordance {

	public Enter(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canDo(SWActor a) {
		return !a.isDead();
	}

	@Override
	public void act(SWActor a) {
		if (a.getForce() >= 40) {  //if force is enough, then "a" canDo the Action "Enter"
			((Sandcrawler) this.target).enterCrawler(a);	
		} else { //if force is not enough, display a message
			a.say("Uh-oh! " + a.getShortDescription() + "'s force is insufficient to enter " + this.target.getShortDescription() + "!");
		}
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Enter " + this.target.getShortDescription();
	}
	
}
