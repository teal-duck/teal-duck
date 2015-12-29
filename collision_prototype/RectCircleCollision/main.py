"""
Based on "calculating the projection vector" demo on http://www.metanetsoftware.com/technique/tutorialA.html
http://www.showmycode.com/?752955611975f706bdfabb7153315711
"""

import pygame;
from pygame.locals import *;
import sys;

from aabb import AABB;
from circle import Circle;
from vector2 import Vector2;
import math;



screenWidth = 600;
screenHeight = screenWidth;
screenSize = Vector2(screenWidth, screenHeight);
screenCenter = screenSize / 2;
title = "Rect Circle Collision";

fps = 60;

backgroundColour = (200, 200, 200);
alpha = 64;
outlineThickness = 1;
centerThickness = 2;

innerLineColour = (128, 128, 128);
innerLineThickness = 1;

outerLineThickness = 3;

overlapLineColour = (80, 80, 80);
overlapLineCollisionColour = (80, 0, 80);
overlapLineThickness = 2;
overlapLineCollisionThickness = 4;
centerLinePosition = Vector2(screenWidth - outerLineThickness * 2, screenHeight - outerLineThickness * 2);

centerLineColour = (0, 80, 0);
centerLinePosition = Vector2(screenWidth - outerLineThickness * 4, screenHeight - outerLineThickness * 4);

redLineColour = (80, 0, 0);
redLinePosition = Vector2(screenWidth - outerLineThickness * 6, screenHeight - outerLineThickness * 6);

blueLineColour = (0, 0, 80);
blueLinePosition = Vector2(screenWidth - outerLineThickness * 8, screenHeight - outerLineThickness * 8);


lineDistanceFromScreenCenter = (screenWidth / 2) - 60;


circleColour = (255, 0, 0);
boxColour = (0, 0, 255);

minBounds = Vector2(20, 20);
maxBounds = screenSize - minBounds;



def drawAABB(displaySurface, aabb, colour, alpha, outlineThickness, centerThickness):
	alphaSurface = pygame.Surface(aabb.size.intTuple)
	alphaSurface.set_alpha(alpha)
	alphaSurface.fill(colour);

	displaySurface.blit(alphaSurface, aabb.position.intTuple); 

	pygame.draw.rect(displaySurface, colour, aabb.intTuple, outlineThickness);
	pygame.draw.circle(displaySurface, colour, aabb.center.intTuple, centerThickness);


def drawCircle(displaySurface, circle, colour, alpha, outlineThickness, centerThickness):
	ck = (127, 33, 33);
	alphaSurface = pygame.Surface((circle.diameter, circle.diameter));
	alphaSurface.fill(ck);
	alphaSurface.set_colorkey(ck);
	pygame.draw.circle(alphaSurface, colour, (circle.radius, circle.radius), circle.radius);
	alphaSurface.set_alpha(alpha);

	displaySurface.blit(alphaSurface, circle.topLeft.intTuple);

	pygame.draw.circle(displaySurface, colour, circle.position.intTuple, circle.radius, outlineThickness);
	pygame.draw.circle(displaySurface, colour, circle.position.intTuple, centerThickness);



def updateMouse(lastMouseState, holdingObject, holdingOffset, box, circle, minBounds, maxBounds):
	mouseState = pygame.mouse.get_pressed();
	if (mouseState[0]):
		mousePosition = Vector2(*pygame.mouse.get_pos());

		if (holdingObject == None):
			if (lastMouseState[0] == False):
				if (circle.intersectsPoint(mousePosition)):
					holdingObject = circle;

				elif (box.intersectsPoint(mousePosition)):
					holdingObject = box;

				if (holdingObject != None):
					holdingOffset = mousePosition - holdingObject.position;
		else:
			newPosition = mousePosition - holdingOffset;
			width = holdingObject.width;
			height = holdingObject.height;

			if (newPosition.x < minBounds.x):
				newPosition.x = minBounds.x;
			if (newPosition.x + width > maxBounds.x):
				newPosition.x = maxBounds.x - width;
			
			if (newPosition.y < minBounds.y):
				newPosition.y = minBounds.y;
			if (newPosition.y + height > maxBounds.y):
				newPosition.y = maxBounds.y - height;

			holdingObject.position = newPosition;
	else:
		holdingObject = None;
		holdingOffset = Vector2();

	return (mouseState, holdingObject, holdingOffset);
	


def projectPointOnLine(point, linePoint, lineDirection):
	# return linePoint + ((point - linePoint).dot(lineDirection) / lineDirection.dot(lineDirection)) * lineDirection;
	directionLengthSquared = lineDirection.dot(lineDirection);
	if (directionLengthSquared == 0):
		return Vector2(0, 0);
	else:
		return linePoint + lineDirection * ((point - linePoint).dot(lineDirection) / directionLengthSquared);



def overlap(min1, max1, min2, max2):
	return max(0, min(max1, max2) - max(min1, min2));



def getOverlapForPointsOnLine(lineDirection, b0p0, b0p1, b1p0, b1p1):
	b0p0AlongLine = b0p0.dot(lineDirection);
	b0p1AlongLine = b0p1.dot(lineDirection);
	b1p0AlongLine = b1p0.dot(lineDirection);
	b1p1AlongLine = b1p1.dot(lineDirection);

	b0Left = min(b0p0AlongLine, b0p1AlongLine);
	b0Right = max(b0p0AlongLine, b0p1AlongLine);

	b1Left = min(b1p0AlongLine, b1p1AlongLine);
	b1Right = max(b1p0AlongLine, b1p1AlongLine);

	return overlap(b0Left, b0Right, b1Left, b1Right);



def getCloserAndFurtherPoints(aabb, circle):
	closestX = 0;
	closestY = 0;
	furthestX = 0;
	furthestY = 0;

	if (circle.centerX < aabb.left):
		closestX = aabb.left;
		furthestX = aabb.right;
	elif (circle.centerX > aabb.right):
		closestX = aabb.right;
		furthestX = aabb.left;
	else:
		insideX = True;
		closestX = circle.centerX;
		furthestX = circle.centerX;

	if (circle.centerY < aabb.top):
		closestY = aabb.top;
		furthestY = aabb.bottom;
	elif (circle.centerY > aabb.bottom):
		closestY = aabb.bottom;
		furthestY = aabb.top;
	else:
		closestY = circle.centerY;
		furthestY = circle.centerY;

	return Vector2(closestX, closestY), Vector2(furthestX, furthestY);

"""
float abs_cos_angle= fabs(cos(angle));
float abs_sin_angle= fabs(sin(angle));
if (width/2/abs_cos_angle <= height/2/abs_sin_angle)
{
    magnitude= fabs(width/2/abs_cos_angle);
}
else
{
    magnitude= height/2/abs_sin_angle;
}"""

"""
double magnitude;
double abs_cos_angle= fabs(cos(angle));
double abs_sin_angle= fabs(sin(angle));
if (width/2*abs_sin_angle <= height/2*abs_cos_angle)
{
	magnitude= width/2/abs_cos_angle;
}
else
{
	magnitude= height/2/abs_sin_angle;
}"""

def vectorFromCenterOfAABBToEdge(aabb, pointInAABB):
	vec = pointInAABB - aabb;
	angle = math.atan2(vec.y, vec.x);

	cosAngle = abs(math.cos(angle));
	sinAngle = abs(math.sin(angle));

	magnitude = 0;
	if (aabb.width / 2.0 * sinAngle <= aabb.height / 2.0 * cosAngle):
		magnitude = aabb.width / 2 / cosAngle;
	else:
		magnitude = aabb.height / 2 / sinAngle;

	return vec.unit * magnitude;



def aabbCircleCollision(aabb, circle):
	"""(AABB, Circle) -> (Bool, Vector2, Float)
	Returns (True, normal, smallestOverlap) if there is a collision, else (False, Vector2.(0, 0), 0)"""

	if ((circle.centerX > aabb.left) and (circle.centerX < aabb.right) and (circle.centerY > aabb.top) and (circle.centerY < aabb.bottom)):
		# projection = (circle.center - aabb.center).unit;

		lengthToEdgeInDirection = 0;
		vec = vectorFromCenterOfAABBToEdge(aabb, circle.center);
		projection = vec.unit;
		lengthToEdgeInDirection = vec.magnitude;

		

		return True, projection, lengthToEdgeInDirection; # + circle.radius;

	else:
		closerPoint, furtherPoint = getCloserAndFurtherPoints(aabb, circle);

		if (closerPoint != None):
			projection = (circle.center - closerPoint).unit;
			if ((projection.x == 0) and (projection.y == 0)):
				projection = (circle.center - furtherPoint).unit;

			circleExtents = projection * circle.radius;

			circleCloser = circle.center - circleExtents;
			circleFurther = circle.center + circleExtents;

			overlap = getOverlapForPointsOnLine(projection, furtherPoint, closerPoint, circleCloser, circleFurther);

			if (overlap > 0):
				return True, projection, overlap;
		return False, Vector2(0, 0), 0;




def doProjection(displaySurface, c0, c1, lineDirection, b0p0, b0p1, b1p0, b1p1):
	linePerpendicular = lineDirection.perpendicular;
	lineCenter = screenCenter + linePerpendicular * lineDistanceFromScreenCenter;

	b0p0OnLine = projectPointOnLine(b0p0, lineCenter, lineDirection);
	b0p1OnLine = projectPointOnLine(b0p1, lineCenter, lineDirection);

	b1p0OnLine = projectPointOnLine(b1p0, lineCenter, lineDirection);
	b1p1OnLine = projectPointOnLine(b1p1, lineCenter, lineDirection);

	s = screenSize.magnitude;
	pygame.draw.line(displaySurface, innerLineColour, (lineCenter + lineDirection * s).intTuple, (lineCenter - lineDirection * s).intTuple, innerLineThickness);

	pygame.draw.line(displaySurface, c0, b0p0OnLine.intTuple, b0p1OnLine.intTuple, outerLineThickness);
	pygame.draw.line(displaySurface, c1, b1p0OnLine.intTuple, b1p1OnLine.intTuple, outerLineThickness);

	pygame.draw.line(displaySurface, innerLineColour, b0p0.intTuple, b0p0OnLine.intTuple, innerLineThickness);
	pygame.draw.line(displaySurface, innerLineColour, b0p1.intTuple, b0p1OnLine.intTuple, innerLineThickness);
	pygame.draw.line(displaySurface, innerLineColour, b1p0.intTuple, b1p0OnLine.intTuple, innerLineThickness);
	pygame.draw.line(displaySurface, innerLineColour, b1p1.intTuple, b1p1OnLine.intTuple, innerLineThickness);

	lineOverlap = getOverlapForPointsOnLine(lineDirection, b0p0, b0p1, b1p0, b1p1);

	b0p0AlongLine = (b0p0 - lineCenter).dot(lineDirection);
	b0p1AlongLine = (b0p1 - lineCenter).dot(lineDirection);
	b1p0AlongLine = (b1p0 - lineCenter).dot(lineDirection);
	b1p1AlongLine = (b1p1 - lineCenter).dot(lineDirection);

	b0Left = min(b0p0AlongLine, b0p1AlongLine);
	b1Left = min(b1p0AlongLine, b1p1AlongLine);

	drawPoint = max(b0Left, b1Left);
	overlapLineCenter = lineCenter + (lineDirection.perpendicular * 10);

	return (lineOverlap, drawPoint, overlapLineCenter, lineDirection)



def renderOverlapLine(displaySurface, shortest, lineOverlap, drawPoint, overlapLineCenter, lineDirection):
	colour = overlapLineCollisionColour if shortest else overlapLineColour;
	thickness = overlapLineCollisionThickness if shortest else overlapLineThickness;

	startOverlap = (overlapLineCenter + lineDirection * drawPoint);
	endOverlap = (overlapLineCenter + lineDirection * (drawPoint + lineOverlap));

	pygame.draw.line(displaySurface, colour, startOverlap.intTuple, endOverlap.intTuple, thickness);




def drawLinesToEdge(displaySurface, xEdge, yEdge, x, y):
	pygame.draw.line(displaySurface, innerLineColour, (x, y), (xEdge, y), innerLineThickness);
	pygame.draw.line(displaySurface, innerLineColour, (x, y), (x, yEdge), innerLineThickness);



def render(displaySurface, box, circle):
	displaySurface.fill(backgroundColour);

	closerPoint, furtherPoint = getCloserAndFurtherPoints(box, circle);

	if (closerPoint != None):
		# projection = (closerPoint - circle.center).unit;
		# circleExtents = projection * circle.radius;
		# overlapLine = doProjection(displaySurface, boxColour, circleColour, projection, furtherPoint, closerPoint, circle.center + circleExtents, circle.center - circleExtents);

		projection = (circle.center - closerPoint).unit;
		if ((projection.x == 0) and (projection.y == 0)):
			projection = (circle.center - furtherPoint).unit;

		circleExtents = projection * circle.radius;

		circleCloser = circle.center - circleExtents;
		circleFurther = circle.center + circleExtents;

		overlapLine = getOverlapForPointsOnLine(projection, furtherPoint, closerPoint, circleCloser, circleFurther);
		overlapLine = doProjection(displaySurface, boxColour, circleColour, projection, furtherPoint, closerPoint, circleCloser, circleFurther);


		collision = (overlapLine[0] > 0);
		shortest = collision;
		renderOverlapLine(displaySurface, shortest, *overlapLine);
		shortest = False;

		drawLinesToEdge(displaySurface, 0, 0, *box.topLeft.intTuple);
		drawLinesToEdge(displaySurface, screenWidth, 0, *box.topRight.intTuple);
		drawLinesToEdge(displaySurface, 0, screenHeight, *box.bottomLeft.intTuple);
		drawLinesToEdge(displaySurface, screenWidth, screenHeight, *box.bottomRight.intTuple);
	
	# Square
	drawAABB(displaySurface, box, boxColour, alpha, outlineThickness, centerThickness);

	# Circle
 	drawCircle(displaySurface, circle, circleColour, alpha, outlineThickness, centerThickness);



def renderMouse(displaySurface, holdingObject, holdingOffset):
	if (holdingObject != None):
		holderSize = 2;
		position = holdingObject.position + holdingOffset;

		pygame.draw.circle(displaySurface, (0, 0, 0), position.intTuple, holderSize);



def main():
	fpsClock = pygame.time.Clock();

	pygame.init();
	displaySurface = pygame.display.set_mode((screenWidth, screenHeight));
	pygame.display.set_caption(title);

	# blue = AABB(Vector2(60, 60), Vector2(140, 60));
	# red = AABB(Vector2(300, 180), Vector2(100, 180));
	size = screenWidth / 4;
	box = AABB(Vector2((screenWidth - size) / 2, (screenHeight - size) / 2), Vector2(size, size));

	radius = screenWidth / 10;
	circle = Circle(Vector2(screenWidth - radius * 3, radius * 2), radius);

	holdingObject = None;
	holdingOffset = Vector2(0, 0);
	lastMouseState = (False, False, False);

	gameRunning = True;
	while (gameRunning):
		for event in pygame.event.get():
			if (event.type == QUIT):
				gameRunning = False;

		lastMouseState, holdingObject, holdingOffset = updateMouse(lastMouseState, holdingObject, holdingOffset, box, circle, minBounds, maxBounds);
		

		renderMouse(displaySurface, holdingObject, holdingOffset);

		maxIter = 1;
		i = maxIter;
		while (i > 0):
			i -= 1;
			collision, normal, overlap = aabbCircleCollision(box, circle);
			if (collision):
				print("Collided! Normal: " + str(normal) + "; Smallest overlap: " + str(overlap));

				circle.position += normal * overlap;
			else:
				break;


		render(displaySurface, box, circle);

		pygame.display.update();
		fpsClock.tick(fps);

	pygame.quit();
	sys.exit();



if (__name__ == "__main__"):
	main();