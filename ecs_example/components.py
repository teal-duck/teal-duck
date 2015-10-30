# from collections import namedtuple;
# PositionComponent = namedtuple("PositionComponent", ["position"]);
# VelocityComponent = namedtuple("VelocityComponent", ["velocity"]);
# SizeComponent = namedtuple("SizeComponent", ["size"]);


from ecs import Component;



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