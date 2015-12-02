"""
Based on "calculating the projection vector" demo on http://www.metanetsoftware.com/technique/tutorialA.html
http://www.showmycode.com/?752955611975f706bdfabb7153315711
"""

import pygame;
from pygame.locals import *;
import sys;

from aabb import AABB;
from vector2 import Vector2;



screenWidth = 600;
screenHeight = screenWidth;
screenSize = Vector2(screenWidth, screenHeight);
screenCenter = screenSize / 2;
title = "Calculating Projection Vector";

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
centerLinePosition = Vector2(screenWidth - outerLineThickness * 2, screenHeight - outerLineThickness * 2);

centerLineColour = (0, 80, 0);
centerLinePosition = Vector2(screenWidth - outerLineThickness * 4, screenHeight - outerLineThickness * 4);

redLineColour = (80, 0, 0);
redLinePosition = Vector2(screenWidth - outerLineThickness * 6, screenHeight - outerLineThickness * 6);

blueLineColour = (0, 0, 80);
blueLinePosition = Vector2(screenWidth - outerLineThickness * 8, screenHeight - outerLineThickness * 8);


lineDistanceFromScreenCenter = (screenWidth / 2) - 20;


redColour = (255, 0, 0);
blueColour = (0, 0, 255);

minBounds = Vector2(20, 20);
maxBounds = screenSize - minBounds; # Vector2(outerLineThickness * 9, outerLineThickness * 9);
# minX = 50;
# minY = minX;
# maxX = screenWidth - minX;
# maxY = screenHeight - minY;



def drawAABB(displaySurface, aabb, colour, alpha, outlineThickness, centerThickness):
	alphaSurface = pygame.Surface(aabb.size.tuple)
	alphaSurface.set_alpha(alpha)
	alphaSurface.fill(colour);

	displaySurface.blit(alphaSurface, aabb.position.tuple); 

	pygame.draw.rect(displaySurface, colour, aabb.tuple, outlineThickness);
	pygame.draw.circle(displaySurface, colour, aabb.center.tuple, centerThickness);



def updateMouse(lastMouseState, holdingRect, holdingOffset, blue, red, minBounds, maxBounds):
	mouseState = pygame.mouse.get_pressed();
	if (mouseState[0]):
		mousePosition = Vector2(*pygame.mouse.get_pos());

		if (holdingRect == None):
			if (lastMouseState[0] == False):
				if (red.intersectsPoint(mousePosition)):
					holdingRect = red;

				elif (blue.intersectsPoint(mousePosition)):
					holdingRect = blue;

				if (holdingRect != None):
					holdingOffset = mousePosition - holdingRect.position;
		else:
			newPosition = mousePosition - holdingOffset;
			width = holdingRect.width;
			height = holdingRect.height;

			if (newPosition.x < minBounds.x):
				newPosition.x = minBounds.x;
			if (newPosition.x + width > maxBounds.x):
				newPosition.x = maxBounds.x - width;
			
			if (newPosition.y < minBounds.y):
				newPosition.y = minBounds.y;
			if (newPosition.y + height > maxBounds.y):
				newPosition.y = maxBounds.y - height;

			holdingRect.position = newPosition;
	else:
		holdingRect = None;
		holdingOffset = Vector2();

	return (mouseState, holdingRect, holdingOffset);
	

def signum(x):
	return 1 if x > 0 else -1 if x < 0 else 0;



def projectPointOnLine(point, linePoint, lineDirection):
	return linePoint + (point - linePoint).dot(lineDirection) / lineDirection.dot(lineDirection) * lineDirection;



def doProjection(displaySurface, b1, b2, lineDirection):
	b1Center = b1.center;
	b2Center = b2.center;

	centerDiff = b2Center - b1Center;

	# projected = centerDiff.dot(lineDirection);

	linePerpendicular = lineDirection.perpendicular;
	lineCenter = screenCenter + linePerpendicular * lineDistanceFromScreenCenter;

	b1CenterOnLine = projectPointOnLine(b1Center, lineCenter, lineDirection);
	b2CenterOnLine = projectPointOnLine(b2Center, lineCenter, lineDirection);

	pygame.draw.line(displaySurface, centerLineColour, b1CenterOnLine.tuple, b2CenterOnLine.tuple, outerLineThickness);

	pygame.draw.line(displaySurface, innerLineColour, b1Center.tuple, b1CenterOnLine.tuple);
	pygame.draw.line(displaySurface, innerLineColour, b2Center.tuple, b2CenterOnLine.tuple);






def render(displaySurface, blue, red):
	displaySurface.fill(backgroundColour);

	upVector = Vector2(0, -1);
	rightVector = Vector2(1, 0);

	projectionVectors = [upVector, rightVector, Vector2(1, -1).unit];

	for vector in projectionVectors:
		doProjection(displaySurface, red, blue, vector);


	"""
	centerDiffX = centerDiff.dot(rightVector);
	centerDiffY = centerDiff.dot(upVector);

	blueHalfWidthX = signum(centerDiffX) * blue.width / 2;
	blueHalfWidthY = signum(centerDiffY) * blue.height / 2;
	blueCorner = blueCenter + Vector2(blueHalfWidthX, blueHalfWidthY);

	redHalfWidthX = -1 * signum(centerDiffX) * red.width / 2;
	redHalfWidthY = -1 * signum(centerDiffY) * red.height / 2;
	redCorner = redCenter + Vector2(redHalfWidthX, redHalfWidthY);

	# Green horizontal
	pygame.draw.rect(displaySurface, centerLineColour, ((blueCenter.x, centerLinePosition.y), (centerDiffX, outerLineThickness)));
	# Green vertical
	pygame.draw.rect(displaySurface, centerLineColour, ((centerLinePosition.x, blueCenter.y), (outerLineThickness, centerDiffY)));
	# Blue center to green horizontal
	pygame.draw.line(displaySurface, innerLineColour, blueCenter.tuple, (blueCenter.x, centerLinePosition.y), innerLineThickness);
	# Blue center to green vertical
	pygame.draw.line(displaySurface, innerLineColour, blueCenter.tuple, (centerLinePosition.x, blueCenter.y), innerLineThickness);
	# Red center to green horizontal
	pygame.draw.line(displaySurface, innerLineColour, redCenter.tuple, (redCenter.x, centerLinePosition.y), innerLineThickness);
	# Red center to green vertical
	pygame.draw.line(displaySurface, innerLineColour, redCenter.tuple, (centerLinePosition.x, redCenter.y), innerLineThickness);

	# Blue center to red center
	pygame.draw.line(displaySurface, innerLineColour, blueCenter.tuple, redCenter.tuple, innerLineThickness);

	# Red horizontal
	pygame.draw.rect(displaySurface, redLineColour, ((redCenter.x, redLinePosition.y), (redHalfWidthX, outerLineThickness)));
	# Red vertical
	pygame.draw.rect(displaySurface, redLineColour, ((redLinePosition.x, redCenter.y), (outerLineThickness, redHalfWidthY)));
	# Red corner to red horizontal
	pygame.draw.line(displaySurface, innerLineColour, redCorner.tuple, (redCorner.x, redLinePosition.y), innerLineThickness);
	# Red corner to red vertical
	pygame.draw.line(displaySurface, innerLineColour, redCorner.tuple, (redLinePosition.x, redCorner.y), innerLineThickness);

	# Blue horizontal
	pygame.draw.rect(displaySurface, blueLineColour, ((blueCenter.x, blueLinePosition.y), (blueHalfWidthX, outerLineThickness)));
	# Blue vertical
	pygame.draw.rect(displaySurface, blueLineColour, ((blueLinePosition.x, blueCenter.y), (outerLineThickness, blueHalfWidthY)));
	# Blue corner to blue horizontal
	pygame.draw.line(displaySurface, innerLineColour, blueCorner.tuple, (blueCorner.x, blueLinePosition.y), innerLineThickness);
	# Blue corner to blue vertical
	pygame.draw.line(displaySurface, innerLineColour, blueCorner.tuple, (blueLinePosition.x, blueCorner.y), innerLineThickness);
"""

	# Blue rect
	drawAABB(displaySurface, blue, blueColour, alpha, outlineThickness, centerThickness);

	# Red rect
	drawAABB(displaySurface, red, redColour, alpha, outlineThickness, centerThickness);



def renderMouse(displaySurface, holdingRect, holdingOffset):
	if (holdingRect != None):
		holderSize = 2;
		position = holdingRect.position + holdingOffset;

		pygame.draw.circle(displaySurface, (0, 0, 0), position.tuple, holderSize);



def main():
	fpsClock = pygame.time.Clock();

	pygame.init();
	displaySurface = pygame.display.set_mode((screenWidth, screenHeight));
	pygame.display.set_caption(title);

	blue = AABB(Vector2(60, 60), Vector2(140, 60));
	red = AABB(Vector2(300, 180), Vector2(100, 180));

	holdingRect = None;
	holdingOffset = Vector2();
	lastMouseState = (False, False, False);

	gameRunning = True;
	while (gameRunning):
		for event in pygame.event.get():
			if (event.type == QUIT):
				gameRunning = False;

		lastMouseState, holdingRect, holdingOffset = updateMouse(lastMouseState, holdingRect, holdingOffset, blue, red, minBounds, maxBounds);
		
		render(displaySurface, blue, red);

		renderMouse(displaySurface, holdingRect, holdingOffset);

		pygame.display.update();
		fpsClock.tick(fps);

	pygame.quit();
	sys.exit();



if (__name__ == "__main__"):
	main();