public class GameBoard{
	
	// just convenience
	private int width; // number of cols
	private int height; // number of rows

	private Tile[][] map; // The Tiles in the map

	// --- Debug Mode ---
	public boolean DEBUG_MODE = true;

	//************* Basic accessors **************
	// basic size accessors
	public int getHeight(){
		if(map == null)
			return 0;
		else
			return map.length;
	}
	// I made these accessors this way to avoid them crashing when called before the map is initialized. 
	public int getWidth(){
		if(map == null || map.length == 0)
			return 0;
		else
			return map[0].length;
	}

	// Create an empty Template (char[][]) square array of size * size
	public char[][] createEmptyTemplate(int size){
		char[][] newArray = new char[size][size];
		for(int row = 0; row < size; row++){
			for(int col = 0; col < size; col++){
				newArray[row][col] = '.';
			}
		}
		return newArray;
	}

	// ***********************************************
	// convert the char array into a new Tile array.
	// Careful, this will erase the old board 
	public void setBoard(char[][] newBoard){
		// Create a new array of Tiles
		map = new Tile[newBoard.length][newBoard[0].length];

		height = newBoard.length;
		width = newBoard[0].length;

		// Fill the tile array with stuff
		for(int row = 0; row < newBoard.length; row++){
			for(int col = 0 ; col< newBoard[row].length; col++){
				// converting a 1dd array of strings into a 2d array of chars, then Tile
				map[row][col] = createTile(newBoard[row][col]);
			}
		}

		setAllNeighbors();
		if( DEBUG_MODE) { System.out.println(toString());}
	}

	// Create a single new Tile based on the char code and return it
	// Only public so you can test it
	public Tile createTile(char code){

		Tile returnTile = new Tile(); // make the return variable
		// Depending on the code, add features

		if(code == 'X'){
			returnTile.setWall(true);

		}else if(code == 'P'){
			// If P is found, also create a player

			// Add a player to the tile
			Player newPlayer = new Player();
			addPlayer(returnTile, newPlayer);

		}else if(code == 'I'){
			// If I is found, create an Item
			// Create the new item and add it to the map
			Item newItem = new Item();
			addItem(returnTile, newItem);

		}else if(Character.isDigit(code)){
			// Create a new tile
			int difficulty = Character.getNumericValue(code);
			// add a creature to the tile
			Creature newCreature = new Creature(difficulty);
			addCreature(returnTile, newCreature);
			
		}


		System.out.println(returnTile);
		return returnTile;
	}

	// ************************************************
	// Convert Tiles to char
	// return the char version of the board from the Tiles
	// Uses getTileCode
	public char[][] getBoard(){
		// create a new char array to return, based on the map
		char[][] returnMap = new char[map.length][map[0].length];

		// Iterate the board and return the tileCode (char) based on what Objects the Tile contains. 
		for(int row = 0; row < map.length; row++){
			for(int col = 0 ; col< map[row].length; col++){
				returnMap[row][col] = getTileCode(map[row][col]);
			}
		}

		return returnMap;
	}

	// Used by getBoard to convert from Tile to char
	public char getTileCode(Tile currentTile){

		char returnChar = '.'; // Default return value

		// Check which type of tile we have
		if( currentTile.isWall() ){
			returnChar = 'X'; 
			
		// Does it have an Item?
		}else if( currentTile.hasItem()){
			returnChar = 'I';

		// Does it have a Player?
		}else if( currentTile.hasPlayer()){
			returnChar = 'P';

		// Does it have a Creature?
		}else if( currentTile.hasCreature()){
			// Returns an Int with the Creature difficulty
			Creature c = currentTile.getCreature();
			int difficulty = c.getDifficulty();
			// 10 is base ten, could also get base 16 etc
			returnChar = Character.forDigit(difficulty, 10);

		}

		return returnChar;
	}

	// ************************************************
	// Tell each tile to assign all its neighbors
	private void setAllNeighbors(){
		// Iterate all neighbors
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				// setting them one at a time
				// For each tile, tell it to get its neighbors. 
				setOneTileNeighbors(row, col);
			}
		}
	}

	// Assign the neighbors of one tile
	// Indexes used: 
	// 0 Is the tile to the North / Up from our given tile
	// 1 is East / Right
	// 2 is South / Down
	// 3 is West / Left
	private void setOneTileNeighbors(int row, int col){
		if( DEBUG_MODE ){
			System.out.println("[DEBUG]:Is Map null?: " + map.length);
			System.out.println("[DEBUG]:Set One Neighbor: row" + row + " col: " + col);
		}		

		// Create the array of neighbors
		Tile[] neighbors = new Tile[4];
		// Note 0 0 of our array is the top left
		// Using % to wrap around
		// Note we are adding the height/width when going -1 spaces to 
		// avoid 0-1 %length = -1 which would be an index out of bounds
		neighbors[0] = map[(row - 1 + height) % height][col];// North
		neighbors[1] = map[row][(col + 1 + width) % width];// East
		neighbors[2] = map[(row + 1 + height) % height][col];// South
		neighbors[3] = map[row][(col - 1 + width) % width];// West

		// Assign the current tile its neighbors
		map[row][col].setNeighbors(neighbors);
	}

	// ******************************************************
	// Array accessors for Player and Creature
	// ******************************************************
	// Phase 3
	// Collect objects from the array
	// Iterate the array and get the player from the tile to return
	public Player getPlayer(){
		// return variable
		Player thePlayer = null;

		// iterate the array and return the first player found,  there should only be one
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){

				// Extra debugging checks
				if(DEBUG_MODE) { validateTile(row,col);}

				// If the tile has a player, return it
				if(map[row][col].hasPlayer()){
					// actually retrieving the player
					thePlayer = map[row][col].getPlayer();
					if( DEBUG_MODE ){ System.out.println("DEBUG: Player Returned");}
				}		
			}
		}

		return thePlayer;
	}

	// Phase 4
	// Gets all the creatures from the Tiles in the array and returns them in a new CreatureList
	public CreatureList getCreatures(){
		// Create return list
		CreatureList tempList = new CreatureList();

		// iterate the array and return the first player found,  there should only be one
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){
				if(DEBUG_MODE) { validateTile(row,col);}
				
				// Add the creature to the creature list to be returned
				if(map[row][col].hasCreature()){
					tempList.addCreature(map[row][col].getCreature());
				}
			}
		}

		return tempList;
	}


	// **************************************************
		// *****************************************************
	// Not needed, you can use this for testing or to add functionality
	// Add a player to a tile, with validation
	public boolean addPlayer(int row, int col, Player newPlayer){
		if(DEBUG_MODE) { validateTile(row,col);}

		Tile newTile = map[row][col];
		return addPlayer(newTile, newPlayer);
	}

	// Add the player to the given tile
	// Really is just newPlayer.setTile(currentTile) but with validation
	// returns true if it worked, false if it didn't
	public boolean addPlayer(Tile currentTile, Player newPlayer){	
		// Optional , if you are having trouble
		if(DEBUG_MODE) { validateTile(currentTile);}

		if(!currentTile.isWall() && !currentTile.hasCreature()){

			if( currentTile.hasPlayer()){
				if(DEBUG_MODE) { System.out.println("[DEBUG]: Overwriting Existing Player");}
			}
			
			// Assign both the player to the tile and the tile to the player
			newPlayer.setTile(currentTile); // assign the tile to the player, also assigns the tiles to the player (in the same method)

			if(DEBUG_MODE) { System.out.println("[DEBUG]: Successfully created a new Player");}
			return true;
		}else{
			if(DEBUG_MODE) { System.out.println("[DEBUG]:Can't add player to a wall!");}
			return false;
		}
	}

	// Add item - phase 2
	// Add an item to a tile, with validation
	public boolean addItem(int row, int col, Item newItem){

		if(DEBUG_MODE) { validateTile(row,col);}

		Tile currentTile = map[row][col];
		return addItem( currentTile, newItem);
	}

	// Add an item to the given tile
	// This is really just currentTile.setItem(newItem) but with validation
	public boolean addItem(Tile currentTile, Item newItem){
		// Optional , if you are having trouble
		if(DEBUG_MODE) { validateTile(currentTile); }

		if(!currentTile.isWall()){
			if(currentTile.hasItem()){
				if( DEBUG_MODE) { System.out.println("[DEBUG]:Already has item at " + currentTile.toString());}
				return false;
			}else{
				currentTile.setItem(newItem);
				if( DEBUG_MODE) { System.out.println("[DEBUG]:Adding new Item " + newItem.toString() + " to tile");}
				return true;
			}
		}else{
			return false;
		}
	}

	// Not directly used - Just an Example of an overloaded method
	// Add a creature to a tile using the row and col. This is an overloaded method
	// This is not needed for the assignment but you can use it for testing or adding 
	// additional creatures later if you want
	public boolean addCreature(int row, int col, Creature newCreature){
		if(DEBUG_MODE) { validateTile(row,col);}

		Tile currentTile = map[row][col];
		return addCreature(currentTile, newCreature);
	}
	// This method accepts a Tile and Creature
	public boolean addCreature(Tile currentTile, Creature newCreature){
		if(DEBUG_MODE) { validateTile(currentTile);}
		
		// If not a wall and not a creature there already
		if(!currentTile.isWall()){
			if(currentTile.hasCreature()){
				System.out.println("Tile already has creature");
				return false;
			}else{

				// Add the creature to the map and set references both ways
				newCreature.setTile(currentTile);

				System.out.println("Adding new Creature " + newCreature.toString() + " to tile ");
				return true;
			}
		}else{
			return false;
		}
	}

	// **************************************************
	// Bonus Error checking

	// Output for debugging
	// Convert the map to a String for output
	public String toString(){
		if( map == null){
			return "Map was Null";
		}

		String mapString = "CURRENT MAP:\n";
		for(int row = 0; row < map.length; row++){
			for(int col = 0 ; col< map[row].length; col++){
				mapString += getTileCode(map[row][col]);
			}
			mapString += "\n";
		}

		return mapString;
	}

	// Just a useful utility method to convert a row and col into a formatted string
	// May not be needed, but could be useful for debugging
	public String indexToString(int row, int col){
		return "[" + row + "," + col + "]";
	}

	// Error checking to make sure the tile is on the Map
	public boolean validateTile(int row, int col){
		if ( map == null){
			System.out.println("Error: Map not initialized");
			return false;
		}else if( row < 0 || row >= map.length){
			System.out.println("Error: Row out of bounds!" + row + " value called on map size of " + map.length);
			return false;
		}else if( map[0] == null){
			System.out.println("Error: Columns not initialized"); 
			return false;
		}else if( col < 0 || col >= map[0].length ){
			System.out.println("Error: Col out of bounds!" + col + " value called on map size of " + map[0].length);
			return false;
		}else{
			// Row and col are OK and on the map
			return true;
		}
	}

	// Error checking for a Tile object
	public boolean validateTile(Tile newTile){
		if ( map == null){
			System.out.println("Error: Map not initialized");
			return false;
		}else if( newTile == null){
			System.out.println("Error: Tile is Null"); 
			return false;
		}
		return true;
	}

}