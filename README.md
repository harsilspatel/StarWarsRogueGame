# FIT2099 Assignment

The objective of this assignment was to work in pairs and extend the existing software system abiding by the object-oriented design principles. We were required to collaborate using git and design well written visual forms, using standard UML diagrams.

## Student details 

### Harsil Patel
- Student email: hpat0003@student.monash.edu
- Student id: 28334825

### David Bui
- Student email: dbui0001@student.monash.edu
- Student id: 28891465


## Gameplay
### Entities
#### Blaster
A blaster is a weapon with hitpoints 100 and is represented by ```b```. 

#### Canteen
A canteen, denoted by ```c```, can be used to contain water. It can be filled at a ```Reservoir```, or any other Entity that has a ```Dip``` affordance.

#### Door
A door, represented by ```d```, is an entity with ```Enter``` and ```Exit``` affordances so that the player (controlling Luke) can enter and exit other entities (```Sandcrawler``` for example)

#### Grenade
A grenade, which is identified by ```g``` on the text based user interface, when thrown explodes violently, doing damage to both actors and other entities, in “rings” of differing severity. It causes damages as follows:
- Entities in the location where the grenade is thrown lose 20 hitpoints.
- Entities in locations that can be reached in one step from the location where the grenade is thrown lose 10 points.
- Entities in locations that can be reached in one step from the location where the grenade is thrown lose 5 points.
- The actor that throws the grenade is not affected.

#### Lightsaber
A lightsaber, marked by ```†``` on the interface, can be picked up by any entity, but only those entities with a lot of ```Force``` ability can wield one and use it as a weapon.

#### Reservoir
Water reserviors are denoted by ```W``` and they can be dipped into to fill fillable entities (such as ```Canteens```.  They are assumed to have infinite capacity.


## GIF 🎞

<img src="screencaptures/gameplay.gif">


</br>

## Credits
Assignment base by [Dr. David Squire](http://users.monash.edu/~davids/)


#### Plagiarism Notice

The code in this repository is not endorsed by Monash University. Please note that copying the code for unit assessments without referencing will result in a breach in Academic Integrity Policy and that the author @harsilspatel will not be held responsible for these breaches. For more information please visit https://www.monash.edu/students/academic/policies/academic-integrity
