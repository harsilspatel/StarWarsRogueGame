Leave affordance

Issue: There is currently no way for an actor to put down the item the actor is holding. That is, the actor cannot pick up anything else.

Design choice:
Create a new Leave class which will complement the existing Take class, as, all the enitities that an Actor can "Take" (i.e. entities that have Take affordance), should also have a "Leave" affordance for actor to drop it. The Actor will drop the entity at the same location.

Advantage:
Having another class will make it more extensible, for eg. we can implement a Throw class where the Actor "throws" the Entity and it drops at different location.
Disadvantage:
Implementing a whole class just to undo what "Take" does will make the project more complex, as that same functionality can also be achieved by a function.