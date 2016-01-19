package com.tealduck.game.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.tealduck.game.tests.collision.AABBTest;
import com.tealduck.game.tests.collision.CircleTest;
import com.tealduck.game.tests.collision.CollisionTest;
import com.tealduck.game.tests.collision.IntersectionTest;
import com.tealduck.game.tests.component.PatrolRouteComponentTest;
import com.tealduck.game.tests.component.PositionComponentTest;
import com.tealduck.game.tests.component.ScoreComponentTest;
import com.tealduck.game.tests.component.WeaponComponentTest;
import com.tealduck.game.tests.engine.EntityEngineTest;
import com.tealduck.game.tests.engine.EntityManagerTest;
import com.tealduck.game.tests.engine.EntityTagManagerTest;
import com.tealduck.game.tests.engine.EventManagerTest;
import com.tealduck.game.tests.engine.SystemManagerTest;
import com.tealduck.game.tests.pickup.AmmoPickupTest;
import com.tealduck.game.tests.pickup.HealthPickupTest;
import com.tealduck.game.tests.system.ChaseSystemTest;

@RunWith(Suite.class)
@SuiteClasses({AABBTest.class, CircleTest.class, CollisionTest.class, IntersectionTest.class, PatrolRouteComponentTest.class,
	PositionComponentTest.class, ScoreComponentTest.class, WeaponComponentTest.class, EntityEngineTest.class, EntityManagerTest.class,
	EntityTagManagerTest.class, EventManagerTest.class, SystemManagerTest.class, AmmoPickupTest.class, HealthPickupTest.class,
	ChaseSystemTest.class})


public class AllTests {
	
	public static void main(String[] args) throws Exception {
		JUnitCore.main("com.tealduck.game.tests.AllTests");
	}
}
