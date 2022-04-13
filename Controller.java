// A main Game Controller. This is our starting point and controls
// the main execution loop and interfacing with the keyboard listeners. 
public class Controller{
	// Current state references
	// Note these are stored on the Tiles, we simply have a copy of the pointer to them
	// So that we can easily tell them to update. You need to maintain them on the Tile objects as well though
	private Player currentPlayer; // the player currently on the board
	private CreatureList creatures; // creatures currently on the board

	// Controller references to the Board and Window
	private GameBoard board;
	private BoardWindow window;

	// Edit this if you like to make your game realtime!
	// It is easier to test in turn based mode. 
	private boolean isTurnBased = true;

	// Current steps per turn
	// Edit this to change the speed of the game without changing the frame rate
	// This means a turn takes places every STEPS_PER_TURN number of frames
	// Lower values are faster with 0 or 1 being the fastest. 
	private final int STEPS_PER_TURN = 10; // increase this to slow the game down, 
	private int stepsThisTurn = 0; // this is an internal variable that you shouldnt change. 

	private boolean isRunning = true; // Is the game over? 
	private int turnCounter = 0; // which turn are we on
	// Enable this boolean to get extra debugging info from the Controller. 
	private boolean DEBUG_MODE = true;

	// Start the game
    public static void main(String[] args){
    	Controller runtime = new Controller();
    }

	public Controller(){

		// Initialize our Graphics / Input Window
    	// Needs to happen before we load our board
    	board = new GameBoard();
    	window = new BoardWindow(board, this);
    	
    	// Load a new board, this also sets up player and Creature references (locally) and some other stuff
    	loadBoard(0); // assigns all the data to the board. 

    	// Uncomment This line to activate the file loading
    	// !!!!!!!!!!!!!!!!
    	// loadBoardFromFile(1);

	}

	// load a board from the file at a given index (the MapTestCases contains an array of file names)
	// THis will initialize the board and start the game. 
	public void loadBoardFromFile(int boardID){

		// If the id is ok, load the new board
		char[][] newBoard = MapTestCases.getBoardFromFile(boardID);

		if( newBoard == null){
			System.out.println("ERROR LOADING BOARD FROM FILE");
		}

		board.setBoard(newBoard); // sets the current board state

    	// Reset the Controller state
    	// Retrieve the state of the player and creatures from the board
    	// This is not very efficient but that is ok
		currentPlayer = board.getPlayer(); // Is needed because the Player is being created by the board in this case. 
		
		creatures = board.getCreatures();

		// Redraw the board
    	window.repaint();
    
	}

	// Resets a board loaded from the MapTestCases file
	public void loadBoard(int boardID){
		if( boardID < 0 || boardID > MapTestCases.numTests()){
			System.out.println("No board exists for ID " + boardID);
			return;
		}

		// If the id is ok, load the new board
		char[][] newBoard = MapTestCases.getBoard(boardID);
		board.setBoard(newBoard); // sets the current board state

    	// Reset the Controller state
    	// Retrieve the state of the player and creatures from the board
    	// This is not very efficient but that is ok
		currentPlayer = board.getPlayer(); // Is needed because the Player is being created by the board in this case. 
		// Retrieve a CreatureList of all the creatures on the board
    	// This is not very efficient but that is ok for now since we don't call it all the time. 
		creatures = board.getCreatures();

		// Redraw the board
    	window.repaint();
    }

    // ***** Saving and Loading the Player ******
	// Load the saved player into the current players location or 1,1 (if available)
	public void loadSavedPlayer(){
		System.out.println("Loading not yet implemented. Uncomment this method to allow it");
		/*
		// If map has a current player, add it there
		if(currentPlayer != null){
			// get the current tile
			Tile current = currentPlayer.getCurrentTile();
			// Load the player
			Player loaded = SaveFiles.loadPlayer();
			if( loaded != null){
				// Overwrite the current player with the new loaded player
				board.addPlayer(current,loaded);
				currentPlayer = loaded;
				window.printMessage("Player Loaded: " + currentPlayer.toString() + " with items : " + currentPlayer.getInventory());
			}else{
				System.out.println("Failed to load the player");
			}
		}else{
			System.out.println("The Player can only be loaded if there is a player on the map");
		}
		*/
	}

	// Saves the current player to file
	public void saveCurrentPlayer(){
		System.out.println("Saving not yet implemented. Uncomment this method to allow it");
		/*
		SaveFiles.savePlayer(currentPlayer);
		window.printMessage("Player Saved: " + currentPlayer.toString() + " with items : " + currentPlayer.getInventory());
		*/
	}


	// --- Go to the next turn. --- 
	// This includes:
	// 1. moving the creatures, 
	// 2. Encountering creatures
	// 3. Picking up items
	// 4. End of turn checks (eg is the player dead) 
	public void nextTurn(){
		if( !isRunning){
			System.out.println("GAME OVER");
			return;
		}

		// Just in case we want this
		turnCounter++;

		// resolve conflicts between player and creature, pick up items
		resolveConflicts();
		checkEndConditions();

		creatureStep();
		
		// resolve conflicts between player and creature, pick up items
		resolveConflicts();
		checkEndConditions();
		
		// Redraw board
		window.repaint();
		// Debugging
		//if( DEBUG_MODE){ System.out.println("Turn: " + turnCounter); }
	}

	// Collect Item and activate tile. Can't collect the item if the player is null
	private void playerStep(){
		if( currentPlayer == null){
			if( DEBUG_MODE) { System.out.println("Player is null on the player step"); }
			return;
		}

		collectLoot();

		// Default tile output
		// Get current tile
		Tile currentTile = currentPlayer.getCurrentTile(); 

		System.out.println(currentTile.toString());
		window.printMessage(currentTile.toString());

		// Activate the tile effects
		currentTile.activateTile();
	}

	private void collectLoot(){
		// --- Collect loot --- 
		// Get the current tile the player is on
		Tile currentTile = currentPlayer.getCurrentTile(); 
		// If the tile has an item, pick it up and add it to the player inventory.
		if( currentTile.hasItem()){
			// Get item on the tile
			Item i = currentTile.getItem();
			// Remove the Item and add it to the Player
			currentTile.setItem(null);
			currentPlayer.addItem(i);
			// Print out some messages about what is going on
			System.out.println("Player Collected Item: " + i);
			window.printMessage("Player Collected Item: " + i);
		}

	}

	private void creatureStep(){
		// Update Creatures
		if( creatures != null){
			for(int i =0; i < creatures.getCount(); i++){

				// May want to make sure this is not null if you are having trouble
				// Get the current creature and call the moveCreature() method on it
				creatures.getCreature(i).moveCreature();
			}
		}
	}

	private void resolveConflicts(){
		// Can't check anything without the player
		if(currentPlayer == null){
			if(DEBUG_MODE){ System.out.println("Current Player is null in controller"); }
			return;
		}
		
		// Get the current tile the player is on
		Tile currentTile = currentPlayer.getCurrentTile(); 
		
		// Creature conflicts
		if(currentTile.hasCreature()){
			Creature tempCreature = currentTile.getCreature();
			if(battleCreature(tempCreature)){
				// Won Battle
				window.printMessage("Creature Defeated!: " + tempCreature.toString());
				currentTile.setCreature(null);
				creatures.removeCreature(tempCreature); // Remove from the Creature list
			}else{
				// Lost Battle
				window.printMessage("Creature Damaged Player!: " + tempCreature.getDifficulty());
				tempCreature.moveCreature();// move them off the player tile
			}
		}
	}

	// using the given player, battle the creature
	// Slightly changed from the script in A1
	public boolean battleCreature(Creature guardian){
		int items = currentPlayer.getItemCount();
		int roll = GameData.randomRoll(0,3);
		int playerStrength = items + roll;

		if( DEBUG_MODE) { System.out.println("Battle: items: " + items + " roll: " + roll + " playerStrength " + playerStrength);}

		int creatureStrength = guardian.getDifficulty();
		if( DEBUG_MODE) { System.out.println("Creature Diff: " + creatureStrength);}

		if(playerStrength > creatureStrength){
			if( DEBUG_MODE) { System.out.println("Player defeats Creature : " + guardian.toString());}
			return true;
		}else{
			if( DEBUG_MODE) { System.out.println("Player takes : " + creatureStrength + " damage!");}
			currentPlayer.takeDamage(creatureStrength);
			return false;
		}
	}

	// check if the player has collected an item and killed the creatures
	// or if the player has taken critical damage
	public void checkEndConditions(){
		
		if( currentPlayer == null){
			return;
		}

		// Player has died
		if(currentPlayer.getHitPoints() <= 0 ){
			gameOver(false);
		// Player has won
		}else if(creatures != null && creatures.getCount() == 0 && currentPlayer.getItemCount() > 0){
			gameOver(true);
		}
	}

	// Set the Game Over condition. 
	// If true, the player has won
	// if false the player has been eaten
	private void gameOver(boolean playerWins){
		window.printMessage("Game Over!: ");
		isRunning = false;

		if( playerWins ){
			window.printMessage("Player Wins! ");
			window.printMessage(currentPlayer.playerStatus());
		}else{
			window.printMessage("Player Has been Eaten! Oh No!");
			window.printMessage(currentPlayer.playerStatus());
			currentPlayer.playerDied();
			currentPlayer = null;
		}
		
	}

	// We will use an int value to specific direction to the Player
	// 0 Is North / Up
	// 1 is East / Right
	// 2 is South / Down
	// 3 is West / Left
	// Note: This should be the same order the Neighbors are stored as. 
	// Get input from the BoardDrawing Window
	public void arrowKeyPressed(int direction){
		// --- Debugging & Validation --- 
		// Debugging
		//if( DEBUG_MODE) { System.out.println("Arrow Key Pressed: " + direction); }
		// error checking (player must exist)
		if(currentPlayer == null){
			return;
		}

		// If the player can move, move them. 
		if(currentPlayer.isMoveValid(direction)){
 			currentPlayer.moveTo(direction);

 			// Call the player step here instead
			playerStep();

 			// If we are in turn based mode, also update to the next turn
 			if ( isTurnBased){
 				nextTurn(); // start the next turn
 			}
 		}
		
		// [ ] Remove this and add your own input 		
 		if( DEBUG_MODE){ window.printMessage("Testing: Arrow Pressed: " + direction);}
	}
	
	// Non arrow key pressed
	// [ ] Optional, but its there if you want to use it. 
	public void keyPressed(char key){
		if( DEBUG_MODE) { System.out.println("Key Pressed: " + key);}

		if( key == 'l'){
			loadSavedPlayer();
		}else if( key == 's'){
			saveCurrentPlayer();
		}
	}

	// BONUS & OPTIONAL 
	// This is called automatically on a Timer from the BoardWindow class. 
	// If you want to make your game real time instead of turn based, call nextTurn 
	// from here instead of after player movement 
	public void stepUpdate(){
		// This gets called all the time if active
		//if( DEBUG_MODE){ System.out.println("StepUpdate: " + turnCounter); }
		if( !isTurnBased){
			stepsThisTurn++;
			if(stepsThisTurn > STEPS_PER_TURN){
				stepsThisTurn = 0;
				nextTurn(); // start the next turn
			}
		}
	}
}