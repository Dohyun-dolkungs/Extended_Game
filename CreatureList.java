/*
*	A CreatureList is a partially full array of Creature objects
*/
public class CreatureList{
	// Current array state
	private Creature[] creatures;
	private int currentIndex = 0; // current number of creatures, also is the size
	private int maxSize = 100; // maximum capacity. Note we are not boundary checking for this. 

	// Basic constructor simply creates the array. currentIndex should already be 0 but we are being explicit. 
	public CreatureList(){
		creatures = new Creature[maxSize];
		currentIndex = 0;
	}

	// Return the count of creatures in the array
	public int getCount(){
		return currentIndex;
	}

	// Return the creature at a given index
	public Creature getCreature(int index){
		// Error / Boundary checking
		if(index < 0 || index >= currentIndex){
			System.out.println("Warning! CreatureList has not creature at index " + index);
		}
		if(creatures[index] == null){
			System.out.println("Warning! Creature accessed at " + index + " was found to be null!");
		}

		return creatures[index];
	}

	// Add the next item to the array
	public void addCreature(Creature newCreature){
		if(newCreature == null){
			System.out.println("Warning: Null creatured added to the creatureList!");
		}

		creatures[currentIndex] = newCreature;
		currentIndex ++; // add to array then update
	}

	// Add the contents of a second CreatureList to this CreatureList
	public void addCreatures(CreatureList newList){
		for(int i = 0; i < newList.getCount(); i++){
			// handles blank entries
			Creature temp = newList.getCreature(i);
			if( temp != null){
				// Add the creature 
				// Note this is the local list (aka this.addCreature)
				addCreature(newList.getCreature(i));
			}
		}
	}

	// Removes a single creature from the list and bumps the rest up a spot to fill the empty index
	public void removeCreature(Creature toRemove){
		// empty list case
		if( currentIndex < 1){
			return;
		}else{
			// We will have a blank spot in the array
			// Iterate backwards so we don't mess up the next indexes
			for(int i = currentIndex-1; i >= 0; i--){
				// find the object to remove
				if(toRemove == creatures[i]){
					// If we are removing anything but the last entry, copy the objects one index over to fill the space. 
					if(i < currentIndex-1){
						// if not the last entry, copy the rest over to fill in the blank spot
						// starting at the empty spot, copy over it. 
						for(int j = i; j<currentIndex; j++){
							// copy the creatures up one index after the spot where we removed the object
							creatures[j] = creatures[j+1]; 
						}
						// There should only be one creature that is this object
						// remove this return to finish iterating the list	
					}					

					creatures[currentIndex-1] = null; // remove the last entry (which is a copy now)
					currentIndex--; // one less in the list
					return;
				}
			}

		}

		
	}

	// Print the whole item list as a String
	// formatting and returning all the strings
	public String toString(){

		String list = "[";		

		if(currentIndex == 0){
			return "[EMPTY]";
		}

		// Note you are using current Index not max here. 
		for(int i = 0; i < currentIndex-1; i++){
			list += creatures[i].toString() + ", ";
		}

		// handle the end of the list
		list+= creatures[currentIndex-1].toString() + "]";

		return list;
	}
	
}