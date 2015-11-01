import pygame;
import math;
from ecs import System;
from components import *;
from collision import *;
from vector2 import Vector2;



class LogicSystem(System):
	def update(self, deltaTime):
		for entity in self.getAllEntitiesPossessingComponents(PlayerControlledComponent, ForceComponent): #, VelocityComponent):
			controller = self.getComponent(entity, PlayerControlledComponent);
			#velocityComponent = self.getComponent(entity, VelocityComponent);
			forceComponent = self.getComponent(entity, ForceComponent);

			controls = controller.controls;

			keys = pygame.key.get_pressed();
			dx = 0;
			dy = 0;
			if (keys[controls["up"]]):
				dy -= 1;
			if (keys[controls["down"]]):
				dy += 1;
			if (keys[controls["left"]]):
				dx -= 1;
			if (keys[controls["right"]]):
				dx += 1;

			# speed = 200.0;
			# velocityComponent.velocity = Vector2(dx, dy).setMagnitudeTo(speed);

			forceComponent.force += Vector2(dx, dy) * 10000#0;

		for entity in self.getAllEntitiesPossessingComponents(AIControlledComponent, PositionComponent, VelocityComponent):
			controller = self.getComponent(entity, AIControlledComponent);
			positionComponent = self.getComponent(entity, PositionComponent);
			velocityComponent = self.getComponent(entity, VelocityComponent);

			players = self.getAllEntitiesPossessingComponents(PlayerControlledComponent, PositionComponent);
			if (len(players) > 0):
				# Ugly hack
				player = players.pop();

				playerPositionComponent = self.getComponent(player, PositionComponent);

				direction = playerPositionComponent.position - positionComponent.position;
				speed = 100.0;
				vecToMove = direction.setMagnitudeTo(speed);
				velocityComponent.velocity = vecToMove;
			else:
				velocityComponent.velocity = Vector2(0, 0);


		for entity in self.getAllEntitiesPossessingComponents(HealthComponent):
			healthComponent = self.getComponent(entity, HealthComponent);

			if (healthComponent.health <= 0):
				healthComponent.health = 0;
				self.entityManager.removeComponent(entity, PlayerControlledComponent);
				self.entityManager.removeComponent(entity, VelocityComponent);
				self.entityManager.getComponent(entity, DisplayComponent).colour = (128, 128, 128);



class CollisionSystem(System):
	def update(self, deltaTime):
		collidableEntities = list(self.getAllEntitiesPossessingComponents(PositionComponent, CollisionComponent));
		
		for i in range(len(collidableEntities)):
			for j in range(i + 1, len(collidableEntities)):
				ent0 = collidableEntities[i];
				ent1 = collidableEntities[j];

				velocityComponent0 = None;
				velocityComponent1 = None;
				if (self.entityHasComponent(ent0, VelocityComponent)):
					velocityComponent0 = self.getComponent(ent0, VelocityComponent);
				if (self.entityHasComponent(ent1, VelocityComponent)):
					velocityComponent1 = self.getComponent(ent1, VelocityComponent);

				# Don't care about static objects colliding
				# Needs 1 or both to have a velocity component
				if ((velocityComponent0 is None) and (velocityComponent1 is None)):
					continue;

				velocity0 = velocityComponent0.velocity if velocityComponent0 is not None else Vector2(0, 0);
				velocity1 = velocityComponent1.velocity if velocityComponent1 is not None else Vector2(0, 0);

				positionComponent0 = self.getComponent(ent0, PositionComponent);
				positionComponent1 = self.getComponent(ent1, PositionComponent);
				collisionComponent0 = self.getComponent(ent0, CollisionComponent);
				collisionComponent1 = self.getComponent(ent1, CollisionComponent);

				p0 = positionComponent0.position;
				p1 = positionComponent1.position;
				s0 = collisionComponent0.shape;
				s1 = collisionComponent1.shape;

				collision = isCollision(p0, s0, p1, s1);

				if (collision):
					velocity0 = Vector2(0, 0);
					velocity1 = Vector2(0, 0);

				if (velocityComponent0 is not None):
					velocityComponent0.velocity = velocity0;
				if (velocityComponent1 is not None):
					velocityComponent1.velocity = velocity1;



class ForcesSystem(System):
	def update(self, deltaTime):
		forceEntities = self.getAllEntitiesPossessingComponents(VelocityComponent, AccelerationComponent, ForceComponent, MassComponent);

		for entity in forceEntities:
			velocityComponent = self.getComponent(entity, VelocityComponent);
			accelerationComponent = self.getComponent(entity, AccelerationComponent);
			forceComponent = self.getComponent(entity, ForceComponent);
			massComponent = self.getComponent(entity, MassComponent);


			mu = 1;
			if (velocityComponent.velocity.magnitude() == 0):
				mu = 0.1;
			r = massComponent.mass;
			direction = velocityComponent.velocity.normalized() * -1;
			friction = (mu * r + (velocityComponent.velocity.magnitudeSquared() / 2)) * direction;

			forceComponent.force += friction;

			accelerationComponent.acceleration = forceComponent.force / massComponent.mass;
			velocityComponent.velocity = velocityComponent.velocity + (accelerationComponent.acceleration * deltaTime);
			forceComponent.force = Vector2(0, 0);


			# if (velocityComponent.velocity.magnitude() > 200):
			# 	velocityComponent.velocity = velocityComponent.velocity.setMagnitudeTo(200);



class MovementSystem(System):
	def update(self, deltaTime):
		movingEntities = self.getAllEntitiesPossessingComponents(PositionComponent, VelocityComponent);
		
		for entity in movingEntities:
			positionComponent = self.getComponent(entity, PositionComponent);
			velocityComponent = self.getComponent(entity, VelocityComponent);

			newPosition = positionComponent.position + velocityComponent.velocity * deltaTime;
			positionComponent.position = newPosition;

			

def buildRectFromVectors(position, size):
	rect = pygame.Rect(position.x, position.y, size.x, size.y);
	return rect;



class RenderSystem(System):
	def __init__(self, entityManager, displaySurface):
		super(RenderSystem, self).__init__(entityManager);
		self.displaySurface = displaySurface;



	def update(self, _):
		self.displaySurface.fill((0, 0, 0));

		renderableEntities = self.getAllEntitiesPossessingComponents(PositionComponent, DisplayComponent);

		for entity in renderableEntities:
			positionComponent = self.getComponent(entity, PositionComponent);
			displayComponent = self.getComponent(entity, DisplayComponent);

			position = positionComponent.position;

			if (displayComponent.shape == "Rect"):
				rect = buildRectFromVectors(position, displayComponent.size);
				pygame.draw.rect(self.displaySurface, displayComponent.colour, rect);

			elif (displayComponent.shape == "Circle"):
				radius = displayComponent.size;

				centre = Vector2(int(position.x + radius), int(position.y + radius));

				pygame.draw.circle(self.displaySurface, displayComponent.colour, centre, radius);

			if (self.entityHasComponent(entity, HealthComponent)):
				healthComponent = self.getComponent(entity, HealthComponent);
				maxHealth = healthComponent.maxHealth;
				health = healthComponent.health;

				healthPercent = health / maxHealth;

				healthBarHeight = 5;
				healthBarWidth = 30;

				healthBarHeadGap = 5;
				healthBarTopLeft = (position.x, position.y - (healthBarHeadGap + healthBarHeight));

				healthBarSize = (int(healthBarWidth * healthPercent), healthBarHeight);

				pygame.draw.rect(self.displaySurface, (255, 0, 0), (healthBarTopLeft, (healthBarWidth, healthBarHeight)));
				if (healthPercent > 0):
					pygame.draw.rect(self.displaySurface, (0, 255, 0), (healthBarTopLeft, healthBarSize));