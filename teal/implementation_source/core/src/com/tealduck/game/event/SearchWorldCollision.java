package com.tealduck.game.event;


import com.tealduck.game.component.ChaseComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.IEvent;


/**
 * Used by entities that are in random search mode. If they hit a wall they turn around to walk away from it.
 */
public class SearchWorldCollision implements IEvent {
	public static final SearchWorldCollision instance = new SearchWorldCollision();


	private SearchWorldCollision() {
	}


	@Override
	public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
		EntityManager entityManager = entityEngine.getEntityManager();

		if (entityManager.entityHasComponent(receiver, ChaseComponent.class)) {
			ChaseComponent chaseComponent = entityManager.getComponent(receiver, ChaseComponent.class);

			if ((chaseComponent.rotateTime <= 0f)
					&& ((chaseComponent.maxSearchTime - chaseComponent.searchTime) > 0.5f)) {
				chaseComponent.hitWorld = true;
			}

		}
		return false;
	}

}
