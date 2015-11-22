package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.UserInputComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.GameSystem;


public class InputLogicSystem extends GameSystem {
	public InputLogicSystem(EntityManager entityManager, EntityTagManager entityTagManager,
			EventManager eventManager) {
		super(entityManager, entityTagManager, eventManager);
	}


	@Override
	public void update(float deltaTime) {
		// TODO: Change hardcoded controls to be configurable
		// Possible put in the component themselves so that e.g. local multiplayer - 2 players with different
		// control configs in their components

		// TODO: Change hardcoding of entity speed

		@SuppressWarnings("unchecked")
		Set<Integer> entities = entityManager.getEntitiesWithComponents(MovementComponent.class,
				UserInputComponent.class);

		for (int entity : entities) {

			int dx = 0;
			int dy = 0;

			if (Gdx.input.isKeyPressed(Keys.D)) {
				dx += 1;
			}
			if (Gdx.input.isKeyPressed(Keys.A)) {
				dx -= 1;
			}
			if (Gdx.input.isKeyPressed(Keys.W)) {
				dy += 1;
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				dy -= 1;
			}

			MovementComponent movementComponent = entityManager.getComponent(entity,
					MovementComponent.class);
			movementComponent.velocity.set(dx, dy).setLength(movementComponent.maxSpeed);

		}
	}
}
