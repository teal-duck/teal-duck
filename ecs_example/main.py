import pygame;
from pygame.locals import *;

import sys;
import random;

import config;
from vector2 import Vector2;

from ecs import EntityManager;
from components import *;
from systems import * ;



def setupWindow(width, height, title, resizable):
	pygame.init();
	displaySurface = pygame.display.set_mode((width, height), pygame.RESIZABLE if resizable else 0);
	pygame.display.set_caption(title);

	return displaySurface;



def processEvents():
	quitEvent = False;

	for event in pygame.event.get():
		if (event.type == QUIT):
			quitEvent = True;

	return (not quitEvent);



def setupPlayer(entityManager):
	player = entityManager.createEntity();
	position = PositionComponent(Vector2(0, 0));
	velocity = VelocityComponent(Vector2(0, 0));
	size = SizeComponent(Vector2(30, 30));
	display = DisplayComponent((255, 0, 0), "Rect");
	controller = PlayerControlledComponent();

	entityManager.addComponent(player, position);
	entityManager.addComponent(player, velocity);
	entityManager.addComponent(player, size);
	entityManager.addComponent(player, display);
	entityManager.addComponent(player, controller);

	return player;



def setupStaticEnemy(entityManager):
	enemy = entityManager.createEntity();
	position = PositionComponent(Vector2(random.randint(50, 350), random.randint(50, 250)));
	size = SizeComponent(Vector2(20, 20));
	display = DisplayComponent((0, 255, 0), "Circle");

	entityManager.addComponent(enemy, position);
	entityManager.addComponent(enemy, size);
	entityManager.addComponent(enemy, display);

	return enemy;



def run(displaySurface, entityManager, fps):
	gameRunning = True;
	fpsClock = pygame.time.Clock();

	playerEntity = setupPlayer(entityManager);

	for _ in range(5):
		enemy = setupStaticEnemy(entityManager);

	logicSystem = LogicSystem(entityManager);
	movementSystem = MovementSystem(entityManager);
	renderSystem = RenderSystem(entityManager, displaySurface);

	systems = [logicSystem, movementSystem, renderSystem];

	while (gameRunning):
		gameRunning = processEvents();
		deltaTime = fpsClock.get_time() / 1000.0;

		for system in systems:
			system.update(deltaTime);

		pygame.display.update();
		fpsClock.tick(fps);



def shutdown():
	pygame.quit();
	sys.exit();



def main():
	displaySurface = setupWindow(config.WIDTH, config.HEIGHT, config.TITLE, config.RESIZABLE);
	entityManager = EntityManager();
	run(displaySurface, entityManager, config.FPS);
	shutdown();



if (__name__ == "__main__"):
	main();