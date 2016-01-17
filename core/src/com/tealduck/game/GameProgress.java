package com.tealduck.game;


/**
 * Groups together the number of the level that was passed and the score the player got in the level. Used for passing
 * information from GameScreen to WinScreen and back to GameScreen for the continue button.
 */
public class GameProgress {
	public int levelNumber;
	public int score;


	/**
	 * @param levelNumber
	 * @param score
	 */
	public GameProgress(int levelNumber, int score) {
		this.levelNumber = levelNumber;
		this.score = score;
	}


	@Override
	public String toString() {
		return "LevelOverData(" + levelNumber + ", " + score + ")";
	}
}
