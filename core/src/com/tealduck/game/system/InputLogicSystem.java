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


public class InputLogicSystem extends GameSystem {
	// TODO Clean up how InputLogicSystem knows camera
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

			if (movementComponent.sprinting) {
				movementComponent.sprintTime -= deltaTime;
			} else {
				movementComponent.sprintTime += deltaTime;
			}
			if (movementComponent.sprintTime < 0) {
				movementComponent.sprintTime = 0;
				movementComponent.sprinting = false;
				movementComponent.usedAllSprint = true;
			}
			if (movementComponent.sprintTime > movementComponent.maxSprintTime) {
				movementComponent.sprintTime = movementComponent.maxSprintTime;
				movementComponent.usedAllSprint = false;
			}

			float sprintScale = 1;
			if (controls.getStateForAction(Action.SPRINT, controller) > 0) {
				if (movementComponent.sprinting) {
					sprintScale = movementComponent.sprintScale;
				} else {
					if ((movementComponent.sprintTime >= 0) && !movementComponent.usedAllSprint) {
						movementComponent.sprinting = true;
						sprintScale = movementComponent.sprintScale;
					}
				}
			} else {
				movementComponent.sprinting = false;
			}

			Vector2 accelerationDelta = new Vector2(dx, dy);
			accelerationDelta.limit(velocityLimit);
			accelerationDelta.scl(movementComponent.maxSpeed * sprintScale);
			movementComponent.acceleration.add(accelerationDelta);

			// TODO: Perhaps put mouse into the input system instead of if (controller != null)
			if (controller != null) {
				// TODO: Tidy up look at for controller
				// TODO: Maybe smooth rotation?
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
				// .position.cpy().add(32, 32);

				positionComponent.lookAt.set(posInWorld.cpy().sub(entityCenter).nor());
			}

			positionComponent.lookAt.nor();

			// TODO: Move weapon logic into its own system
			if (entityManager.entityHasComponent(entity, WeaponComponent.class)) {
				WeaponComponent weaponComponent = entityManager.getComponent(entity,
						WeaponComponent.class);
				weaponComponent.doCooldown(deltaTime);
				weaponComponent.doReload(deltaTime);

				float fireState = controls.getStateForAction(Action.FIRE, controller);

				if ((fireState != 0) || Gdx.input.isButtonPressed(0)) {
					// TODO: Calculate position to shoot from
					Vector2 shootPosition = positionComponent.getCenter();
					// .position.cpy();
					Vector2 shootDirection = positionComponent.lookAt.cpy().nor();
					shootPosition.mulAdd(shootDirection, 20f);

					Team team = null;
					if (entityManager.entityHasComponent(entity, TeamComponent.class)) {
						team = entityManager.getComponent(entity, TeamComponent.class).team;
					}

					weaponComponent.fireWeapon(getEntityEngine(), entity, shootPosition,
							shootDirection, team);
				}

				float reloadState = controls.getStateForAction(Action.RELOAD, controller);
				if (reloadState != 0) {
					weaponComponent.startReloading();
				}
			}
		}
	}

}
