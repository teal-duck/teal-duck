import pygame;
from pygame.locals import *;
import sys;

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



def main():
	width = 800;
	height = 600;
	title = "Swept AABB";

	pygame.init();
	displaySurface = pygame.display.set_mode((width, height));
	pygame.display.set_caption(title);

	gameRunning = True;
	while (gameRunning):
		for event in pygame.event.get():
			if (event.type == QUIT):
				gameRunning = False;
				
		pygame.display.update();

	pygame.quit();
	sys.exit();



if (__name__ == "__main__"):
	main();