import pygame;
from ecs import System;
from components import *;
from vector2 import Vector2;
import math;



class LogicSystem(System):
	def update(self, deltaTime):
		for entity in self.getAllEntitiesPossessingComponents(PlayerControlledComponent, VelocityComponent):
			controller = self.getComponent(entity, PlayerControlledComponent);
			velocityComponent = self.getComponent(entity, VelocityComponent);
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

			speed = 200;
			velocityComponent.velocity = Vector2(dx, dy).setMagnitudeTo(speed);

		for entity in self.getAllEntitiesPossessingComponents(AIControlledComponent, PositionComponent, VelocityComponent):
			controller = self.getComponent(entity, AIControlledComponent);
			positionComponent = self.getComponent(entity, PositionComponent);
			velocityComponent = self.getComponent(entity, VelocityComponent);

			players = self.getAllEntitiesPossessingComponents(PlayerControlledComponent, PositionComponent);
			if (len(players) > 0):
				player = players.pop();

				playerPositionComponent = self.getComponent(player, PositionComponent);

				direction = playerPositionComponent.position - positionComponent.position;
				speed = 100;
				vecToMove = direction.setMagnitudeTo(speed);
				velocityComponent.velocity = vecToMove;
			else:
				velocityComponent.velocity = Vector2(0, 0);


		for entity in self.getAllEntitiesPossessingComponents(HealthComponent):
			healthComponent = self.getComponent(entity, HealthComponent);
			healthComponent.health = healthComponent.health - 10 * deltaTime;

			if (healthComponent.health <= 0):
				healthComponent.health = 0;
				self.entityManager.removeComponent(entity, PlayerControlledComponent);
				self.entityManager.removeComponent(entity, VelocityComponent);
				self.entityManager.getComponent(entity, DisplayComponent).colour = (128, 128, 128);



def collides(entityManager, ent1, ent2):
	pass;



class MovementSystem(System):
	def update(self, deltaTime):
		movingEntities = self.getAllEntitiesPossessingComponents(PositionComponent, VelocityComponent);
		
		for entity in movingEntities:
			positionComponent = self.getComponent(entity, PositionComponent);
			velocityComponent = self.getComponent(entity, VelocityComponent);

			newPosition = positionComponent.position + velocityComponent.velocity * deltaTime;

			if (entityIsCollidable(self.entityManager, entity)):
				pass;

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