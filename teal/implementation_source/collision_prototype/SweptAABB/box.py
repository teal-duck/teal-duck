# http://www.gamedev.net/page/resources/_/technical/game-programming/swept-aabb-collision-detection-and-response-r3084

class Box(object):
	def __init__(self, x, y, w, h, vx, vy):
		self.x = x;
		self.y = y;
		self.w = w;
		self.h = h;
		self.vx = vx;
		self.vy = vy;