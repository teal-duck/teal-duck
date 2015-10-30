import pygame;
from ecs import System;
from components import *;
from vector2 import Vector2;



class MovementSystem(System):
	def update(self, deltaTime):
		movingEntities = self.getAllEntitiesPossessingComponents([PositionComponent, VelocityComponent]);
		
		for entity in movingEntities:
			positionComponent = self.getComponent(entity, PositionComponent);
			velocityComponent = self.getComponent(entity, VelocityComponent);

			newPosition = positionComponent.position + velocityComponent.velocity * deltaTime;

			if (newPosition.x > 300):
				newPosition = Vector2(300, newPosition.y);

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

		renderableEntities = self.getAllEntitiesPossessingComponents([PositionComponent, SizeComponent, DisplayComponent]);

		for entity in renderableEntities:
			positionComponent = self.getComponent(entity, PositionComponent);
			sizeComponent = self.getComponent(entity, SizeComponent);
			displayComponent = self.getComponent(entity, DisplayComponent);

			if (displayComponent.shape == "Rect"):
				rect = buildRectFromVectors(positionComponent.position, sizeComponent.size);
				pygame.draw.rect(self.displaySurface, displayComponent.colour, rect);

			elif (displayComponent.shape == "Circle"):
				pygame.draw.circle(self.displaySurface, displayComponent.colour, positionComponent.position, sizeComponent.size.x);