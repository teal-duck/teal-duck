package com.tealduck.game.tests.component;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.PositionComponent;

public class PositionComponentTest {

	@Test
	public void testGetCenter() {
		Assert.assertEquals(new Vector2(48, 48), new PositionComponent(new Vector2(16, 16), null, new Vector2(64, 64)).getCenter());

	}

}
