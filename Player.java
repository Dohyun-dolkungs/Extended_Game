// Our fearless hero. Contains all the information
// about a single player instance. 
public class Player{
	
	// State data. 
	private String name;
	private int hitPoints; // current hit points
	private int startingHitPoints; // starting / max hitpoints

	// Inventory
	private ItemList inventory;
	private Tile currentTile;

	// Player with a random name. 
	public Player(){
		name = GameData.getRandomName();
		hitPoints = 10;
		startingHitPoints = 10;
		inventory = new ItemList();
	}

	// Player with specified parameters
	public Player(String newName, int newHitPoints){
		name = newName;
		hitPoints = newHitPoints;
		inventory = new ItemList();
	}

	public void setTile(Tile currentTile){
		this.currentTile = currentTile;
		currentTile.setPlayer(this);
	}

	// Needed to resolve items
	public Tile getCurrentTile(){
		return currentTile;
	}

	public void playerDied(){
		currentTile.setPlayer(null);
	}

	// Is the next tile a wall?
	// True if it is not, false if it is 
	public boolean isMoveValid(int direction){
		Tile[] neighbors = currentTile.getNeighbors();
		if(neighbors[direction].isWall()){ // Can the player move to that tile?
			return false;
		}else{
			return true;
		}
	}

	// Move the player to the next tile in the given direction
	// 0 - North
	// 1 - East
	// 2 - South
	// 3 - West
	public void moveTo(int direction){
		System.out.println("Moving " + direction);
		// get the next tile
		Tile[] neighbors = currentTile.getNeighbors();

		// update the tiles
		Tile newTile = neighbors[direction];
		moveToTile(newTile);
	} 

	// Move the player to a given tile
	private void moveToTile(Tile newTile){
		// Move the player from the current tile to the next tile
		newTile.setPlayer(this);// assign the player to it
		currentTile.setPlayer(null); // update the currentTile state
		// assign the player reference to the current tile
		currentTile = newTile;
	}

	// --- Simple Accessors ---
	// Basic toString. Just the name
	public String toString(){
		return name;
	}

	// --- Health ---
	public int getHitPoints(){
		return hitPoints;
	}

	// take damage
	public void takeDamage(int damage){
		hitPoints -= damage;
	}

	// Small accessor method that lets the player heal
	public void healDamage(int healing){
		hitPoints += healing;
		if(hitPoints > startingHitPoints){
			hitPoints = startingHitPoints;
		}
	}

	// Collect an item
	public void addItem(Item newItem){
		inventory.addItem(newItem);
	}

	// Collect an item
	public void addItems(ItemList newItemList){
		inventory.addItems(newItemList);
	}

	// current player Item count
	public int getItemCount(){
		return inventory.itemCount();
	}

	// Useful for debugging
	public String getInventory(){
		return inventory.toString();
	}

	public String formattedInventoryToString(){
		return inventory.toStringForFiles();
	}

	// Print out a status method
	// Includes name, hitpoints and progress (locations reached and entered)
	public String playerStatus(){
		String st = name;
		st += " has " + hitPoints + " Hit Points.";
		st += "\nThey also collected the following loot: " + getInventory();
		return st;
	}



	
}