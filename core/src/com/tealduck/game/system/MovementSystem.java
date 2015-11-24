package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.GameSystem;


public class MovementSystem extends GameSystem {
	public MovementSystem(EntityManager entityManager, EntityTagManager entityTagManager,
			EventManager eventManager) {
		super(entityManager, entityTagManager, eventManager);
	}


	@Override
	public void update(float deltaTime) {
		moveEntities(deltaTime);
		updateSpriteLocations();
	}


	private void moveEntities(float deltaTime) {
		@SuppressWarnings("unchecked")
		Set<Integer> entities = entityManager.getEntitiesWithComponents(PositionComponent.class,
				MovementComponent.class);

		for (int entity : entities) {
			MovementComponent movementComponent = entityManager.getComponent(entity,
					MovementComponent.class);
			Vector2 position = entityManager.getComponent(entity, PositionComponent.class).position;
			Vector2 velocity = movementComponent.velocity;

			position.mulAdd(velocity, deltaTime);
		}

	}


	/**
	 * For each entity that has a position and sprite, updates the position in the sprite to be the same as the
	 * position.
	 */
	private void updateSpriteLocations() {
		@SuppressWarnings("unchecked")
		Set<Integer> entities = entityManager.getEntitiesWithComponents(PositionComponent.class,
				SpriteComponent.class);

		for (int entity : entities) {
			Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
			Vector2 position = entityManager.getComponent(entity, PositionComponent.class).position;

			sprite.setPosition(position.x, position.y);
		}

	}
}
