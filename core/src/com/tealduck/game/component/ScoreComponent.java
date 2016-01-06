package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


public class ScoreComponent extends Component {
	public int score;
	public int combo;
	public int workingScore;

	public float comboTime;


	public ScoreComponent() {
		score = 0;
		combo = 0;
		workingScore = 0;
		comboTime = 0;
	}


	public void increaseScoreWithComboGain(int points) {
		workingScore += points;
		combo += 1;
		comboTime = getComboTimeForCombo(combo);
	}


	public float getComboTimeForCombo(int combo) {
		// TODO: Calculate combo time
		return 2f;
	}


	public void finishCombo() {
		score += workingScore * combo;
		workingScore = 0;
		combo = 0;
		comboTime = 0;
	}


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
