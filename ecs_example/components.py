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



class DisplayComponent(Component):
	def __init__(self, colour, shape):
		self.colour = colour;
		self.shape = shape;



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