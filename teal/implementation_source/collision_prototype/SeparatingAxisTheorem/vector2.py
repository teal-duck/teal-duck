import math;



class Vector2(object):
	def __init__(self, x = 0, y = 0):
		self.x = x;
		self.y = y;

	def set(self, x, y):
		self.x = x;
		self.y = y;

	@property
	def magnitudeSquared(self):
		return self.x * self.x + self.y * self.y;

	@property
	def magnitude(self):
		return math.sqrt(self.magnitudeSquared);

	@property
	def unit(self):
		magnitude = self.magnitude;
		if (magnitude == 0):
			return Vector2(0, 0);
		else:
			return Vector2(self.x / magnitude, self.y / magnitude);

	def dot(self, other):
		return self.x * other.x + self.y * other.y;

	def projectInDirection(self, other):
		return self.dot(other) * other.unit;

	@property
	def perpendicular(self):
		return Vector2(-self.y, self.x);

	@property
	def tuple(self):
		return (self.x, self.y);

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
		if (scalar == 0):
			return Vector2(0, 0);
		else:
			return Vector2(self.x / scalar, self.y / scalar);
	__rdiv__ = __div__;

	def __str__(self):
		return "Vector2(" + str(self.x) + ", " + str(self.y) + ")";

	def __repr__(self):
		return self.__str__();