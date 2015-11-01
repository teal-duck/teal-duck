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
			player = players.pop();

			playerPositionComponent = self.getComponent(player, PositionComponent);

			direction = playerPositionComponent.position - positionComponent.position;
			speed = 100;
			vecToMove = direction.setMagnitudeTo(speed);
			velocityComponent.velocity = vecToMove;




def collides(ent1, ent2):
	pass;



class MovementSystem(System):
	def update(self, deltaTime):
		movingEntities = self.getAllEntitiesPossessingComponents(PositionComponent, VelocityComponent);
		
		for entity in movingEntities:
			positionComponent = self.getComponent(entity, PositionComponent);
			velocityComponent = self.getComponent(entity, VelocityComponent);

			newPosition = positionComponent.position + velocityComponent.velocity * deltaTime;

			if (self.entityManager.entityHasComponent(entity, SizeComponent)):
				sizeComponent = self.getComponent(entity, SizeComponent);

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

		renderableEntities = self.getAllEntitiesPossessingComponents(PositionComponent, SizeComponent, DisplayComponent);

		for entity in renderableEntities:
			positionComponent = self.getComponent(entity, PositionComponent);
			sizeComponent = self.getComponent(entity, SizeComponent);
			displayComponent = self.getComponent(entity, DisplayComponent);

			if (displayComponent.shape == "Rect"):
				rect = buildRectFromVectors(positionComponent.position, sizeComponent.size);
				pygame.draw.rect(self.displaySurface, displayComponent.colour, rect);

			elif (displayComponent.shape == "Circle"):
				pygame.draw.circle(self.displaySurface, displayComponent.colour, (int(positionComponent.position.x + 0.5), int(positionComponent.position.y + 0.5)), sizeComponent.size.x);