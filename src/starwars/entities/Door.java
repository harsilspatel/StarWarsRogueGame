package starwars.entities;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWEntity;
import starwars.actions.Exit;

public class Door extends SWEntity {
	
	/**
	 * Constructor for the <code>Door</code> class. This constructor will,
	 * <ul>
	 * 	<li>Initialize the message renderer for the <code>Door</code></li>
	 * 	<li>Add a <code>Exit</code> affordance to this <code>Door</code> so Player can leave thru the Door</li> 
	 * </ul>
	 * 
	 * @param m <code>MessageRenderer</code> to display messages.
	 * @param doorLabel a label to the door
	 * @see 	{@link starwars.actions.Exit} 
	 */
	private String doorLable;
	public Door(MessageRenderer m, String doorLable) {
		super(m);
		this.doorLable = doorLable;
		this.addAffordance(new Exit(this, m));
	}
	
	/**
	 * This method returns the symbol of the <code>Door</code> to "d"
	 * @return the symbol of the Door "d"
	 */
	public String getSymbol() {
		return "d";
	}
	
	/**
	 * This method returns the short door description
	 * @return the lable as the description
	 */
	@Override 
	public String getShortDescription() {
		return this.doorLable;
	}
	
	/**
	 * This method returns the long door description
	 * @return the lable as the description
	 */
	@Override
	public String getLongDescription () {
		return this.getShortDescription();
	}

}
