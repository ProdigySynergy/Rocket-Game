// Final Game Project (Rocket Game)
// By John Ajala
// 29 November, 2017
/* Game class to set and get scores and levels 
 */

public class Game {
	/*
	 * GAME_LEVEL is the current game level
	 * start is when the enter key is pressed and the game begins
	 * gameover is when X is pressed or the use runs out of points
	 * endalevel is when a level is over
	 */
	private int GAME_LEVEL = 1;
	private boolean start = false, gameover = false, endalevel = false;
	
	
	// Other scores and levels instance date
	private int GAME_SCORE = 0, PREV_SCORE, HIGHEST_SCORE = 0, HIGHEST_LEVEL = 1;
	
	
	
	
	/*
	 * return boolean
	 */
	public boolean start()
	{
		gameover = false;
		start = true;
		return start;
	}
	

	/*
	 * return boolean
	 */
	public boolean isStarted()
	{
		return start;
	}
	
	/*
	 * return boolean
	 */
	public boolean stop()
	{
		gameover = true;
		start = false;
		return true;
	}
	
	/*
	 * return void
	 */
	public void setGameLevel()
	{
		GAME_LEVEL += 1;
	}

	/*
	 * return void
	 */
	public void resetGameLevel()
	{
		GAME_LEVEL = 1;
	}

	/*
	 * return integer
	 */
	public int getGameLevel()
	{
		return GAME_LEVEL;
	}

	/*
	 * return void
	 */
	public void setGameScore(int s)
	{
		GAME_SCORE += s;
		setPrevScore(GAME_SCORE);
	}

	/*
	 * return boolean
	 * keep previous score for display when game exits
	 */
	private void setPrevScore(int s)
	{
		PREV_SCORE = s;
	}
	

	/*
	 * return integer
	 */
	public int getPrevScore()
	{
		return PREV_SCORE;
	}

	/*
	 * return void
	 */
	public void resetGameScore()
	{
		GAME_SCORE = 0;
	}

	/*
	 * return void
	 */
	public void reduceGameScore(int s)
	{
		GAME_SCORE -= s;
	}

	/*
	 * return integer
	 */
	public int getGameScore()
	{
		return GAME_SCORE;
	}

	/*
	 * return void
	 */
	public void setGameHighScore(int s)
	{
		if ( s > HIGHEST_SCORE )
			HIGHEST_SCORE = s;
		
	}

	/*
	 * return integer
	 */
	public int getGameHighLevel()
	{
		return HIGHEST_LEVEL;
	}

	/*
	 * return void
	 */
	public void setGameHighLevel(int s)
	{
		if ( s > HIGHEST_LEVEL )
			HIGHEST_LEVEL = s;
		
	}

	/*
	 * return integer
	 */
	public int getGameHighScore()
	{
		return HIGHEST_SCORE;
	}

	/*
	 * return boolean
	 */
	public boolean endALevel()
	{
		return endalevel;
	}

	/*
	 * return void
	 */
	public void setEndALevel(boolean b)
	{
		endalevel = b;
	}

	/*
	 * return boolean
	 */
	public boolean gameOver()
	{
		return gameover;
	}

	/*
	 * return void
	 */
	public void setGameOver(boolean b)
	{
		gameover = b;
	}
}
