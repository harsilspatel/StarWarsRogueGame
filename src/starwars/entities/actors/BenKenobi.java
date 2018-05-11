package starwars.entities.actors;

import java.util.List;

import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWEntityInterface;
import starwars.SWLegend;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.Team;
import starwars.actions.Move;
import starwars.actions.Train;
import starwars.entities.LightSaber;
import starwars.entities.actors.behaviors.AttackInformation;
import starwars.entities.actors.behaviors.AttackNeighbours;
import starwars.entities.actors.behaviors.Patrol;

/**
 * Ben (aka Obe-Wan) Kenobi.  
 * 
 * At this stage, he's an extremely strong critter with a <code>Lightsaber</code>
 * who wanders around in a fixed pattern and neatly slices any Actor not on his
 * team with his lightsaber.
 * 
 * Note that you can only create ONE Ben, like all SWLegends.
 * @author rober_000
 *
 */
public class BenKenobi extends SWLegend {

	private static BenKenobi ben = null; // yes, it is OK to return the static instance!
	private Patrol path;
	
	private BenKenobi(MessageRenderer m, SWWorld world, Direction [] moves) {
		super(Team.GOOD, 1000, m, world);
		path = new Patrol(moves);
		this.setShortDescription("Ben Kenobi");
		this.setLongDescription("Ben Kenobi, an old man who has perhaps seen too much");
		LightSaber bensweapon = new LightSaber(m);
		setItemCarried(bensweapon);
		
		//setting Ben's force level
		this.setForce(100);
	}

	public static BenKenobi getBenKenobi(MessageRenderer m, SWWorld world, Direction [] moves) {
		ben = new BenKenobi(m, world, moves);
		ben.activate();
		return ben;
	}
	
	@Override
	protected void legendAct() {

		if(isDead()) {
			return;
		}
		
		SWLocation loc = SWWorld.getEntitymanager().whereIs(this);
		List<SWEntityInterface> neighbours = SWWorld.getEntitymanager().contents(loc);
		for(SWActor disciple: this.getDisciples()) {
			if (neighbours.contains(disciple) && disciple.getForce() != 100) {
				Train train = new Train(disciple, messageRenderer);
				scheduler.schedule(train, this, 1);
				this.trained(disciple);
				break;
			}
		}
		
		AttackInformation attack;
		attack = AttackNeighbours.attackLocals(ben,  ben.world, true, true);
		
		if (attack != null) {
			say(getShortDescription() + " suddenly looks sprightly and attacks " +
		attack.entity.getShortDescription());
			scheduler.schedule(attack.affordance, ben, 1);
		}
		else {
			Direction newdirection = path.getNext();
			say(getShortDescription() + " moves " + newdirection);
			Move myMove = new Move(newdirection, messageRenderer, world);

			scheduler.schedule(myMove, this, 1);
		}
	}

}
