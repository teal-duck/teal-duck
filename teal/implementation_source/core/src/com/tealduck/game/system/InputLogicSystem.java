package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tealduck.game.Team;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.TeamComponent;
import com.tealduck.game.component.UserInputComponent;
import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;


/**
 *
 */
public class InputLogicSystem extends GameSystem {
	private final OrthographicCamera camera;


	public InputLogicSystem(EntityEngine entityEngine, OrthographicCamera camera) {
		super(entityEngine);
		this.camera = camera;
	}


	@Override
	public void update(float deltaTime) {
		EntityManager entityManager = getEntityManager();

		@SuppressWarnings("unchecked")
		Set<Integer> entities = entityManager.getEntitiesWithComponents(MovementComponent.class,
				UserInputComponent.class);

		for (int entity : entities) {
			UserInputComponent userInputComponent = entityManager.getComponent(entity,
					UserInputComponent.class);
			MovementComponent movementComponent = entityManager.getComponent(entity,
					MovementComponent.class);
			PositionComponent positionComponent = entityManager.getComponent(entity,
					PositionComponent.class);

			ControlMap controls = userInputComponent.controls;
			Controller controller = userInputComponent.controller;

			float rightState = controls.getStateForAction(Action.RIGHT, controller);
			float leftState = controls.getStateForAction(Action.LEFT, controller);
			float upState = controls.getStateForAction(Action.UP, controller);
			float downState = controls.getStateForAction(Action.DOWN, controller);

			float dx = rightState - leftState;
			float dy = upState - downState;

			int velocityLimit = 1;

			// Sprinting logic:
			// Amount of sprint left is number between 0 and maxSprintTime
			// If the player is sprinting, decrease the number to 0
			// If they aren't sprinting, increase the number back to full
			if (movementComponent.sprinting) {
				movementComponent.sprintTime -= deltaTime;
			} else {
				movementComponent.sprintTime += deltaTime;
			}
			// If the number reaches 0, then all the sprint has been used
			if (movementComponent.sprintTime <= 0) {
				movementComponent.sprintTime = 0;
				movementComponent.sprinting = false;
				movementComponent.usedAllSprint = true;
			}
			// If the number reaches the max, then sprint fully recharged
			if (movementComponent.sprintTime >= movementComponent.maxSprintTime) {
				movementComponent.sprintTime = movementComponent.maxSprintTime;
				movementComponent.usedAllSprint = false;
			}

			float sprintScale = 1;
			if (controls.getStateForAction(Action.SPRINT, controller) > 0) {
				// Player already sprinting
				if (movementComponent.sprinting) {
					sprintScale = movementComponent.sprintScale;
				} else {
					// Only allow sprinting to start if the player didn't use the charge
					if ((movementComponent.sprintTime >= 0) && !movementComponent.usedAllSprint) {
						movementComponent.sprinting = true;
						sprintScale = movementComponent.sprintScale;
					}
				}
			} else {
				movementComponent.sprinting = false;
			}

			// Add the input acceleration to the total acceleration
			// Use limit so that if the player pressed e.g. up and right, the length of the vector is still
			// 1, not sqrt(2)
			Vector2 accelerationDelta = new Vector2(dx, dy);
			accelerationDelta.limit(velocityLimit);
			accelerationDelta.scl(movementComponent.maxSpeed * sprintScale);
			movementComponent.acceleration.add(accelerationDelta);

			// Make the player look at the mouse or controller stick
			if (controller != null) {
				float lookRightState = controls.getStateForAction(Action.LOOK_RIGHT, controller);
				float lookLeftState = controls.getStateForAction(Action.LOOK_LEFT, controller);
				float lookUpState = controls.getStateForAction(Action.LOOK_UP, controller);
				float lookDownState = controls.getStateForAction(Action.LOOK_DOWN, controller);

				float xLook = (lookRightState > lookLeftState) ? lookRightState : -lookLeftState;
				float yLook = (lookUpState > lookDownState) ? lookUpState : -lookDownState;

				if (!((xLook == 0) && (yLook == 0))) {
					positionComponent.lookAt.set(xLook, yLook);
				}
			} else {
				float x = Gdx.input.getX();
				float y = Gdx.input.getY();

				Vector3 posInWorld3 = camera.unproject(new Vector3(x, y, 0));
				Vector2 posInWorld = new Vector2(posInWorld3.x, posInWorld3.y);

				Vector2 entityCenter = positionComponent.getCenter();

				positionComponent.lookAt.set(posInWorld.cpy().sub(entityCenter).nor());
			}

			positionComponent.lookAt.nor();

			if (entityManager.entityHasComponent(entity, WeaponComponent.class)) {
				WeaponComponent weaponComponent = entityManager.getComponent(entity,
						WeaponComponent.class);
				weaponComponent.doCooldown(deltaTime);
				weaponComponent.doReload(deltaTime);

				float fireState = controls.getStateForAction(Action.FIRE, controller);

				if ((fireState > 0) || Gdx.input.isButtonPressed(0)) {
					Vector2 shootPosition = positionComponent.getCenter();
					Vector2 shootDirection = positionComponent.lookAt.cpy().nor();
					// Add scaled direction to position so bullets come out of beak
					shootPosition.mulAdd(shootDirection, 20f);

					Team team = null;
					if (entityManager.entityHasComponent(entity, TeamComponent.class)) {
						team = entityManager.getComponent(entity, TeamComponent.class).team;
					}

					weaponComponent.fireWeapon(getEntityEngine(), entity, shootPosition,
							shootDirection, team);
				}

				float reloadState = controls.getStateForAction(Action.RELOAD, controller);
				if (reloadState > 0) {
					weaponComponent.startReloading();
				}
			}
		}
	}

}
