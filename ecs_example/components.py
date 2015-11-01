# from collections import namedtuple;
# PositionComponent = namedtuple("PositionComponent", ["position"]);
# VelocityComponent = namedtuple("VelocityComponent", ["velocity"]);
# SizeComponent = namedtuple("SizeComponent", ["size"]);


from ecs import Component;
from pygame.locals import *;



class PositionComponent(Component):
	def __init__(self, position):
		self.position = position;



class VelocityComponent(Component):
	def __init__(self, velocity):
		self.velocity = velocity;



class SizeComponent(Component):
	def __init__(self, size):
		self.size = size;



class AABBComponent(Component):
	def __init__(self, size):
		self.size = size;

	@property
	def width(self):
		return self.size.x;

	@property
	def height(self):
		return self.size.y;



class CircleCollisionComponent(Component):
	def __init__(self, radius):
		self.radius = radius;



def entityIsCollidable(entityManager, entity):
	return entityManager.entityHasComponent(entity, AABBComponent) or entityManager.entityHasComponent(entity, CircleCollisionComponent);



def getCollidableEntities(entityManager):
	return entityManager.getAllEntitiesPossessingComponents(AABBComponent, CircleCollisionComponent);



class DisplayComponent(Component):
	def __init__(self, colour, shape, size):
		self.colour = colour;
		self.shape = shape;
		self.size = size;



class PlayerControlledComponent(Component):
	def __init__(self):
		self.controls = {
			"up": K_w,
			"down": K_s,
			"left": K_a,
			"right": K_d
		};



class AIControlledComponent(Component):
	def __init__(self):
		pass;