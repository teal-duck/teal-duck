package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.controllers.Controller;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.UserInputComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;


public class InputLogicSystem extends GameSystem {
	public InputLogicSystem(EntityManager entityManager, EntityTagManager entityTagManager,
			EventManager eventManager) {
		super(entityManager, entityTagManager, eventManager);
	}


	@Override
	public void update(float deltaTime) {
		@SuppressWarnings("unchecked")
		Set<Integer> entities = entityManager.getEntitiesWithComponents(MovementComponent.class,
				UserInputComponent.class);

		for (int entity : entities) {
			UserInputComponent userInputComponent = entityManager.getComponent(entity,
					UserInputComponent.class);
			ControlMap controls = userInputComponent.controls;
			Controller controller = userInputComponent.controller;

			float dx = 0;
			float dy = 0;

			float rightState = controls.getStateForAction(Action.RIGHT, controller);
			float leftState = controls.getStateForAction(Action.LEFT, controller);
			float upState = controls.getStateForAction(Action.UP, controller);
			float downState = controls.getStateForAction(Action.DOWN, controller);

			dx = rightState - leftState;
			dy = upState - downState;

			MovementComponent movementComponent = entityManager.getComponent(entity,
					MovementComponent.class);
			movementComponent.velocity.set(dx, dy).limit(1).scl(movementComponent.maxSpeed);

		}
	}
}
