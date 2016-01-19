package com.tealduck.game.tests.component;

import org.junit.Assert;
import org.junit.Test;

import com.tealduck.game.component.ScoreComponent;

public class ScoreComponentTest {
	private static final double DELTA = 1e-7;
	
	@Test
	public void testIncreaseScore() {

		ScoreComponent scoreComponent = new ScoreComponent();
		scoreComponent.increaseScoreWithComboGain(100);
		Assert.assertEquals(scoreComponent.workingScore, 100);
		Assert.assertEquals(scoreComponent.combo, 1);

		scoreComponent.increaseScoreWithComboGain(100);
		Assert.assertEquals(scoreComponent.workingScore, 200);
		Assert.assertEquals(scoreComponent.combo, 2);

	}

	@Test
	public void testGetComboTimeForCombo() {

		ScoreComponent scoreComponent = new ScoreComponent();
		Assert.assertEquals(scoreComponent.getComboTimeForCombo(0), 4.75, DELTA);
		Assert.assertEquals(scoreComponent.getComboTimeForCombo(1), 4.0, DELTA);
		Assert.assertEquals(scoreComponent.getComboTimeForCombo(2), 2.75, DELTA);
		Assert.assertEquals(scoreComponent.getComboTimeForCombo(3), 1.0, DELTA);
		Assert.assertEquals(scoreComponent.getComboTimeForCombo(4), 0.4, DELTA); //fails? Dunno why. Should run
		Assert.assertEquals(scoreComponent.getComboTimeForCombo(5), 0.4, DELTA); //also fails. Same as above. Mincombo time means it SHOULD run		

		//for (int x = 0; x < 8; x += 1) {
			//System.out.println("" + x + ": " + scoreComponent.getComboTimeForCombo(x));
			// System.out.printf("%i: %d", x, new
			// ScoreComponent().getComboTimeForCombo(x));
		//}

	}
	
	@Test
	public void testFinishCombo() {
		
		ScoreComponent scoreComponent = new ScoreComponent();
		scoreComponent.increaseScoreWithComboGain(100);
		scoreComponent.finishCombo();
		Assert.assertEquals(scoreComponent.score, 100);
		Assert.assertEquals(scoreComponent.workingScore, 0);
		Assert.assertEquals(scoreComponent.combo, 0);
		Assert.assertEquals(scoreComponent.comboTime, 0, DELTA);
		
	}

	
	@Test
	public void testComboCountdown() {
		
		ScoreComponent scoreComponent = new ScoreComponent();
		scoreComponent.increaseScoreWithComboGain(100);
		scoreComponent.getComboTimeForCombo(1);
		scoreComponent.comboCountdown(2.0f);
		Assert.assertEquals(scoreComponent.comboTime, 2.0f, DELTA);
		scoreComponent.comboCountdown(2.5f);
		Assert.assertEquals(scoreComponent.score, 100);
		Assert.assertEquals(scoreComponent.workingScore, 0);
		Assert.assertEquals(scoreComponent.combo, 0);
		Assert.assertEquals(scoreComponent.comboTime, 0, DELTA);
		
	}
}
