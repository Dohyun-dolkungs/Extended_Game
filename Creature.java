
/*
*	Creature Class
* 	A creature represents a single adversary on the GameBoard
* 	The player can interact with a Creature by stepping on the same Tile
*	Creatures 
*/
public class Creature{

	// *** Stats ***
	// The strength of the creature. This will determine the Name
	private int difficulty;
	// Name of the creature is randomly generated based on the difficulty
	private String creatureName;

	// *** Current Location State ***
	// The current tile the creature is currently on. 
	private Tile currentTile;

	// *** DEBUGGING OUTPUT FLAG ***
	private boolean DEBUG_MODE = false;
	
	// Constructor that accepts a single difficulty value, which is used to set the name. 
	public Creature(int newDifficulty){
		difficulty = newDifficulty;
		creatureName = GameData.getMonsterNameByDifficulty(newDifficulty);
	}

	// Assigns the creatures reference to the current tile but
	// also assigns the tiles reference to the current creature. 
	public void setTile(Tile currentTile){
		// assign the tile to the creature. 
		this.currentTile = currentTile;
		// Let the tile know the creature is on it
		currentTile.setCreature(this);
	}

	// Move the player to the next open tile
	public void moveCreature(){
		if ( DEBUG_MODE ) { System.out.println("Moving Creature");}
		// get the next tile
		
		if( moveToPlayer()){
			if ( DEBUG_MODE ) { System.out.println("Creature moves towards the player");}
		}else{
			if( moveRandom()){
				if ( DEBUG_MODE ) { System.out.println("Moved Creature Randomly");}
			}else{
				if ( DEBUG_MODE ) { System.out.println("No valid moves for Creature");}
			}
		}
	}

	// Search the neighboring tiles. 
	// If the tile is both open and has the player, move to that tile and return true.
	// If not, return false 
	private boolean moveToPlayer(){
		// FInd the neighboring tiles
		Tile[] neighbors = currentTile.getNeighbors();
		Tile playerTile = null;
		// Iterate the neighbors
		for(int i = 0; i < neighbors.length; i++){
			// If it is a valid move
			if(isCreatureMoveValid(neighbors[i])){
				// and the player is on that tile
				if(neighbors[i].hasPlayer()){
					// move towards it 
					moveToTile(neighbors[i]);
					return true;
				}
			}
		}

		// The player is not next to the creature. 
		return false;
	}

	// Select a random neighboring tile to move to. 
	// Some considerations
	// if the first tile selected is not valid, check the next neighbor. 
	// Remember you can use % neighborCount to "wrap" the index back to zero
	// when it goes out of bounds
	// Also, you will want to keep track of how many times you loop, attempting to move
	// If you check all indexes, you should break out of the loop and 
	// also not move, since you checked all possible moves and they were not valid
	private boolean moveRandom(){
		// Get temp variables
		Tile[] neighbors = currentTile.getNeighbors();
		int neighborCount = neighbors.length;
		// Select an initial random direction
		int randomDirection = GameData.randomRoll(0,neighborCount);
		if( DEBUG_MODE) { System.out.println("Random Direction Chosen: " + randomDirection);} 
		if( DEBUG_MODE) { System.out.println("Neighbors: " + neighborCount);} 
		// increment the randomly selected value, remembering to wrap around the index
		int attempts = 0; // Check that you don't keep going around and around
		Tile nextTile = neighbors[randomDirection];

		// Loop through while attempts < neighbors, and the next neighbor is either not valid (has a wall) or already has a monster
		while(attempts < neighborCount && !isCreatureMoveValid(nextTile)){
			// checking other neighbors looking for a valid one
			randomDirection = ( randomDirection + 1 + neighborCount ) % neighborCount; // Check the next move, clockwise (increase counter) 
			attempts++;
			if( DEBUG_MODE) { System.out.println("Random Direction Chosen in loop: " + randomDirection);} 
			nextTile = neighbors[randomDirection];
		}

		// Only move if there is a valid move available
		if( attempts < neighbors.length){
			// actually execute the move
			moveToTile(nextTile);
			return true;
		}else{
			System.out.println("No valid moves found for monster");
			return false;
		}
	}

	// Is the next tile a wall? 
	// Also check if its a creature
	// A move is valid if it is not a creature or a wall. 
	private boolean isCreatureMoveValid(Tile proposedMove){
		// If the neighboring tile is neither a wall or another creature, then it is valid
		if(proposedMove.isWall() || proposedMove.hasCreature()){ // Can the creature move to that tile?
			return false;
		}else{
			return true;
		}
	}

	// Useful method that handles moving
	// This does not check for validity but simply assigns the creature to the new tile
	// and also updates both the current and new tiles references. 
	private void moveToTile(Tile newTile){
		// Move the player from the current tile to the next tile
		newTile.setCreature(this);// assign the player to it
		currentTile.setCreature(null); // update the currentTile state
		// assign the player reference to the current tile
		currentTile = newTile;
	}

	// **** ACCESSOR METHODS *****
	public int getDifficulty(){
		return difficulty;
	}

	// toString returns only the creature name. 
	public String toString(){
		return creatureName;
	}

}