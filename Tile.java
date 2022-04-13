// the Tile class represents a single Tile on the GameBoard. 
// This is where the other objects in the game are stored. 
// c Dylan Fries 2019
public class Tile{
	// the neighbors of this tile
	private Tile[] neighbors;
	// is it passable or not
	protected boolean isWall = false;
	
	// Objects on the tile
	protected Creature creature = null;
	protected Item item;
	protected Player player;

	// Basic Tile
	public Tile(){
		// basic tile constructor
		isWall = false;
	}

	// Override this method in child classes
	public String toString(){
		return "Nothing to see here...";
	}

	// Override this with child classes. 
	public void activateTile(){
		System.out.println("Nothing to do here");
	}

	public void setNeighbors(Tile[] newNeighbors){
		neighbors = newNeighbors;
	}

	public Tile[] getNeighbors(){
		return neighbors;
	}

	// Set the state of the tile to be Wall
	public void setWall(boolean isWall){
		this.isWall = isWall;
	}

	// Mutator methods
	public void setItem(Item newItem){
		item = newItem;
	}
	public void setPlayer(Player newPlayer){
		player = newPlayer;
	}
	public void setCreature(Creature newCreature){
		creature = newCreature;
	}

	// -----  Accessor methods -----
	// Returns the Item
	public Item getItem(){
		return item;
	}
	// Returns the player
	public Player getPlayer(){
		return player;
	}
	// Returns the Creature
	public Creature getCreature(){
		return creature;
	}

	// ----- Does the Tile contain things --
	// Does the Tile contain an item? 
	public boolean hasItem(){
		return (item != null);
	}
	// Does the Tile contain a Player? 
	public boolean hasPlayer(){
		return (player != null);
	}
	// Does the Tile contain a Creature? 
	public boolean hasCreature(){
		return (creature != null);
	}
	
	// returns true if this Tile is a wall or false if it is not
	// walls are not passible by Creatures or Players 
	public boolean isWall(){
		return isWall;
	}
}