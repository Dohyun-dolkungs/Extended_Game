import java.io.*;

public class MapTestCases{
	
  public static boolean DEBUG_MODE = true;

  private static String[] mapFiles = {"mapFile0.txt","mapFile1.txt","invalidFile.txt"};

  // Add more if you want
	private static String[][] testBoards = {
    { "X.X",
      ".X.",
      "X.X"},

    { ".......",
      "..X.X..",
      ".X...X.",
      "..XXX..",
      "......."},

    { "P...",
      ".X.X",
      "...X",
      "...."},

    { ".......P......",
      "..X...X...X..2",
      "..............",
      "..1.X....I.X..",
      ".............."},
	};

	// This will convert the selected test case from an array of Strings into
  // the 2D array of booleans used in Assignment 2.
  public static char[][] getBoard(int index){
    // one is the number of Strings, the other is the length of the string
    String[] sourceMap = testBoards[index];
   
   // Since we are using the same functionality twice in two methods, lets put it in its own method
    return stringToChar(testBoards[index]);
  }

  // Converts an array of Strings to a 2d array of chars
  // Keep in mind a string is just an array of chars, and can use theString.length() method
  public static char[][] stringToChar(String[] strings){
    



    return null;
  }

  // Load a board from the listed array of files
  // This should first count the number of valid lines 
  // ( A line is valid if it contains any characters, so blank lines are ignored)
  public static char[][] getBoardFromFile(int index){





    return null;
  }

  // Iterate a file and count the number of non empty lines
  // A valid line is one that has length > 0
  // Tip: String has a length() method (note it has brackets unlike arrays)
  public static int countValidLines(String file) throws IOException{





    return 0;
  }

  // Load the given number of lines from a file
  // Starting from line 0, the size of the array returned will be lineCount Strings
  public static String[] loadLinesFromFile(String file, int lineCount) throws IOException{
    

    
    return null;
  }

  //The user interface also needs to know how many tests are available.
  public static int numTests(){
    return testBoards.length;
  }
}