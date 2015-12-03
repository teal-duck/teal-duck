from aabb import AABB;
from vector2 import Vector2;


class Circle(object):
	def __init__(self, position, radius):
		self.position = position;
		self.radius = radius;

	@property
	def width(self):
		return self.radius;

	@property
	def height(self):
		return self.radius;

	@property
	def center(self):
		return self.position;

	@property
	def centerX(self):
		return self.center.x;

	@property
	def centerY(self):
		return self.center.y;

	@property
	def x(self):
		return self.position.x;

	@property
	def y(self):
		return self.position.y;

	@property
	def left(self):
		return self.position.x - self.radius;

	@property
	def top(self):
		return self.position.y - self.radius;

	@property
	def right(self):
		return self.position.x + self.radius;

	@property
	def bottom(self):
		return self.position.y + self.radius;

	@property
	def topLeft(self):
		return Vector2(self.left, self.top);

	@property
	def topRight(self):
		return Vector2(self.right, self.top);

	@property
	def bottomLeft(self):
		return Vector2(self.left, self.bottom);

	@property
	def bottomRight(self):
		return Vector2(self.right, self.bottom);

	@property
	def topMiddle(self):
		return Vector2(self.centerX, self.top);

	@property
	def leftMiddle(self):
		return Vector2(self.left, self.centerY);

	@property
	def bottomMiddle(self):
		return Vector2(self.centerX, self.bottom);
		
	@property
	def rightMiddle(self):
		return Vector2(self.right, self.centerY);

	@property
	def diameter(self):
		return self.radius * 2;

	@property
	def aabb(self):
		return AABB(self.position - Vector2(self.radius, self.radius), Vector2(self.diameter, self.diameter));

	def intersectsPoint(self, point):
		return ((point - self.position).magnitudeSquared < self.radius * self.radius);

	def __str__(self):
		return "Circle(" + str(self.position) + ", " + str(self.radius) + ")";

	def __repr__(self):
		return self.__str__();