from vector2 import Vector2;



class CollisionShape(object):
	pass;



class AABBShape(CollisionShape):
	def __init__(self, size):
		self.size = size;



class CircleShape(CollisionShape):
	def __init__(self, radius):
		self.radius = radius;



def aabbToAABBCollision(pos0, size0, pos1, size1):
	# print("AABB to AABB collision");
	return False;



def aabbToCircleCollision(pos0, size0, pos1, radius):
	#  print("AABB to Circle collision");
	return False;



# http://gamedevelopment.tutsplus.com/tutorials/when-worlds-collide-simulating-circle-circle-collisions--gamedev-769

def circleToCircleCollision(pos0, radius0, pos1, radius1):
	# print("Circle to Circle collision");
	return False;
	# return (pos1 - pos0).magnitudeSquared() < (radius0 + radius1) * (radius0 + radius1);



def getCircleCentre(topLeftCorner, radius):
	return topLeftCorner + Vector2(radius, radius);



def isCollision(pos0, shape0, pos1, shape1):
	if (shape0.__class__ == AABBShape):
		if (shape1.__class__ == AABBShape):
			return aabbToAABBCollision(pos0, shape0.size, pos1, shape1.size);
		elif (shape1.__class__ == CircleShape):
			return aabbToCircleCollision(pos0, shape0.size, getCircleCentre(pos1, shape1.radius), shape1.radius);

	elif (shape1.__class__ == CircleShape):
		if (shape1.__class__ == AABBShape):
			return aabbToCircleCollision(pos1, shape1.size, getCircleCentre(pos0, shape0.radius), shape0.radius);
		elif (shape1.__class__ == CircleShape):
			return circleToCircleCollision(getCircleCentre(pos0, shape0.radius), shape0.radius, getCircleCentre(pos1, shape1.radius), shape1.radius);
