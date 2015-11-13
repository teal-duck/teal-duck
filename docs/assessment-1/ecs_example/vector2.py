from collections import namedtuple;
import math;

class Vector2(namedtuple("Vector2", ["x", "y"])):
	def magnitudeSquared(self):
		return self.x * self.x + self.y * self.y;

	def magnitude(self):
		return math.sqrt(self.x * self.x + self.y * self.y);

	def normalized(self):
		magnitude = self.magnitude();
		if (magnitude == 0):
			return Vector2(0, 0);
		else:
			return self / self.magnitude();

	def setMagnitudeTo(self, magnitude):
		return self.normalized() * magnitude;

	def __add__(self, other):
		return Vector2(self.x + other.x, self.y + other.y);
	__radd__ = __add__;

	def __sub__(self, other):
		return Vector2(self.x - other.x, self.y - other.y);

	__rsub__ = __sub__;

	def __mul__(self, scalar):
		return Vector2(scalar * self.x, scalar * self.y);
	__rmul__ = __mul__;

	def __div__(self, scalar):
		x = self.x;
		if (x != 0):
			x = x / scalar;
		y = self.y;
		if (y != 0):
			y = y / scalar;
		return Vector2(x, y);
	__rdiv__ = __div__;

	def __str__(self):
		return "Vector2(" + str(self.x) + ", " + str(self.y) + ")";

	def __repr__(self):
		return self.__str__();
