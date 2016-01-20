from vector2 import Vector2;


class AABB(object):
	def __init__(self, position, size):
		self.position = position;
		self.size = size;

	@property
	def x(self):
		return self.position.x;

	@property	
	def y(self):
		return self.position.y;

	@property
	def width(self):
		return self.size.x;

	@property
	def height(self):
		return self.size.y;

	@property
	def left(self):
		return self.x;

	@property
	def right(self):
		return self.x + self.width;

	@property
	def top(self):
		return self.y;

	@property
	def bottom(self):
		return self.y + self.height;

	@property
	def centerX(self):
		return self.x + (self.width / 2);

	@property
	def centerY(self):
		return self.y + (self.height / 2);

	@property
	def center(self):
		return Vector2(self.centerX, self.centerY);

	@property
	def tuple(self):
		return (self.position.tuple, self.size.tuple);

	def intersectsPoint(self, point):
		"""Vector2 -> Bool"""
		return ((point.x > self.left)
			and (point.x < self.right)
			and (point.y > self.top)
			and (point.y < self.bottom));

	def __str__(self):
		return "AABB(" + self.x + ", " + self.y + ", " + self.w + ", " + self.h + ")";

	def __repr__(self):
		return self.__str__();