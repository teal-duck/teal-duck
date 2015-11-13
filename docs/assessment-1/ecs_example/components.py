from pygame.locals import *;

from ecs import Component;
from vector2 import Vector2;



class PositionComponent(Component):
	def __init__(self, position = Vector2(0, 0)):
		self.position = position;

	def __str__(self):
		return "PositionComponent(position=" + str(self.position) + ")";
	__repr__ = __str__;



class VelocityComponent(Component):
	def __init__(self, velocity = Vector2(0, 0)):
		self.velocity = velocity;

	def __str__(self):
		return "VelocityComponent(velocity=" + str(self.velocity) + ")";
	__repr__ = __str__;



class AccelerationComponent(Component):
	def __init__(self, acceleration = Vector2(0, 0)):
		self.acceleration = acceleration;

	def __str__(self):
		return "AccelerationComponent(acceleration=" + str(self.acceleration) + ")";
	__repr__ = __str__;



class ForceComponent(Component):
	def __init__(self, force = Vector2(0, 0)):
		self.force = force;

	def __str__(self):
		return "ForceComponent(force=" + str(self.force) + ")";
	__repr__ = __str__;



class MassComponent(Component):
	def __init__(self, mass = 1):
		self.mass = mass;

	def __str__(self):
		return "MassComponent(mass=" + str(self.mass) + ")";
	__repr__ = __str__;



class HealthComponent(Component):
	def __init__(self, maxHealth, startingHealth = None):
		self.maxHealth = maxHealth;
		self.health = maxHealth if startingHealth is None else startingHealth;

	def __str__(self):
		return "HealthComponent(maxHealth=" + str(self.maxHealth) + ", health=" + str(self.health) + ")";
	__repr__ = __str__;



class CollisionComponent(Component):
	def __init__(self, collisionShape):
		self.shape = collisionShape;

	def __str__(self):
		return "CollisionComponent(collisionShape=" + str(self.shape) + ")";
	__repr__ = __str__;



class DisplayComponent(Component):
	def __init__(self, colour, shape, size):
		self.colour = colour;
		self.shape = shape;
		self.size = size;

	def __str__(self):
		return "DisplayComponent(colour=" + str(self.colour) + ", shape=" + str(self.shape) + ", size=" + str(self.size) + ")";
	__repr__ = __str__;



class PlayerControlledComponent(Component):
	def __init__(self):
		self.controls = {
			"up": K_w,
			"down": K_s,
			"left": K_a,
			"right": K_d
		};

	def __str__(self):
		return "PlayerControlledComponent(controls=" + str(self.controls) + ")";
	__repr__ = __str__;



class AIControlledComponent(Component):
	def __init__(self):
		pass;

	def __str__(self):
		return "AIControlledComponent()";
	__repr__ = __str__;





# class AABBComponent(Component):
# 	def __init__(self, size):
# 		self.size = size;

# 	@property
# 	def width(self):
# 		return self.size.x;

# 	@property
# 	def height(self):
# 		return self.size.y;

# 	def __str__(self):
# 		return "AABBComponent(size=" + str(self.size) + ")";
# 	__repr__ = __str__;



# class CircleCollisionComponent(Component):
# 	def __init__(self, radius):
# 		self.radius = radius;

# 	def __str__(self):
# 		return "CircleCollisionComponent(radius=" + str(self.radius) + ")";
# 	__repr__ = __str__;



# def entityIsCollidable(entityManager, entity):
# 	return entityManager.entityHasComponent(entity, AABBComponent) or entityManager.entityHasComponent(entity, CircleCollisionComponent);



# def getCollidableEntities(entityManager):
# 	return entityManager.getAllEntitiesPossessingComponents(AABBComponent, CircleCollisionComponent);
