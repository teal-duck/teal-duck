package com.tealduck.game;


public class LevelOverData {
	public int levelNumber;
	public int score;


	public LevelOverData(int levelNumber, int score) {
		this.levelNumber = levelNumber;
		this.score = score;
	}


	@Override
	public String toString() {
		return "LevelOverData(" + levelNumber + ", " + score + ")";
	}
}
