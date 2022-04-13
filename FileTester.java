/*
* The FileTester class is meant to test the methods in MapTestCases (phase 1) and 
* SaveFiles (phase 3). This unit testing should not be modified (other then uncommenting code)
* you should make your methods work with it. 
*/
public class FileTester{

	// the default test files to load for phase 1
	private static String testFile0 = "mapFile0.txt";
	private static String testFile1 = "mapFile1.txt";
	

	public static void main(String[] args){

		testPhase1();

		//testPhase3();

	}

	// Try loading and processing the Phase 1 methods in MapTestCases. 
	public static void testPhase1(){
		// Test file counter
		try{
			int lineCount = MapTestCases.countValidLines(testFile0);
			System.out.println("Line Count is: " + lineCount);
		}catch(Exception e){
			System.out.println("File could not be opened");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		// Test file counter on a file with leading empty lines.
		try{
			int lineCount = MapTestCases.countValidLines(testFile1);
			System.out.println("Line Count is: " + lineCount);
		}catch(Exception e){
			System.out.println("File could not be opened");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		// Test file counter
		try{
			int lineCount = MapTestCases.countValidLines("invalid_file_not_found.txt");
			System.out.println("Line Count is: " + lineCount);
		}catch(Exception e){
			System.out.println("Testing with invalid File. This is correct as the file does not exist");
		}

		try{
			char[][] testArray = MapTestCases.getBoardFromFile(0);
			System.out.println(arrayToString(testArray));
		
			testArray = MapTestCases.getBoardFromFile(1);
			System.out.println(arrayToString(testArray));
		}catch(Exception e){
			System.out.println("File could not be opened");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
/*
	// Try loading and writing to the SaveFiles class in phase 3
	public static void testPhase3(){
		// Test Player Loader
		try{
			Player newPlayer = SaveFiles.loadPlayer();
			System.out.println("Player Loaded: " + newPlayer.toString() + " " + newPlayer.getInventory());
		}catch(Exception e){
			System.out.println("Player File could not be opened");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		// Test Player Saver
		try{

			Player newPlayer = new Player ("Tester", 11);
			newPlayer.addItem(new Item("Test Item 1"));
			newPlayer.addItem(new Item("Another Item"));
			newPlayer.addItem(new Item("Item"));
			if( SaveFiles.savePlayer(newPlayer)){
				System.out.println("Player Correctly Saved");
			}else{
				System.out.println("Player Was NOT Saved");
			}
		}catch(Exception e){
			System.out.println("Player File could not be Saved");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		// Test Player Loader
		try{
			Player newPlayer = SaveFiles.loadPlayer();
			System.out.println("Player Loaded: " + newPlayer.toString() + " " + newPlayer.getInventory());
		}catch(Exception e){
			System.out.println("Player File could not be opened");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
*/

	// Output for debugging
	// Convert the map to a String for output
	/* Eg output
	"..X..\n
    ..P..\n
    X.I.X"

	*/
	public static String arrayToString(char[][] array){
		// Error checking
		if( array == null){
			return "ERROR Array was Null";
		}

		// Iterate the array and convert to a string. 
		String mapString = "CURRENT MAP:\n";
		for(int row = 0; row < array.length; row++){
			for(int col = 0 ; col< array[row].length; col++){
				mapString += array[row][col];
			}
			mapString += "\n";
		}

		return mapString;
	}

}