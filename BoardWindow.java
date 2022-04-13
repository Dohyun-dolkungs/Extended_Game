// Game Board using Java Graphics2D library and a Thread
// Most of this is beyond the scope of the 1020 course
//
// Dylan Fries 2019
//  
// Drawing libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*; //This is needed for the mouse and key events

// Util library for Random
import java.util.Random;

// Some examples of using Graphics and JFrame. 
// http://zetcode.com/gfx/java2d/basicdrawing/
// http://zetcode.com/tutorials/javagamestutorial/animation/
// JFrame is the whole Window and will hold all the other Panels
// where we draw the graphics to. 
// We will talk about what extends means later on
public class BoardWindow extends JFrame{ 
    
    // Enable Debugging output
    private boolean DEBUG_MODE = true;

    // ----- Window Settings -----
    // Adjust these (but do so at your own risk!)
    // Board size in pixels
    final int WINDOW_WIDTH = 600;
    final int WINDOW_HEIGHT = 600;
    final int DIALOGUE_HEIGHT = 200; // the bottom part of the screen
    final int WINDOW_MARGIN = 20; // border margin around the outside of the window

    // Speed (For realtime updates)
    final static int SPEED = 25; //Time between generations when on "play" (in msecs)


    // =========================================
    // ============== Don't edit this ==========
    // ----- Tile settings -----
    // The size of a single tile
    // this gets set programmatically(overwritten) based on tile count and available space
    private int tileSize = 25; 

    // ----- Dialogue Window -----
    // This is the number of available text boxes displayed
    private int dialoguePanes = 5; // Cannot be 0, 
    private String[] dialogue = {"1","2","3","4","5"}; // current strings written to the GUI

    // ----- Controller references ----
    // References to the Panel within the Window
    private BoardPanel panel; // panel within the window
    // Reference to the Game Board, which will store the board state. 
    private GameBoard board; // board reference
    // Reference to the Controller class, which we will use to control gameplay actions
    private Controller controls;

    // [ ] Not enabled right now
    private boolean running; //True if playing, false if paused.

    // ---Thread / Timer classes ---
    //These objects allow the simulation to run automatically at the indicated SPEED
    private ActionListener doOneGeneration;
    private Timer myTimer;

    /**
     * Constructor for objects of class Board
     * Accepts references to both the GameBoard and Controller.
     */
    public BoardWindow(GameBoard board, Controller controls){
        // Let the board reference our other controller classes
        this.board = board;
        this.controls = controls;

        // *** Methods related to JFrame
        setTitle("A2 Board"); // Call the JFrame methods
        // Set Window size in pixels
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT); 
        setBackground(Color.BLACK); // Board background color
        
        // Add the Panel
        // Note this must be added BEFORE setVisible(true) or the panel won't draw (on Mac only)
        panel = new BoardPanel(this); // Create a panel and pass it the JFrame reference
        add(panel); // add the panel to the window

        // Enable the Window and bring it to the foreground
        setFocusable(true);
        requestFocusInWindow(); // give the window focus
        setVisible(true); // make the window visable

        // ----- Input Listeners -----
        // Listen for Mouse Input - Not Used Yet
        // panel.addMouseListener(new HandleMouse());
        // Listen for keyboard Keys
        addKeyListener(new HandleKeys());

        running=true; // initially we are stopped

        // Enable the Timer and Action Listener
        doOneGeneration = new ActionListener(){
        public void actionPerformed(ActionEvent event) {
            controls.stepUpdate(); // Lets the Controller know we are on a new frame
            repaint();
            };
        };

        // Start a timer thread that fires every SPEED ms. 
        myTimer = new Timer(SPEED, doOneGeneration);
        myTimer.start();
        
        // Exit the code when the window is closed. 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Appends a new message to the info panel. We can't access this directly
    // because panel is a private class stored in the BoardWindow
    // So this method simply passes the method call through. 
    // --- If you want to send messages to the GUI dialogue, send them to this method
    public void printMessage(String newMessage){
       panel.printMessage(newMessage);
    }

    // A Private class that extends JPanel (we will talk about what this means later!)
    // Basically it is a container for other GUI elements. 
    private class BoardPanel extends JPanel{

        private BoardWindow myWindow;

        public BoardPanel(BoardWindow window){
            myWindow = window;// Get the reference to the JFrame this panel is in
        }

        // called by repaint
        @Override
        public void paintComponent(Graphics graphicsObj){
            super.paintComponent(graphicsObj);
            
            //drawStar(graphicsObj);
            
            //drawLine(graphicsObj);
            drawBoard(graphicsObj, board);
            drawInfoPanel(graphicsObj);
        }

        // Draws a gameboard to the screen. It will adapt its size to fit the correct
        // number of tiles to the window size. 
        private void drawBoard(Graphics graphicsObj, GameBoard board){
            // Cast the Graphics object to a graphics 2d object
            Graphics2D graphicsObj2D = (Graphics2D) graphicsObj;

            if(board.getWidth() < 1 || board.getHeight() < 1){
                System.out.println("Board Size is invalid ("+board.getWidth() + " by " + board.getHeight() + " Tiles. Terminating program." );
                System.out.println("You must complete Phase 1 before the board will be loaded again!");
                 System.exit(0);
            }

            int width = getWidth() - (WINDOW_MARGIN * 2); // width of the window
            int height = getHeight() - DIALOGUE_HEIGHT - WINDOW_MARGIN * 2; // height of the window

            // Calculate the size of the squares needed to fit the game board. 
            //int shortBoardEdge = Math.min(width, height);
            int minHorizontal = width / board.getWidth();
            int minVertical = height / board.getHeight();
            // This is in pixels
            // Get the largest tile size we can fit
            tileSize = Math.min(minHorizontal, minVertical);            

            // Draw an Image
            char[][] charMap = board.getBoard();

            // draw each tile sequentially
            for(int row =0 ; row < charMap.length; row++){
                for(int col = 0; col < charMap[row].length; col++){
                    drawTile(graphicsObj2D, charMap[row][col], row, col );
                }
            }
        }

        // Draw a single tile based on its char tileCode and at the row and col 
        private void drawTile(Graphics2D g2d, char tileCode, int row, int col){
            //if( DEBUG_MODE) { System.out.println("Drawing Tile: " + tileCode + " r/c:" + row + ":" + col);}
            // Get the offset based on the map size
            int xOffset = (col * tileSize) + WINDOW_MARGIN;
            int yOffset = (row * tileSize) + WINDOW_MARGIN;

            // Select the tilecode and draw the shape (square or circle)
            if( tileCode == 'X'){
                g2d.setColor(Color.BLACK);
                g2d.fillRect(xOffset, yOffset, tileSize, tileSize);
            }else if(tileCode == '.'){
                g2d.setColor(Color.WHITE);
                g2d.drawRect(xOffset, yOffset, tileSize, tileSize);
                g2d.fillRect(xOffset, yOffset, tileSize, tileSize);
            }else if(tileCode == 'P'){
                // Draw player
               // if( DEBUG_MODE ) { System.out.println("P HIT" + xOffset + ":" + yOffset + " " + tileSize);} 
                g2d.setColor(Color.GREEN);
                g2d.drawOval(xOffset, yOffset, tileSize,tileSize);
                g2d.fillOval(xOffset, yOffset, tileSize,tileSize);    
            }else if(Character.isDigit(tileCode)){
                // Monster
                g2d.setColor(Color.RED);
                g2d.drawOval(xOffset, yOffset, tileSize,tileSize);
                g2d.fillOval(xOffset, yOffset, tileSize,tileSize);   
                g2d.setColor(Color.BLUE);
                g2d.drawString(""+tileCode, xOffset ,yOffset ); 
            }else if(tileCode == 'I'){
                g2d.setColor(Color.YELLOW);
                g2d.drawOval(xOffset, yOffset, tileSize,tileSize);
                g2d.fillOval(xOffset, yOffset, tileSize,tileSize);    
            }else if(tileCode == 'T'){
                // Pit Trap
                g2d.setColor(new Color(166,166,166));
                g2d.fillRect(xOffset, yOffset, tileSize, tileSize);
            }else if(tileCode == 'H'){
                // Healing Tile
                g2d.setColor(Color.CYAN);
                g2d.fillRect(xOffset, yOffset, tileSize, tileSize);
            }

            g2d.setColor(Color.BLACK);
            // Draw outline of tile
            g2d.drawRect(xOffset, yOffset, tileSize, tileSize);
        }

        // Draw the info panel
        private void drawInfoPanel(Graphics g2d){
            g2d.setColor(Color.BLUE);
            // offset from the top
            int width = getWidth() - (WINDOW_MARGIN * 2); // width of the window
            int height = DIALOGUE_HEIGHT;
            int yOffset = getHeight() - DIALOGUE_HEIGHT - WINDOW_MARGIN; // height of the window
            int xOffset = WINDOW_MARGIN;
            g2d.drawRect(xOffset, yOffset, width, DIALOGUE_HEIGHT);
            g2d.drawString("This is gona be awesome",70,20); 

            int textIndent = 20;
            // Render the text
            g2d.setColor(Color.BLACK);
            // [ ] font settings? 
            int oneTextHeight = DIALOGUE_HEIGHT / dialoguePanes;
            for(int i = 0 ; i < dialoguePanes; i++){
                int yPos = yOffset + (oneTextHeight * i) + textIndent;
                g2d.drawString(dialogue[i], xOffset + textIndent, yPos );
            }
        }

        private void printMessage(String newMessage){
            // bump the other messages up
            for(int i = 0; i < dialoguePanes - 1; i++){
                dialogue[i] = dialogue[i+1];
            }
            // Write the last one
            dialogue[dialoguePanes-1] = newMessage;
        }

        
    } // End Board Panel private inner class

/** Not using the mouse (Yet)
     private class HandleMouse implements MouseListener {
        //The five standard methods are required. I don't want these ones:*/
//        public void mousePressed(MouseEvent e){ /*Do nothing */ }
//        public void mouseReleased(MouseEvent e){ /*Do nothing */ }
//        public void mouseEntered(MouseEvent e){ /*Do nothing */ }
//        public void mouseExited(MouseEvent e){ /*Do nothing */ }
        
        //The only one we really want to pay attention to
//        public void mouseClicked(MouseEvent e){
/*        }//mouseClicked      
    }//private inner class HandleMouse
*/     
    // Our HandleKeys method implements the KeyListener interface (which is beyond the scope of this course)
    // Basically it listens for key presses, then passes those to the Controller class. 
    private class HandleKeys implements KeyListener {
        //The standard methods are required.
        public void keyPressed(KeyEvent e){ 
            //System.out.println("Key Pressed: " + e.toString());
            int keyCode = e.getKeyCode();
            if(keyCode == KeyEvent.VK_UP){
                // up arrow
                controls.arrowKeyPressed(0);
            }else if(keyCode == KeyEvent.VK_RIGHT){
                // Right
                controls.arrowKeyPressed(1);
            }else if(keyCode == KeyEvent.VK_DOWN){
                // down arrow
                controls.arrowKeyPressed(2);
            }else if(keyCode == KeyEvent.VK_LEFT){
                // left arrow
                controls.arrowKeyPressed(3);
            }

            char typed = e.getKeyChar(); // convert to a char & lowercase

            // if this is a number
            if(Character.isDigit(typed)){
                if( DEBUG_MODE ) { System.out.println("Load Map "  + typed);}
                // convert to an int
                int selected = Character.digit(typed,10);

                // If this is a valid map
                if(selected>0 && selected <= MapTestCases.numTests()){
                    controls.loadBoard(selected-1);
                    repaint();
                }else{
                    System.out.println("Map out of bounds");
                }
            }else {
                // Other Keys / Letter keys with hard coded functionality
                // convert to lowercase first
                typed = Character.toLowerCase(typed);
                controls.keyPressed(typed); // Tell the controller a key was pressed

                // P also means pause timer
                if(keyCode=='p'){
                    if(running = !running)
                        myTimer.start();
                    else
                        myTimer.stop();

                // This listen for integers and reloads that sample map (if it exists)
                }
            }  
        }

        // Need to be implimented because of the interface but not used. 
        public void keyReleased(KeyEvent e){ /*Do nothing */ }
        public void keyTyped(KeyEvent e){ /*Do nothing */ }
    }
} // End BoardWindow
