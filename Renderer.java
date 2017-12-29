// Final Game Project (Rocket Game)
// By John Ajala and Gina Girgis
// 29 November, 2017
/* renderer class is the entire game. It sets and organizes the entire game components
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.Timer;


public class Renderer extends JPanel {
	
    public final int WIDTH = 800; //width of the game
    public final int HEIGHT = 600; //height of the game
	private final int JUMP = 10;  // increment for rocket movement
	
//instant variables for image icons
	private ImageIcon rocketImage, bgImage, bgStone;
/*instant variable for integers
 * x is the x coordinate of the rocket 
 * y is the y coordinate for the rocket
 * stonexpos is the x position of the stone
 * stoneypos is the y position of the stone
 * movex is the amount of times the stone should move
 * upperbound is the top limit
 * ct is a counter
 * totalstone is integer variable holding the total stones per level*/
	
	private int x, y, stoneXPos, stoneYPos, moveX, upperBound, ct, totalStones;
	// delay is the timer delay 
	private int DELAY = 200, randi;
	private Timer timer;
	Random generate = new Random();
	Game game = new Game();
	
	//bx is the bullet x position, same for y
	private int bx, by, newBulletPos;
	//check rocket state by readytofire
	boolean readyToFire, shot = false;
	
	/*
	 * stoneListX holds random x positions for stones
	 * stoneListY holds random y positions for stones
	 */
	ArrayList<Integer> stoneListX = new ArrayList<Integer>();
	ArrayList<Integer> stoneListY = new ArrayList<Integer>();
	
	// bullets holds all the rectangle bullet objects fired
	private ArrayList<Rectangle> bullets;
	Rectangle bullet;
	
	// Sound object
    public static Clip clip;
    public static AudioInputStream audioInputStream;
    private String shootSound = "shoot.wav"; // Shoot Sound object  
    private String killSound = "kill.wav"; // Kill sound  object
    boolean playing = false;
	
	public Renderer() // Constructor
	{
		addKeyListener(new RendererListener()); // Listener to listen to renderer interface
		addKeyListener(new ShootingListener()); // Listener to listen to shooting interface
		
		
		x = 0; //Set the rocket x position
		y = (HEIGHT/2)-60; // set the rockets y positions
		
		rocketImage = new ImageIcon("images.png"); // Rocket image object

		bgImage = new ImageIcon("cloudbg.gif"); // Background image object
	    
	    // (840 - 780 + 1) + 780
//	    stoneXPos = generate.nextInt(61) + 780;
//	    // (580 - 40 + 1)+40 = 541 + 40
//	    stoneYPos = generate.nextInt(541) + 40;
	    moveX = 3; // Rocks move 3 to the left of it's previous position
	    randi = randi(); // Call the randi method
	    
 		bgStone = new ImageIcon("stone.png"); // Stone image object
	    
	    setFocusable(true); // Set focus of the JPanel object
	    setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Set the size of the game board to 
	    
	    
	    createXPos(game.getGameLevel()); // Create the random x positions based on each level 
	    createYPos(game.getGameLevel()); // Create the random y positions based on each level
	    bullets = new ArrayList<Rectangle>();  // Initialize the bullets arraylist

	    
		timer = new Timer(DELAY, new StoneListener()); // Create a new timer object
	    timer.start(); // Call the timer object method
	    
	    
	}
	
	
	private void endGame()
	{
		/*
		 * End the game
		 * Remove all elements in stoneListX ArrayList Object
		 * Remove all elements in stoneListY ArrayList Object
		 * Remove all elements in bullets in ArrayList Object
		 * reset gamescore and gamelevel
		 */
		game.setGameOver(true); // set gameover to true
		timer.stop(); // Stop timer
		stoneListX.removeAll(stoneListX);
		stoneListY.removeAll(stoneListY);
		bullets.removeAll(bullets);
		game.resetGameScore();
		game.resetGameLevel();
		
	}
	
	
	private void startGame()
	{
		/*
		 * End the game
		 * Remove all elements in stoneListX ArrayList Object First
		 * Remove all elements in stoneListY ArrayList Object First
		 * Remove all elements in bullets in ArrayList Object First
		 * reset gamescore and gamelevel
		 */
		stoneListX.removeAll(stoneListX);
		stoneListY.removeAll(stoneListY);
		bullets.removeAll(bullets);

		/*
		 * Create the new game positions for x and y
		 */
		createXPos(game.getGameLevel());
	    createYPos(game.getGameLevel());
		bullets = new ArrayList<Rectangle>(); // Initialize the bullets array
		
	}


	//-----------------------------------------------------------------
	//  Draws the image in the current location.
	//-----------------------------------------------------------------
	public void paintComponent(Graphics page)
	{
		super.paintComponent(page); // call the paintcomponent 
		
		
		if (game.isStarted())
		{
			/*
			 * if isStarted is true, render the board
			 */
			if (game.gameOver())
			{
				/* 
				 * If game over is true, Call the scoreBoard method, write on board and stop the timer 
				 * Scoreboard is the white panel on top that shows the score of the players
				 */
				scoreBoard(page);
				page.setColor(Color.BLACK);
				page.setFont(new Font("Ariel", 3, 55));
				page.drawString("ROCKET GAME!!!", 75, HEIGHT / 4);
				page.setColor(Color.BLUE);
				page.setFont(new Font("Ariel", 1, 25));
				page.drawString("Yo! Your Highest Score is: "+game.getGameHighScore(), 75, HEIGHT / 2);
				page.setColor(Color.RED);
				page.setFont(new Font("Ariel", 1, 25));
				page.drawString("Highest Level Reached: "+game.getGameHighLevel(), 75, (HEIGHT / 2) + 50);
				page.drawString("Last Score Earned: "+game.getPrevScore(), 75, (HEIGHT / 2) + 100);
				page.setColor(Color.GREEN);
				page.drawString("John Ajala and Gina Girgis", 75, (HEIGHT / 2) + 200);
				
				timer.stop();
			}
			else
			{
			    bgImage.paintIcon(this, page, 0, 0);
			    rocketImage.paintIcon(this, page, x, y);
			    //setBackground(Color.black);
			    
			    scoreBoard(page); // Set the scoreboard
				
				for(int i = 0; i < stoneListX.size(); i++)
				{
					
					bgStone.paintIcon(this, page, stoneListX.get(i), stoneListY.get(i) );
					
				}
				
				for (int i = 0; i < bullets.size(); i++)
				{
					
					if (shot)
					{
						page.setColor(Color.RED);
						page.fillRect(bullets.get(i).x, bullets.get(i).y, bullets.get(i).width, bullets.get(i).height);
					
					}
					if (bullets.get(i).x > 800)
					{
						removeBullet(bullets.get(i));
					}
					
					
					shooting(); // shooting is in the for loop to make continuous shots possible
					if (playing)
						stop();
				}
//				shooting(); // if shooting is here, it would not get faster
				
			}

		}
		else
		{
			/*
			*	if isStart is false, write instructions on the board 
			*	Ask the user to press enter to start the game.
			*/
			
			page.setColor(Color.BLACK);
			page.setFont(new Font("Ariel", 3, 55));
			page.drawString("ROCKET GAME!!!", 75, HEIGHT / 4);
			page.setColor(Color.BLUE);
			page.setFont(new Font("Ariel", 1, 25));
			page.drawString("Press Enter to start!", 75, HEIGHT / 3);
			page.setColor(Color.RED);
			page.setFont(new Font("Ariel", 1, 25));
			page.drawString("Press the space bar to shoot rocks!", 75, (HEIGHT / 3) + 50);
			page.drawString("Press X to end game!", 75, (HEIGHT / 3) + 100);
			page.drawString("Press S to increase your speed!", 75, (HEIGHT / 3) + 150);
			page.drawString("Enjoy!!!!", 75, (HEIGHT / 3) + 200);
			page.setColor(Color.GREEN);
			page.drawString("John Ajala and Gina Girgis", 75, (HEIGHT / 3) + 300);
		}

		
	}
	
	/*
	 * return type void
	 * scoreBoard(Graphics parameter)
	 * Set the scoreboard on the top of the game.
	 */
	private void scoreBoard(Graphics page)
	{
		page.setColor(Color.WHITE);
		page.fillRect(0, 0, 800, 20);
		
		page.setColor(Color.BLUE);
		page.setFont(new Font("Ariel", 3, 15));
		page.drawString("Score: "+game.getGameScore(), 10, 15);
		page.drawString("Level: "+game.getGameLevel(), 200, 15);
		page.setColor(Color.RED);
		page.drawString("High Score: "+game.getGameHighScore(), 500, 15);
	}
	
	
	/*
	 * return type void
	 * Takes in Rectangle ad a parameter
	 * add a new bullet to the bullets arraylist
	 */
	private void addBullet(Rectangle block)
	{
		bullets.add(block);
	}
	

	/*
	 * return type void
	 * Takes in Rectangle ad a parameter
	 * remove a new bullet to the bullets arraylist
	 */
	private void removeBullet(Rectangle block)
	{
		bullets.remove(block);
	}
	

	/*
	 * return type int
	 * Create a random number between (the current game level * 2 + 2) +2
	 * That is, a number greater than 2 and less than the arithmetics
	 * Random numbers are the random Y positions for the rocks
	 */
	private int randi()
	{
		Game game = new Game();

		// Generate Random number of Stones
		randi = generate.nextInt((2*game.getGameLevel() ) + 2) + 2;
		
		return randi;
	}
	
	/*
	 * return type void
	 * Takes in level as parameter
	 * Creates intergers to the right and add to the stoneListX object
	 */
	private void createXPos(int level)
	{
		upperBound = (int) 4+level;
		totalStones = 5+(level*2); // Creates total stones for each level
		ct = 0;
		do
		{
			// Loop runs at least once 
			ct++;
			int xIncrement = ct*100; // Greatly increase the x coordinate to space the rocks out
			// (max - min + 1) + min 900
			for (int i = 0; i < upperBound; i++)
			{
				stoneListX.add((generate.nextInt(61) + 780) + xIncrement ); // Add the the x coordinate to the stoneListX object
			}
		} while (ct <= totalStones);
		
	}
	
	/*
	 * return type void
	 * Takes in level as parameter
	 * Creates intergers to the right and add to the stoneListY object
	 */
	private void createYPos(int level)
	{
		upperBound = (int) 4+level;
		totalStones = 5+(level*2); // Creates total stones for each level
		ct = 0;
		do
		{
			ct++;
			for (int i = 0; i < upperBound; i++)
			{
				stoneListY.add(generate.nextInt(541) + 40); // increase the y coordinate to space the rocks between the game height
			}
		} while (ct <= totalStones);
		
	}
	
	/*
	 * return type void
	 * initialize the game
	 */
	public void init()
	{

		endGame(); // End the game to be sure all the X and Y coordinates are at zero
		x = 0; // Reset Rocket weight
		y = (HEIGHT/2)-60; // Reset Rocket height
		
		if (game.start()) // If start is true
		{
			game.start(); // Set start to true
			startGame(); // Call the startGame method
			
			timer = new Timer(DELAY, new StoneListener());
		    timer.start();
		}
	}
	
	
	/*
	 * RendererListener interface
	 * it implements the keylistener
	 */
	private class RendererListener implements KeyListener
	{
		//Respond to arrow key presses for the rocket
		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_UP: // if the up direction key is pressed
					if (y != -50)
						y -= JUMP;
					else
						y = HEIGHT + 50;
					break;
				case KeyEvent.VK_DOWN: // if the down direction key is pressed
					if (y != HEIGHT + 50)
						y += JUMP;
					else
						y = -60;
					break;
				case KeyEvent.VK_RIGHT: // if the right direction key is pressed
					if (x != WIDTH)
						x += JUMP;
					else
						x = -70;
					break;
				case KeyEvent.VK_LEFT: // if the left direction key is pressed
					if (x != -70)
						x -= JUMP;
					else
						x = WIDTH;
					break;
			}
			checkIntersect();
			repaint(); // Call the repaint method
		}

	    public void keyReleased(KeyEvent event) {
	    	if (event.getKeyCode() == KeyEvent.VK_ENTER) { // If the user presses enter
	            init(); // Call the init method
	        }
	    }
		
		//--------------------------------------------------------------
	    //  Provide empty definitions for unused event methods.
	    //--------------------------------------------------------------
	    public void keyTyped(KeyEvent event) {}

	}
	
	/*
	 * return type is void
	 */
	public void checkIntersect()
	{
		for(int i = 0; i < stoneListX.size(); i++)
		{
			if ( ( ((x + 77) - stoneListX.get(i)) < 2 ) && (((x + 77) - stoneListX.get(i)) > -1) && ((y - 50) < stoneListY.get(i) && (y + 100) > stoneListY.get(i) ) )
			{
				// Objects collide
				// the Rocket hit a rock!
				// Reduce Score
				// Reduce Life
				// Remove Stone
				if (stoneListX.size() > 0)
					stoneListX.remove(stoneListX.get(i));
				if (stoneListY.size() > 0)
					stoneListY.remove(stoneListY.get(i));
				// Rock hits rocket
				// Decrease point
				game.reduceGameScore(10);
				play(killSound);
				if (game.getGameScore() < 1)
					endGame();
				
			}
		}
	}
	

	/*
	 * StoneListener interface
	 * it implements actions
	 * It is created any time the timer object is instantiated
	 */
	private class StoneListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			for(int i = 0; i < stoneListX.size(); i++)
			{
				// While timer is running,
				// Reduce the x coordinate of the each stone in the stoneListX object
				stoneXPos = stoneListX.set(i, stoneListX.get(i)-moveX); 
				// Get the y coordinate of the each stone in the stoneListY object
				stoneYPos = stoneListY.get(i);
				
				
				if (shot)
				{
					// If shots have been fired
					for (int j = 0; j < bullets.size(); j++)
					{
						// For each of the shots fired
						// Check if it intersects with any of the stones current coordinate.
						if (j > 0 && i < stoneListX.size())
						{
							if (bullets.get(j).intersects(stoneListX.get(i)+7, stoneListY.get(i), 77, 60))
							{
								// Bullet intersects (hits) a rock
								// Remove both rock and bullet
								removeBullet(bullets.get(j));
								if (stoneListX.size() > 0)
									stoneListX.remove(stoneListX.get(i));
								if (stoneListY.size() > 0)
									stoneListY.remove(stoneListY.get(i));
								game.setGameScore(10); // Increase the players point by 10
								// call the game set high score method and check if the current score is the high score
								game.setGameHighScore(game.getGameScore()); 
								// Play the kill sound
								play(killSound);
							}
						}
					
					}
					
				}
				

				if ( ( ((x + 77) - stoneListX.get(i)) < 2 ) && (((x + 77) - stoneListX.get(i)) > -1) && ((y - 50) < stoneListY.get(i) && (y + 100) > stoneListY.get(i) ) )
				{
					// Objects collide
					// the Rocket hit a rock!
					// Reduce Score
					// Reduce Life
					// Remove Stone
					if (stoneListX.size() > 0)
						stoneListX.remove(stoneListX.get(i));
					if (stoneListY.size() > 0)
						stoneListY.remove(stoneListY.get(i));
					// Rock hits rocket
					// Decrease point
					game.reduceGameScore(10);
					play(killSound);
					if (game.getGameScore() < 1)
						endGame();
					
				}

				if ( i < stoneListX.size() && stoneListX.get(i) < 0)
				{
					if (stoneListX.size() > 0)
						stoneListX.remove(stoneListX.get(i));
					if (stoneListY.size() > 0)
						stoneListY.remove(stoneListY.get(i));
					// Rock leaves board and does not hit rocket add half point
					game.setGameScore(1);
					game.setGameHighScore(game.getGameScore());;
				}
				
				if (stoneListX.size() == 0 && stoneListY.size() == 0)
				{
					// Level over 
					// On to the next one
					game.setEndALevel(true);
					game.setGameLevel();
					
					createXPos(game.getGameLevel());
				    createYPos(game.getGameLevel());
				    setDelay();
				    timer = new Timer(DELAY, new StoneListener());
				    timer.start();
					
				}
				
			}
	        repaint(); // call the repaint method
		}
		
	}
	

	/*
	 * ShootingListener interface
	 * 
	 */
	private class ShootingListener implements KeyListener
	{

		@Override
		public void keyTyped(KeyEvent e) { } // Unused

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				// if space bar is clicked, fire a shot
				if (game.isStarted())
				{
				    shot = true;
				    
					if (bullet == null)
						readyToFire = true;
					
					if (readyToFire)
					{
						bx = x + 70;
						by = y + 61;
						bullets.add(new Rectangle(bx, by, 7, 5));
						shot = true;
						if (playing)
							stop();
						play(shootSound);
					}
				}
			}
			
			if (e.getKeyCode() == KeyEvent.VK_S)
			{
				// If the user presses the S button, increase the speed by 20
				setDelay();
				timer = new Timer(DELAY, new StoneListener());
			    timer.start();
			}
			
			/*
			 * If X is clicked, end the game
			 */
			if (e.getKeyCode() == KeyEvent.VK_X)
			{
				game.setGameOver(true);
				endGame();
			}
			
		}

		@Override
		/*
		 * if space bar is clicked, fire a shot
		 */
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				if (game.isStarted())
				{
					shot = true;
					readyToFire = true;
					if(playing)
						clip.stop();
				}
			}
			
		}
	}
	
	/*
	 * return type void
	 * 
	 */
	private void shooting()
	{
		for (int i = 0; i < bullets.size(); i++)
		{
			if (shot)
			{
				// Add one to each bullet fired each time to move it forward.
				newBulletPos = (int)(bullets.get(i).x)++;
			}
		}
	}
	
	/*
	 * return type void
	 * Method to increase the timer by 20
	 */
	public void setDelay()
	{
		DELAY -= 20;
	}
	
	/*
	 * play sound method
	 * uses the AudioInputStream object
	 */
	public void play(String f) {

		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(f).getAbsoluteFile());
		} catch (UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}

        try {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(2);
            clip.start();
            playing = true;

        } catch (LineUnavailableException e) {
        } catch (IOException e) {
			e.printStackTrace();
		}

    }

	/*
	 * Stop the sound
	 */
    public void stop() {
        clip.stop();
    }


	

}
