Leave affordance

Issue: There is currently no way for an actor to put down the item the actor is holding. That is, the actor cannot pick up anything else.

Design choice:
Create a new Leave class which will complement the existing Take class, as, all the enitities that an Actor can "Take" (i.e. entities that have Take affordance), should also have a "Leave" affordance for actor to drop it. The Actor will drop the entity at the same location.

Advantage:
Having another class will make it more extensible, for eg. we can implement a Throw class where the Actor "throws" the Entity and it drops at different location.
Disadvantage:
Implementing a whole class just to undo what "Take" does will make the project more complex, as that same functionality can also be achieved by a function.


Droids

Design Choice:
- A SWActor variable that will let the droid know it’s owner
- Implementing a FindActor behaviour, which will take SWActor as a parameter (we will pass-in the owner for droids), that will tell the droid where to “Move” to, it will analyse the neighbouring locations to see if owner is present. If it is then return the Direction of it’s owner, if not then command droid to move in a random direction till it reaches the grid’s end.
- we will have two boolean flags, in order to:
    -  check if owner is dead 
    - see if droid is immobile or not

Advantages:
A highly flexible design that can be extended to make game more interesting. For example, Ben Kenobi can use Find Actor to find Luke to train him or TuskenRaiders can be programmed to find Ben to engage in fight with him!

Disadvantages:
At times, it might be difficult for player to keep track of SWActors and the actors they are finding.
FindActor will be a dependency for SWActor, which can be prone to errors
