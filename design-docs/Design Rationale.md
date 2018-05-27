# Assignment 1 -- Team **dbuihpat**

The overall design choices are made keeping in mind the extensibility of the game.
Below listed the details of design choices for each project requirement.

## Leave affordance

**Design choice**
1. Create a new `Leave` class which will complement the existing `Take` class
2. All the entities that have `Take` affordance should have a `Leave` affordance in their initialisers

**Advantages**
- Having another class will make it more extensible, for eg. we can implement a `Throw` class where the `Actor` *throws* the `Entity` and it drops at different `Location`.
- It'll be easy to implement `Leave` class as it has to do opposite of whatever `Take` does.

**Disadvantages**
- Implementing a whole class, as compared to a method, just to undo what `Take` does will make the project more complex.
- It will increase the dependencies and there will be code to maintain.

## Ben Kenobi
**Design choice**
1. Implement an ArrayList in `SWLegend` as legends can train `SWActor`. The arraylist will make it extendable for SWLegend (Ben in this case) to train other players, so for example, if we wish to include another player Han Solo, we can add him to Ben's list and Ben will train him when they're in the same spot.
2. Ben will check if the if any of his disciple (actor to be trained) is in the same location, if so, Ben will `Train` the actor.
3. Each Training will increase Actor's force level by 20 points, so if Actor wants his force to increase from 40 to 100, he will have to be in same location as Ben 3 times.

**Advantages**
- Highly flexible as if we need Ben to train some other Actor, we just have to include that Actor’s name in Ben’s ArrayList. 
- Implementing a Train class separately will also enable some other player to perform `Train` action. For example, if Luke has mastered the Force, he can train Princess Leia.

**Disadvantages**
- Increases dependencies and complexity of the game. It is not the most efficient solution, as implementing a `train(Luke)` function in BenKenobi would seem like the most simple and  method.


## Droids
**Design choice**

1. For simplilcity we will assume that each droid can have only one owner and each owner can own no more than one droid. Owner will be set while initializing the droid.
2. The droids will belong to team `Neutral`
4. Implement a function to check if droid is on `badlands`, if it is, decrease it's health by 10 units.
5. Implementing a `FindActor` behaviour, which will take `SWActor` as a parameter (we will pass-in the owner for droids), that will tell the droid where to move to. It will do so by analysing the neighbouring locations to see if owner is present. If it is then return the `Direction` of it’s owner, if not then command droid to move in a random `Direction`.
6. If droid is moving in random direction, it will move in that direction till it cannot move further (unless it finds its owner on the way!)
7. We will have two boolean flags:
    - `hasOwner` -- to tell if owner is dead 
    - `isImmobile` -- to tell if droid has health or is immobile
8. Droid will only move if it has an owner and if it is not immobile.


**Advantages**
- A highly flexible design that can be extended to make game more interesting. For example, Ben Kenobi can use Find Actor to find Luke to train him or TuskenRaiders can be programmed to find Ben to engage in a fight with him!

**Disadvantages**

- Adding new class will improve the overall complexity. Furthermore, `FindActor` will be a dependency for actors, which can be prone to errors.
-  At times, it might be difficult for player to keep track of `SWActors` and the actors they are finding.


## Force
**Design choice**
1. For simplicity purposes, the force will be integrated into the `Attack` class
2. An if...else statement will be implemented to check the force level of whoever is attempting to use the force action.
3. A second condition will be if the item carried is an instance of the `Lightsaber` class
4. A new attack message will be displayed based on whether or not the user was able to attack with the lightsaber.
5. The accessors and mutators for the force will be implemented in the `SWActor` class. This is because any actor can take a lightsaber but will not always be able to use it
6. This allows for both Ben and Luke to be able have their force levels set


**Advantages**
- This makes for less coding as a new class doesn't have to be implemented.
- A check simplifies the force action where we simply need to check the character's current force level 

**Disadvantages**
- Requires modification to the attack class
- Have to modify the attack class to avoid use of the lightsaber when the "attack" option is selected


## Lightsaber
**Design Choice**
1. The attack class will only reference to any instances of Class `Lightsaber`.
2. It will be included in the if...else statement (in the attack class) as a condition alongside the force level condition. 

**Advantages**
- Allows for only competent force users to attack with a lightsaber.
- No modifications to the lightsaber class are necessary.
- Use of the lightsaber as a condition is only a small addition. 

**Disadvantages**
- Adds extra steps to allowing attacks to be made by lightsaber wielders. 


## Grenade
**Design Choice**
1. Basic class, practically the same as other items.
2. Direct damage is the same as its hitpoints.

**Advantages**
- Simple implementation .

## Reservoir
**Design Choice**
1. To make non-actors attackable, the attack class needs to be changed so that the target can be a non-actor.
2. In future this would allow for any object to be attacked (given it has the attack affordance).

**Advantages**
- Allows for any entity with the attack affordance to be attacked.
- In this case, it only deals with attacking a reservoir.

**Disadvantages**
- Additional conditions must be added to deal with non-actors
- Some changes necessary to variables and if statements

## Sandcrawler

The idea is, sandcrawler will capture the droid, then it's owner will go into the crawler (only if he has enough Force) for the rescue. The owner will go to the droid's location inside the `crawlerWorld`. As implemented in previous tasks, the droid will start following the owner if owner is in it's neightbouring location. The owner will then take the droid to the exit door and both will leave the sandcrawler.

**Design Choice**
1. Implemented a `Sandcrawler` class which will have a mini interactable world where-in the Player can goto to and save the droid.
2. Crawler will capture the droid if they're on the same location. (It will only capture one droid; will not capture another droid will previous one is released)
3. Captured droid will be allowed to move inside the crawler
4. When the Player enters the `crawlerWorld` the main-world will still be ticking
5. Implemented `Enter` affordance for the Sandcrawler.
6. Implemented a `Door` entity and `Exit` affordance so the Player can leave the crawler.
7. Reusing SWWorld in Sandcrawler.

**Advantages**
- The Sandcrawler will have a swworld which can be made more interative by placing more entities, for eg. Jawas can be placed to fight Luke.
- Reusing the SWWorld code repects the DRY principle, easier to maintain less code.

**Disadvantages**
- The code gets extensively complex and sandcrawler gets dependent on many other classes.
- Understanding the implementation of Sandcrawler can be challenging.

