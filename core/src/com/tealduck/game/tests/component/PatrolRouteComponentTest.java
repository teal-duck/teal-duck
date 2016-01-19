package com.tealduck.game.tests.component;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.PatrolRouteComponent;

public class PatrolRouteComponentTest {
	private ArrayList<Vector2> path;
	private PatrolRouteComponent prc;

	@Before

	public void setup() {
		path = new ArrayList<Vector2>();
		path.add(new Vector2(0, 0));
		path.add(new Vector2(0, 9));
		path.add(new Vector2(9, 9));
		path.add(new Vector2(9, 0));

		prc = new PatrolRouteComponent(path, 0);

	}

	@Test
	public void testPeekAdvanceGet() {
		Assert.assertEquals(prc.peekNextTarget(), new Vector2(0, 9));
		Assert.assertEquals(prc.advanceTarget(), new Vector2(0, 9));
		Assert.assertEquals(prc.getTarget(), new Vector2(0, 9));
		Assert.assertEquals(prc.peekNextTarget(), new Vector2(9, 9));
	}
	
	public void testGetByIndex(){
		Assert.assertEquals(prc.getElementByIndex(0), new Vector2(0,0));
		Assert.assertEquals(prc.getElementByIndex(1), new Vector2(0,9));
		Assert.assertEquals(prc.getElementByIndex(2), new Vector2(9,9));
		Assert.assertEquals(prc.getElementByIndex(3), new Vector2(9,0));
		
	}

}
