package LaPrac;

/**
 *  Create a window in which simple graphic elments can be drawn.
 *  Also allow the programmer to easily get user mouse and keyboard inputs.
 *
 * @author Dr. Wayne Brown <Wayne.Brown@usafa.edu>
 *
 * @version 1.0   July 2007     Original modification of textbook class
 *
 * @version 1.1   19 Sept 2007  Added additional user input for mouse and keyboard
 *
 * @version 1.2   31 Aug  2008  All graphics are drawn to an offscreen buffer.
 *                              Keyboard and Mouse input enhanced.
 *                              Documentation enhanced. 
 * @version 1.3   3 June 2009   updated Javadoc (by Martin Carlisle)
 *                              now redraws when window minimized/restored
 *                              via MyoCanvas class
 * @version 1.4  11 October 2010 changed mouseClickHasOccurred (Martin Carlisle)
 *                               to use synchronized variables
 *============================================================================
 * Original Documentation by Stuart Reges and Marty Stepp (textbook authors)
 *   07/01/2005

 * The DrawingPanel class provides a simple interface for drawing persistent
 * images using a Graphics object.  An internal BufferedImage object is used
 * to keep track of what has been drawn.  A client of the class simply
 * constructs a DrawingPanel of a particular size and then draws on it with
 * the Graphics object, setting the background color if they so choose.

 * To ensure that the image is always displayed, a timer calls repaint at
 * regular intervals.
 *
 *============================================================================
 * Changes made by Dr.Brown to the original code provided by the textbook:
 *    1) Draws to an "offscreen graphics" window that use user must
 *       then copy to the screen.
 *    2) The GUI runs in a separate thread to allow for the 
 *       suspension and resumption of the application program
 *       (so it can wait for mouse and key events).
 *    3) Removed the timer that refreshed the graphics image.
 *============================================================================
 */
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.EventQueue;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;

public class DrawingPanel extends Thread implements MouseInputListener, KeyListener {

    public class SimulatedCAS {

        private boolean value;

        public synchronized boolean getValue() {
            return value;
        }
        public synchronized void setValue(boolean value) {
            this.value = value;
        }
        public synchronized boolean compareAndSwap(boolean expectedValue, boolean newValue) {
            boolean oldValue = value;
            if (value == expectedValue) {
                value = newValue;
            }
            return oldValue;
        }
    }

    public class MyoCanvas extends Canvas {

        public void paint(Graphics g) {
            // this allows for a redraw when minimized/restored
            // may cause a not fully drawn image to be displayed
            // before "copyGraphicsToScreen" called
            g.drawImage(image, 0, 0, width, height, null);
        }
    }
    public static final int LEFT_BUTTON = MouseEvent.BUTTON1; // 1
    public static final int MIDDLE_BUTTON = MouseEvent.BUTTON2; // 2
    public static final int RIGHT_BUTTON = MouseEvent.BUTTON3; // 3
    private static final boolean PRETTY = true;  // true to anti-alias
    private static final int STATUS_BAR_HEIGHT = 30;
    private static final int MAXIMUM_ACTIVE_KEYS = 20;
    public static final int NO_KEY_PRESSED = 0;
    public static final int ANY_KEY = 1;
    public static final int F1_KEY = KeyEvent.VK_F1;
    public static final int F2_KEY = KeyEvent.VK_F2;
    public static final int F3_KEY = KeyEvent.VK_F3;
    public static final int F4_KEY = KeyEvent.VK_F4;
    public static final int F5_KEY = KeyEvent.VK_F5;
    public static final int F6_KEY = KeyEvent.VK_F6;
    public static final int F7_KEY = KeyEvent.VK_F7;
    public static final int F8_KEY = KeyEvent.VK_F8;
    public static final int F9_KEY = KeyEvent.VK_F9;
    public static final int F10_KEY = KeyEvent.VK_F10;
    public static final int F11_KEY = KeyEvent.VK_F11;
    public static final int F12_KEY = KeyEvent.VK_F12;
    public static final int LEFT_ARROW_KEY = KeyEvent.VK_LEFT;
    public static final int RIGHT_ARROW_KEY = KeyEvent.VK_RIGHT;
    public static final int UP_ARROW_KEY = KeyEvent.VK_UP;
    public static final int DOWN_ARROW_KEY = KeyEvent.VK_DOWN;
    public static final int INSERT_KEY = KeyEvent.VK_INSERT;
    public static final int HOME_KEY = KeyEvent.VK_HOME;
    public static final int DELETE_KEY = KeyEvent.VK_DELETE;
    public static final int PAGE_UP_KEY = KeyEvent.VK_PAGE_UP;
    public static final int PAGE_DOWN_KEY = KeyEvent.VK_PAGE_DOWN;
    public static final int ESC_KEY = KeyEvent.VK_ESCAPE;
    public static final int TAB_KEY = KeyEvent.VK_TAB;
    public static final int SHIFT_KEY = KeyEvent.VK_SHIFT;
    public static final int ENTER_KEY = KeyEvent.VK_ENTER;
    private int width, height;     // dimensions of window frame
    private JFrame frame;             // overall window frame
    private Canvas canvas;            // drawing canvas for window (inside panel)
    private BufferedImage image;             // remembers drawing commands
    private Graphics2D canvasGraphics;    // immediate graphics context for painting
    private Graphics2D offscreenGraphics; // buffered graphics context for painting
    private JLabel statusBar;         // status bar showing mouse position
    private long createTime;
    private Thread application;
    private SimulatedCAS[] mouseClicked;
    private int[] mouseClickedX;
    private int[] mouseClickedY;
    private int mostRecentMouseButton;
    private int currentMouseX;
    private int currentMouseY;
    private int waitingForButton;
    private boolean[] buttonDown;        // uses indexes of 1, 2, 3 for buttons
    private int[] activeKeys;
    private boolean[] keyHasBeenRetreivedByApplication;
    private boolean[] keyIsDown;
    private int numberActiveKeys;
    private int indexOfKeyToReturn;
    private boolean waitingForKeyHit;
    private boolean waitingForKeyPressed;

    //----------------------------------------------------------------------------  
    /**
     * Construct a drawing panel of a given width and height enclosed in a window.
     * @param desiredWidth width of panel
     * @param desiredHeight height of panel
     */
    public DrawingPanel(int desiredWidth, int desiredHeight) {
        // Keep a reference to the user application so that it can be suspended to
        // wait for mouse and keyboard events.
        width = desiredWidth;
        height = desiredHeight;
        application = Thread.currentThread();

        // Start the drawing panel in its own thread
        this.run();
    }

    //----------------------------------------------------------------------------  
    /**
     * Run the GUI drawing panel in a separate thread so that the application
     * that is drawing to the window can be paused and restarted (never called by an application).  
     */
    public void run() {
        // Construct a buffered image (an offscreen image that is stored in RAM)
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        offscreenGraphics = image.createGraphics();
        offscreenGraphics.setColor(Color.BLACK);

        // Create a AWT canvas object that can be drawn on - the offscreen image will
        // be drawn onto this canvas
        canvas = new MyoCanvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setBounds(0, 0, width, height);
        canvas.setBackground(Color.WHITE);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);
        canvasGraphics = (Graphics2D) canvas.getGraphics();

        // Create a swing label to display the location of the cursor
        statusBar = new JLabel(" ");
        statusBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        statusBar.setBackground(Color.WHITE);
        statusBar.setForeground(Color.BLACK);
        statusBar.setBounds(0, STATUS_BAR_HEIGHT, width, 20);

        // Create the window
        frame = new JFrame();
        frame.setTitle("Drawing Panel");
        frame.setResizable(false);

        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(canvas, "North");
        frame.getContentPane().add(statusBar, "South");

        frame.pack();
        frame.setFocusable(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.toFront();

        // Initialize the mouse states

        // Store information about mouse clicks for each button separately
        mouseClicked = new SimulatedCAS[4];
        mouseClickedX = new int[4];
        mouseClickedY = new int[4];
        buttonDown = new boolean[4];
        for (int j = 0; j < 4; j++) {
            mouseClicked[j] = new SimulatedCAS();
            mouseClicked[j].setValue(false);
            mouseClickedX[j] = 0;
            mouseClickedY[j] = 0;
            buttonDown[j] = false;
        }

        // Store general information about the state of the mouse
        mostRecentMouseButton = -1;
        currentMouseX = -1;
        currentMouseY = -1;
        waitingForButton = -1;

        // Initialize the key (keyboard) states
        activeKeys = new int[MAXIMUM_ACTIVE_KEYS];
        keyHasBeenRetreivedByApplication = new boolean[MAXIMUM_ACTIVE_KEYS];
        keyIsDown = new boolean[MAXIMUM_ACTIVE_KEYS];
        numberActiveKeys = 0;
        indexOfKeyToReturn = -1;
        waitingForKeyHit = false;
        waitingForKeyPressed = false;

        // Wait for one second to give the swing library time to create the GUI
        // and get it on the screen. This makes sure that the AWT canvas
        // exists on the screen by the time the application tries to display
        // graphics to it.
        sleep(1000);

        // Make the canvas have the focus so that events are immediately sent to it.
        canvas.requestFocus();
    }

    //----------------------------------------------------------------------------  
    /**
     * Set the windows name -- which appears in the window's header.
     * @param name title of window
     */
    public void setWindowTitle(String name) {
        frame.setTitle(name);
    }

    //----------------------------------------------------------------------------  
    /**
     * Close the DrawingPanel (actually closes the JFrame)  
     */
    public void closeWindow() {
        frame.dispose();
    }

    //============================================================================
    // Implement all the MouseInputListener methods
    //============================================================================
    //----------------------------------------------------------------------------  
    /**
     * MouseInputListener Callback (application never calls this method)
     */
    public void mouseMoved(MouseEvent e) {
        currentMouseX = e.getX();
        currentMouseY = e.getY();
        statusBar.setText("(" + currentMouseX + ", " + currentMouseY + ")");
    }

    //----------------------------------------------------------------------------  
    /**
     * MouseInputListener Callback (application never calls this method)
     */
    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    //----------------------------------------------------------------------------  
    /**
     * MouseInputListener Callback (application never calls this method)
     */
    public void mouseExited(MouseEvent e) {
        statusBar.setText(" ");
    }

    //----------------------------------------------------------------------------  
    /**
     * MouseInputListener Callback (application never calls this method)
     */
    public void mousePressed(MouseEvent e) {
        mostRecentMouseButton = e.getButton();
        buttonDown[mostRecentMouseButton] = true;
    }

    //----------------------------------------------------------------------------  
    /**
     * MouseInputListener Callback (application never calls this method)
     */
    public void mouseReleased(MouseEvent e) {
        int whichButton = e.getButton();
        buttonDown[mostRecentMouseButton] = false;
        // get the mouse click information and save it for retreival later
        mouseClicked[whichButton].setValue(true);
        mouseClickedX[whichButton] = e.getX();
        mouseClickedY[whichButton] = e.getY();

        mostRecentMouseButton = whichButton;

        //System.out.println("Recognized click button " + whichButton);
        if (waitingForButton == whichButton) {
            // The application is waiting for this button
            waitingForButton = -1;

            //System.out.println("mouse click = " + mouseButton + " x = " + mouseX + " y = " + mouseY);
            // Resume the application
            synchronized (application) {
                application.notify();
            }
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * MouseInputListener Callback (application never calls this method)
     */
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    //----------------------------------------------------------------------------  
    /**
     * MouseInputListener Callback (application never calls this method)
     */
    public void mouseClicked(MouseEvent e) {
        int whichButton = e.getButton();

    }

    //============================================================================
    // Implement the "KeyListener" methods
    //============================================================================
    //----------------------------------------------------------------------------  
    /**
     * Internal method to search for a key to see if it is in the "active key" list
     * (applications will never call this method).
     * @param keyCode integer value of key
     */
    private int findKeyCodeIndexInList(int keyCode) {
        for (int j = 0; j < numberActiveKeys; j++) {
            if (activeKeys[j] == keyCode) {
                return (j);
            }
        }
        return (-1);
    }

    //----------------------------------------------------------------------------  
    /**
     * Internal debugging method - print the keyboard buffer.
     */
    private void printActiveKeys(String description) {
        // Debugging
        System.out.print(description + numberActiveKeys + " keys in buffer: ");
        for (int j = 0; j < numberActiveKeys; j++) {
            System.out.printf("%d %c %b %b ", activeKeys[j], (char) activeKeys[j],
                    keyHasBeenRetreivedByApplication[j], keyIsDown[j]);
        }
        System.out.println();
    }

    //----------------------------------------------------------------------------  
    /**
     * Internal method to remove a key from the "active key" list
     * (applications will never call this method).
     */
    private void removeActiveKey(int index) {
        for (int j = index; j < numberActiveKeys - 1; j++) {
            activeKeys[j] = activeKeys[j + 1];
            keyHasBeenRetreivedByApplication[j] = keyHasBeenRetreivedByApplication[j + 1];
            keyIsDown[j] = keyIsDown[j + 1];
        }

        numberActiveKeys--;
        if (numberActiveKeys < 0) {
            numberActiveKeys = 0;
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Callback method for "key pressed" events (never call explicitly).
     *
     * Known problem: if multiple keys are being held down simultaneously, the 
     * operating system does not always generate new key pressed events for
     * new keys pressed.
     */
    public void keyPressed(KeyEvent e) {
        //System.out.printf("KEY PRESSED EVENT  keycode = %d %c\n", e.getKeyCode(), e.getKeyCode());

        // Add this key code to the list of keys that are currently down
        if (numberActiveKeys < MAXIMUM_ACTIVE_KEYS) {
            int keyCode = e.getKeyCode();

            // Only add the key to the active list if it is not already in the list.
            // Note: the operating system is sending repeated events for the
            //       same key if the key is being held down.
            int index = findKeyCodeIndexInList(keyCode);
            if (index == -1) {
                activeKeys[numberActiveKeys] = keyCode;
                keyHasBeenRetreivedByApplication[numberActiveKeys] = false;
                keyIsDown[numberActiveKeys] = true;
                numberActiveKeys++;
            } else {
                keyHasBeenRetreivedByApplication[index] = false;
                keyIsDown[index] = true;
            }
        }

        // If the application is waiting for a key press, wake the application up
        if (waitingForKeyPressed) {
            waitingForKeyPressed = false;
            synchronized (application) {
                application.notify(); // stop waiting
            }
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Callback method for "key release" events (never call explicitly).
     */
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // find the key in the list of active keys.
        int index = findKeyCodeIndexInList(keyCode);

        // Remove the key if it has been consumed at least once by the application.
        if (index >= 0) {
            keyIsDown[index] = false;
            if (keyHasBeenRetreivedByApplication[index]) {
                removeActiveKey(index);
            }
        }

        // Debugging
        //printActiveKeys("After keyReleased event: )

        // If the application is waiting for a key hit, wake the application up
        if (waitingForKeyHit) {
            waitingForKeyHit = false;
            synchronized (application) {
                application.notify(); // stop waiting
            }
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Callback method for "key typed" events (never used - never call explicitly).
     */
    public void keyTyped(KeyEvent e) {
    }

    //============================================================================
    // Implement the "DrawingPanel's" methods
    //============================================================================
    //----------------------------------------------------------------------------  
    /**
     * Obtain the Graphics object needed to draw on the DrawingPanel's offscreen
     * graphics buffer. Make sure you call copyGraphicsToScreen() after all your drawing
     * methods have been called to copy the offscreen graphics to the screen.
     */
    // 
    public Graphics2D getGraphics() {
        return offscreenGraphics;
    }

    //----------------------------------------------------------------------------  
    /**
     * Clears the DrawingPanel's graphics window to the specified color. All previously
     * drawn graphics are erased.
     * @param c new background color
     */
    public void setBackground(Color c) {
        // remember the current color so it can be restored
        Color currentColor = offscreenGraphics.getColor();

        offscreenGraphics.setColor(c);
        offscreenGraphics.fillRect(0, 0, width, height);

        // restore color
        offscreenGraphics.setColor(currentColor);
    }

    //----------------------------------------------------------------------------  
    /**
     * Copy the offscreen graphics buffer to the screen. No graphics are visible
     * until this method is called.
     */
    public void copyGraphicsToScreen() {
        Graphics2D myG = (Graphics2D) canvas.getGraphics();
        myG.drawImage(image, 0, 0, width, height, null);
    }

    //----------------------------------------------------------------------------  
    /**
     * Make your application "sleep" for the specified number of milliseconds.
     * @param millis # of milliseconds
     */
    public void sleep(int millis) {
        synchronized (application) {
            try {
                application.sleep(millis);
            } catch (InterruptedException e) {
            }
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Wait for a mouse click on a specific mouse button (your application goes to sleep
     * until the specific mouse button has been clicked (pressed and released)).
     *
     * @param  whichButton   which mouse button you want to wait for
     *                       (DrawingPanel.LEFT_BUTTON, DrawingPanel.MIDDLE_BUTTON, or DrawingPanel.RIGHT_BUTTON).
     */
    public void waitForMouseClick(int whichButton) {
        if (whichButton >= LEFT_BUTTON && whichButton <= RIGHT_BUTTON) {
            waitingForButton = whichButton;
            //System.out.println("Waiting for button " + whichButton + ". Application waiting.");
            synchronized (application) {
                try {
                    application.wait();
                } catch (InterruptedException e) {
                }
                ;
            }
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Determine if a mouse click has occurred. Calling this method always resets the
     * status of the specified button to "unclicked." If you want the location of the 
     * click, immediately call getMouseClickX(whichButton) and/or getMouseClickY(whichButton)
     *
     * @param  whichButton   which mouse button you want to check for a click
     *                       (DrawingPanel.LEFT_BUTTON, DrawingPanel.MIDDLE_BUTTON, or DrawingPanel.RIGHT_BUTTON).
     *
     * @return true if a mouse click using the specified button has occurred since the last call to this method,
     *              false otherwise.  
     */
    public boolean mouseClickHasOccurred(int whichButton) {
        if (whichButton >= LEFT_BUTTON && whichButton <= RIGHT_BUTTON) {
            return mouseClicked[whichButton].compareAndSwap(true, false);
        }
        return false;
    }

    //----------------------------------------------------------------------------  
    /**
     * Determine if a specific mouse button is down at the time of the method call.
     *
     * @param  whichButton   which mouse button you want to check the status of
     *                       (DrawingPanel.LEFT_BUTTON, DrawingPanel.MIDDLE_BUTTON, or DrawingPanel.RIGHT_BUTTON).
     *
     * @return true if a mouse button is down, false otherwise.  
     */
    public boolean isMouseButtonDown(int whichButton) {
        if (whichButton >= LEFT_BUTTON && whichButton <= RIGHT_BUTTON) {
            return buttonDown[whichButton];
        } else {
            return false;
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Get which mouse button was most recently clicked.
     *
     * @return button code (DrawingPanel.LEFT_BUTTON, DrawingPanel.MIDDLE_BUTTON, or DrawingPanel.RIGHT_BUTTON)
     */
    public int getMouseButton() {
        return mostRecentMouseButton;
    }

    //----------------------------------------------------------------------------  
    /**
     * Get the current x coordinate of the mouse.
     *
     * @return x coordinate of the mouse in pixels (left side of window is 0, right
     *         side is the (DrawingPanel's width - 1). 
     */
    public int getMouseX() {
        return currentMouseX;
    }

    //----------------------------------------------------------------------------  
    /**
     * Get the current y coordinate of the mouse.
     *
     * @return y coordinate of the mouse in pixels (top of window is 0, bottom
     *         is the (DrawingPanel's height - 1). 
     */
    public int getMouseY() {
        return currentMouseY;
    }

    //----------------------------------------------------------------------------  
    /**
     * Get the x coordinate of the mouse's location when the most recent mouse click
     * of the specified button occurred.  Call the mouseClickHasOccurred(whichButton)
     * method to determine if a mouse click has occurred. If no mouse click has occurred,
     * this method returns a bogus x coordinate.
     *
     * @return x coordinate of the mouse (in pixels) when the most recent mouse click for the specified button occurred. 
     */
    public int getMouseClickX(int whichButton) {
        if (whichButton >= LEFT_BUTTON && whichButton <= RIGHT_BUTTON) {
            return mouseClickedX[whichButton];
        } else {
            return -1;
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Get the y coordinate of the mouse's location when the most recent mouse click
     * of the specified button occurred. Call the mouseClickHasOccurred(whichButton)
     * method to determine if a mouse click has occurred. If no mouse click has occurred,
     * this method returns a bogus y coordinate.
     *
     * @return y coordinate of the mouse (in pixels) when the most recent mouse click for the specified button occurred. 
     */
    public int getMouseClickY(int whichButton) {
        if (whichButton >= LEFT_BUTTON && whichButton <= RIGHT_BUTTON) {
            return mouseClickedY[whichButton];
        } else {
            return -1;
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Wait for the user to hit a key on the keyboard -- your application goes to 
     * sleep until a key is pressed and released on the keyboard.
     */
    // 
    public void waitForKeyHit() {
        waitingForKeyHit = true;
        synchronized (application) {
            try {
                application.wait();
            } catch (InterruptedException e) {
            }
            ;
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Is the specified key down at this moment in time?
     *
     * @param  whichKeyCode   which key to check
     *
     * @return true if the specified key is down, false otherwise.  
     */
    public boolean keyIsDown(int whichKeyCode) {
        if (whichKeyCode == ANY_KEY) {
            // if any key is in the active buffer and down, then return true
            for (int j = 0; j < numberActiveKeys; j++) {
                if (keyIsDown[j]) {
                    return true;
                }
            }
        } else {
            int index = findKeyCodeIndexInList(whichKeyCode);
            if (index >= 0) {
                return keyIsDown[index];
            }
        }
        return false;
    }

    //----------------------------------------------------------------------------  
    /**
     * Has the specified key been hit (pressed and released)?
     *
     * @param  whichKeyCode   which key to check
     *
     * @return true if the specified key has been hit, false otherwise.  
     */
    public boolean keyHasBeenHit(int whichKeyCode) {
        if (whichKeyCode == ANY_KEY) {
            // if any key is in the active buffer and not down, then return true
            for (int j = 0; j < numberActiveKeys; j++) {
                if (!keyIsDown[j]) {
                    removeActiveKey(j);
                    return true;
                }
            }
        } else // there is a specific key the user wants to query
        {
            int index = findKeyCodeIndexInList(whichKeyCode);
            if (index >= 0) {
                if (!keyIsDown[index]
                        && !keyHasBeenRetreivedByApplication[index]) {
                    removeActiveKey(index);
                    return true;
                }
            }
        }
        return false;
    }

    //----------------------------------------------------------------------------  
    /**
     * Wait for the user to press a key on the keyboard -- your application goes to 
     * sleep until a key is pressed.
     */
    // 
    public void waitForKeyPressed() {
        waitingForKeyPressed = true;
        synchronized (application) {
            try {
                application.wait();
            } catch (InterruptedException e) {
            }
            ;
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Returns a single "key code" for a key that was pressed and released.
     * If multiple keys are hit at the same time, the order of the keys returned
     * is indeterminate. Key hits are bufferred. Therefore, if multiple keys are hit
     * at the same time, subsequent calls to getKeyHitCode will return the other keys.
     * A "key hit" will only be returned once.
     *
     * @return  returns a key code for the latest key that was pressed and released by a user.
     * 
     * <p>The integer values represents the key that was pressed (and not the upper or
     * lower case of the key).
     * <ul>
     *   <li>For the digits, the codes are equivalent to the Unicode character '0' - '9'</li>
     *   <li>For the characters, the codes are equivalent to the Unicode characters 'A' - 'Z' (NOTE: CAPITAL LETTERS)</li>
     *   <li>For the function keys, use the named constants F1_KEY, ... F12_KEY</li>
     *   <li>For the other special keys, use:
     *     <ul>
     *       <li> DrawingPanel.LEFT_ARROW_KEY </li>
     *       <li> DrawingPanel.RIGHT_ARROW_KEY </li>
     *       <li> DrawingPanel.UP_ARROW_KEY </li>
     *       <li> DrawingPanel.DOWN_ARROW_KEY </li>
     *       <li> DrawingPanel.INSERT_KEY </li>
     *       <li> DrawingPanel.HOME_KEY </li>
     *       <li> DrawingPanel.DELETE_KEY </li>
     *       <li> DrawingPanel.PAGE_UP_KEY </li>
     *       <li> DrawingPanel.PAGE_DOWN_KEY </li>
     *       <li> DrawingPanel.ESC_KEY </li>
     *       <li> DrawingPanel.TAB_KEY </li>
     *       <li> DrawingPanel.SHIFT_KEY </li>
     *       <li> DrawingPanel.ENTER_KEY </li>
     *     </ul>
     *   </li>
     * </ul>
     *
     */
    public int getKeyHitCode() {
        int returnKeyCode = NO_KEY_PRESSED; // assume there are no active keys

        if (numberActiveKeys > 0) {
            // Debugging
            //printActiveKeys("Start of getKeyHitCode: " );
            //System.out.println("indexOfKeyToReturn = " + indexOfKeyToReturn);

            // Make sure there is a valid index into the keys hit buffer
            if (indexOfKeyToReturn < 0
                    || indexOfKeyToReturn >= numberActiveKeys) {
                indexOfKeyToReturn = 0;
            }

            // Only return the active key if it has been released and it has not been
            // returned previously.
            if (!keyIsDown[indexOfKeyToReturn]
                    && !keyHasBeenRetreivedByApplication[indexOfKeyToReturn]) {
                returnKeyCode = activeKeys[indexOfKeyToReturn];

                removeActiveKey(indexOfKeyToReturn);
                if (indexOfKeyToReturn >= numberActiveKeys) {
                    indexOfKeyToReturn = 0;
                }
            } else {
                indexOfKeyToReturn = (indexOfKeyToReturn + 1) % numberActiveKeys;
            }
        }

        return returnKeyCode;
    }

    //----------------------------------------------------------------------------  
    /**
     * Return the number of keys that are currently being pressed on the keyboard.
     * <p>If you call the getKeyCode() the number of times returned by this method,
     * you will get the key codes for all keys currently being held down.
     * @return number of pressed keys
     */
    public int numberOfKeysActive() {
        return numberActiveKeys;
    }

    //----------------------------------------------------------------------------  
    /**
     * Return an integer representation of the color at a specified pixel location.
     * @return integer RGB value
     */
    public int getRGB(int x, int y) {
        try {
            return image.getRGB(x, y);
        } catch (Exception e) {
            return 0;
        }
    }

    //----------------------------------------------------------------------------  
    /**
     * Set the color at a specified pixel location.
     * @param x x coordinate
     * @param y y coordinate
     * @param RGB color
     */
    public void setRGB(int x, int y, int RGB) {
        try {
            image.setRGB(x, y, RGB);
        } catch (Exception e) {
        }
    }

    /**
     * Save the current graphics (in the offscreen buffer) to a file. The file name
     * should have an appropriate image file extension, such as ".bmp" or ".jpg"
     * @param filename name of file
     */
    public void saveGraphics(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1);

        // write file
        try {
            ImageIO.write(image, extension, new java.io.File(filename));
        } catch (java.io.IOException e) {
            System.err.println("Unable to save image:\n" + e);
        }
    }

    /**
     * Load a bitmap image into memory.
     *
     * @param  filename   the name of the bitmap image file (including the .bmp extension)
     *
     * @return  a BufferedImage object 
     */
    public static BufferedImage loadBitmap(String filename) {
        // read file
        try {
            BufferedImage image = ImageIO.read(new java.io.File(filename));
            return image;
        } catch (java.io.IOException e) {
            System.err.println("Unable to read bitmap image:\n" + e);
            return null;
        }
    }
}
