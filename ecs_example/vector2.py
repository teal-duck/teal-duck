from collections import namedtuple;

class Vector2(namedtuple("Vector2", ["x", "y"])):
	def __add__(self, other):
		return Vector2(self.x + other.x, self.y + other.y);
	__radd__ = __add__;

	def __mul__(self, scalar):
		return Vector2(scalar * self.x, scalar * self.y);
	__rmul__ = __mul__;

	def __str__(self):
		return "Vector2(" + str(self.x) + ", " + str(self.y) + ")";

	def __repr__(self):
		return self.__str__();