from box import Box;

# http://www.gamedev.net/page/resources/_/technical/game-programming/swept-aabb-collision-detection-and-response-r3084


# float SweptAABB(Box b1, Box b2, float& normalX, float& normalY)

def sweptAABB(b1, b2):
	"""Box x Box -> (Float, (Float, Float))"""
	normalX = 0;
	normalY = 0;

	"""
	float xInvEntry, yInvEntry;
	float xInvExit, yInvExit;

	// find the distance between the objects on the near and far sides for both x and y
	if (b1.vx > 0.0f) {
		xInvEntry = b2.x - (b1.x + b1.w);
		xInvExit = (b2.x + b2.w) - b1.x;
	} else {
		xInvEntry = (b2.x + b2.w) - b1.x;
		xInvExit = b2.x - (b1.x + b1.w);
	}

	if (b1.vy > 0.0f) {
		yInvEntry = b2.y - (b1.y + b1.h);
		yInvExit = (b2.y + b2.h) - b1.y;
	} else {
		yInvEntry = (b2.y + b2.h) - b1.y;
		yInvExit = b2.y - (b1.y + b1.h);
	}
	"""

	xInvEntry = 0;
	yInvEntry = 0;
	xInvExit = 0;
	yInvExit = 0;

	if (b1.vx > 0):
		xInvEntry = b2.x - (b1.x + b1.w);
		xInvExit = (b2.x + b2.w) - b1.x;
	else:
		xInvEntry = (b2.x + b2.w) - b1.x;
		xInvExit = b2.x - (b1.x + b1.w);
	

	if (b1.vy > 0):
		yInvEntry = b2.y - (b1.y + b1.h);
		yInvExit = (b2.y + b2.h) - b1.y;
	else:
		yInvEntry = (b2.y + b2.h) - b1.y;
		yInvExit = b2.y - (b1.y + b1.h);

	"""
	// find time of collision and time of leaving for each axis (if statement is to prevent divide by zero)
	float xEntry, yEntry;
	float xExit, yExit;

	if (b1.vx == 0.0f) {
		xEntry = -std::numeric_limits<float>::infinity();
		xExit = std::numeric_limits<float>::infinity();
	} else {
		xEntry = xInvEntry / b1.vx;
		xExit = xInvExit / b1.vx;
	}

	if (b1.vy == 0.0f) {
		yEntry = -std::numeric_limits<float>::infinity();
		yExit = std::numeric_limits<float>::infinity();
	} else {
		yEntry = yInvEntry / b1.vy;
		yExit = yInvExit / b1.vy;
	}
	"""
	xEntry = 0;
	yEntry = 0;
	xExit = 0;
	yExit = 0;

	if (b1.vx == 0):
		xEntry = -float("inf");
		xExit = float("inf");
	else:
		xEntry = xInvEntry / b1.vx;
		xExit = xInvExit / b1.vx;

	if (b1.vy == 0):
		yEntry = -float("inf");
		yExit = float("inf");
	else:
		yEntry = yInvEntry / b1.vy;
		yExit = yInvExit / b1.vy;
	
	if (yEntry > 1):
		yEntry = -float("inf");
	if (xEntry > 1):
		xEntry = -float("inf");


	"""
	// find the earliest/latest times of collision
	float entryTime = std::max(xEntry, yEntry);
	float exitTime = std::min(xExit, yExit);
	"""

	entryTime = max(xEntry, yEntry);
	exitTime = min(xExit, yExit);

	"""
	// if there was no collision
	if (entryTime > exitTime || xEntry < 0.0f && yEntry < 0.0f || xEntry > 1.0f || yEntry > 1.0f) {
		normalX = 0.0f;
		normalY = 0.0f;
		return 1.0f;
	} else {
		// if there was a collision
		// calculate normal of collided surface
		if (xEntry > yEntry) {
			if (xInvEntry < 0.0f) {
				normalX = 1.0f;
				normalY = 0.0f;
			} else {
				normalX = -1.0f;
				normalY = 0.0f;
			}
		} else {
			if (yInvEntry < 0.0f) {
				normalX = 0.0f;
				normalY = 1.0f;
			} else {
				normalX = 0.0f;
				normalY = -1.0f;
			}
		}

		// return the time of collision
		return entryTime;
	}
	"""

	"""
	if (entryTime > exitTime)
		return 1.0f; // This check was correct.
	if (entryX < 0.0f && entryY < 0.0f)
		return 1.0f;
	if (entryX < 0.0f) {
		// Check that the bounding box started overlapped or not.
		if (s.max.x < t.min.x || s.min.x > t.max.x)
			return 1.0f;
	}
	if (entryY < 0.0f) {
		// Check that the bounding box started overlapped or not.
		if (s.max.y < t.min.y || s.min.y > t.max.y)
			return 1.0f;
	}"""


	# if (entryTime > exitTime or xEntry < 0 and yEntry < 0 or xEntry > 1 or yEntry > 1):
	# 	normalX = 0;
	# 	normalY = 0;
	# 	return (1, (normalX, normalY));
	# else:

	normalX = 0;
	normalY = 0;

	if (entryTime > exitTime):
		return (1, (0, 0));
	
	if (xEntry < 0 and yEntry < 0):
		return (1, (0, 0));

	if (xEntry < 0):
		if (b1.x + b1.w < b2.x or b1.x > b2.x + b2.w):
			return (1, (0, 0));

	if (yEntry < 0):
		if (b1.y + b1.h < b2.y or b1.y > b2.y + b2.h):
			return (1, (0, 0));

	if (xEntry > yEntry):
		if (xInvEntry < 0):
			normalX = 1;
			normalY = 0;
		else:
			normalX = -1;
			normalY = 0;
	else:
		if (yInvEntry < 0):
			normalX = 0;
			normalY = 1;
		else:
			normalX = 0;
			normalY = -1;

	return (entryTime, (normalX, normalY));
	


"""Box GetSweptBroadphaseBox(Box b){
	Box broadphasebox;
	broadphasebox.x = b.vx > 0 ? b.x : b.x + b.vx;
	broadphasebox.y = b.vy > 0 ? b.y : b.y + b.vy;
	broadphasebox.w = b.vx > 0 ? b.vx + b.w : b.w - b.vx;
	broadphasebox.h = b.vy > 0 ? b.vy + b.h : b.h - b.vy;

	return broadphasebox;
}
"""


def getSweptBroadphaseBox(b):
	x = b.x if b.vx > 0 else b.x + b.vx;
	y = b.y if b.vy > 0 else b.y + b.vy;
	w = b.vx + b.w if b.vx > 0 else b.w - b.vx;
	h = b.vy + b.h if b.vy > 0 else b.h - b.vy;

	return Box(x, y, w, h, 0, 0);


"""
bool AABBCheck(Box b1, Box b2) {
	return !(b1.x + b1.w < b2.x || b1.x > b2.x + b2.w || b1.y + b1.h < b2.y || b1.y > b2.y + b2.h);
}
"""

def aabbCheck(b1, b2):
	return not (b1.x + b1.w < b2.x or b1.x > b2.x + b2.w or b1.y + b1.h < b2.y or b1.y > b2.y + b2.h);