import pygame;
from pygame.locals import *;
import sys;
import math;
from box import Box;
from collision import sweptAABB, getSweptBroadphaseBox, aabbCheck;

"""
 TODO: Collision
http://gamedev.stackexchange.com/questions/61620/reusable-top-down-collision-class
Move entity to location
Calculate the intersecting rectangle
Find the smaller dimension of the rectangle (width on x or height on y)
Push entity out of tile by that amount in that direction
Set velocity in that direction to 0

Swept AABB
http://www.gamedev.net/page/resources/_/technical/game-programming/swept-aabb-collision-detection-and-response-r3084
http://www.metanetsoftware.com/technique/tutorialA.html
http://www.metanetsoftware.com/technique/tutorialB.html
"""



def drawBox(surface, box, colour):
	pygame.draw.rect(surface, colour, ((box.x, box.y), (box.w, box.h)));



def getInput(player, moveSpeed):
	keys = pygame.key.get_pressed();
	dx = 0;
	dy = 0;
	if (keys[K_w]):
		dy -= 1;
	if (keys[K_s]):
		dy += 1;
	if (keys[K_a]):
		dx -= 1;
	if (keys[K_d]):
		dx += 1;

	dd = dx * dx + dy * dy;
	if (dd != 0):
		dd = math.sqrt(dd);
		dx /= dd;
		dy /= dd;

	dx *= moveSpeed;
	dy *= moveSpeed;

	player.vx = dx;
	player.vy = dy;



def doCollision(player, wall):
	"""
	Box broadphasebox = GetSweptBroadphaseBox(box);
	if (AABBCheck(broadphasebox, block) {
		float normalx, normaly;
		float collisiontime = SweptAABB(box, block, out normalx, out normaly);
		box.x += box.vx * collisiontime;
		box.y += box.vy * collisiontime;

		if (collisiontime < 1.0f) {
			// perform response here
		}
	}
	"""

	broadphaseBox = getSweptBroadphaseBox(player);
	if (aabbCheck(broadphaseBox, wall)):
		collisionInfo = sweptAABB(player, wall);
		collisionTime = collisionInfo[0];
		normalX = collisionInfo[1][0];
		normalY = collisionInfo[1][1];

		player.x += player.vx * collisionTime;
		player.y += player.vy * collisionTime;

		if (collisionTime < 1):
			remainingTime = 1 - collisionTime;

			dotProduct = (player.vx * normalY + player.vy * normalX) * remainingTime;
			player.vx = dotProduct * normalY;
			player.vy = dotProduct * normalX;
	else:
		player.x += player.vx;
		player.y += player.vy;


	



def render(surface, player, wall):
	surface.fill((0, 0, 0));

	drawBox(surface, player, (255, 0, 0));
	drawBox(surface, wall, (64, 64, 64));



def main():
	width = 800;
	height = 600;
	title = "Swept AABB";

	fps = 60;
	fpsClock = pygame.time.Clock();

	pygame.init();
	displaySurface = pygame.display.set_mode((width, height));
	pygame.display.set_caption(title);

	player = Box(10, 300, 50, 50, 0, 0);
	moveSpeed = 10;
	wall = Box(300, 200, 100, 200, 0, 0);

	gameRunning = True;
	while (gameRunning):
		for event in pygame.event.get():
			if (event.type == QUIT):
				gameRunning = False;

		getInput(player, moveSpeed);
		doCollision(player, wall);
		render(displaySurface, player, wall);

		pygame.display.update();
		fpsClock.tick(fps);

	pygame.quit();
	sys.exit();



if (__name__ == "__main__"):
	main();