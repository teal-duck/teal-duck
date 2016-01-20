package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


/**
 *
 */
public class ScoreComponent extends Component {
	public int score;
	public int combo;
	public int workingScore;

	public float comboTime;


	/**
	 *
	 */
	public ScoreComponent() {
		score = 0;
		combo = 0;
		workingScore = 0;
		comboTime = 0;
	}

	
	/**
	 * Increase the player's score directly.
	 * @param points
	 */
	public void increaseScore(int points) {
		score += points;
	}
	
	
	/**
	 * Add points to the workingScore, increment the combo and calculate the new comboTime.
	 *
	 * @param points
	 */
	public void increaseScoreWithComboGain(int points) {
		workingScore += points;
		combo += 1;
		comboTime = getComboTimeForCombo(combo);
	}


	/**
	 * Calculate how long until the combo ends for the given combo level.
	 *
	 * @param combo
	 * @return time in seconds
	 */
	public float getComboTimeForCombo(int combo) {
		float x = (combo / 2f) + 0.5f;
		float t = -(x * x) + 5f;

		float minComboTime = 0.4f;

		if (t < minComboTime) {
			t = minComboTime;
		}

		return t;
	}


	/**
	 * Add workingScore * combo to the score and reset the combo.
	 */
	public void finishCombo() {
		score += workingScore * combo;
		workingScore = 0;
		combo = 0;
		comboTime = 0;
	}


	/**
	 * Decrement the combo time. If it reaches 0, finishes the combo.
	 *
	 * @param deltaTime
	 */
	public void comboCountdown(float deltaTime) {
		if (comboTime <= 0) {
			return;
		}
		comboTime -= deltaTime;
		if (comboTime < 0) {
			finishCombo();
		}
	}


	@Override
	public String toString() {
		return "ScoreComponent()";
	}
}
