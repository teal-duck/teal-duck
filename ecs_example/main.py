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



def setupPlayer(entityManager, screenSize):
	player = entityManager.createEntity();
	position = PositionComponent(Vector2(20, 20));
	velocity = VelocityComponent(Vector2(0, 0));
	aabb = AABBComponent(Vector2(30, 30));
	display = DisplayComponent((255, 0, 0), "Rect", aabb.size);
	controller = PlayerControlledComponent();
	health = HealthComponent(100);

	entityManager.addComponent(player, position);
	entityManager.addComponent(player, velocity);
	entityManager.addComponent(player, aabb);
	entityManager.addComponent(player, display);
	entityManager.addComponent(player, controller);
	entityManager.addComponent(player, health);

	return player;



def setupStaticEnemy(entityManager, screenSize):
	radius = random.randint(10, 30);
	padding = 20;
	x = random.randint(padding, screenSize[0] - (2 * radius) - padding);
	y = random.randint(padding, screenSize[1] - (2 * radius) - padding);

	enemy = entityManager.createEntity();
	position = PositionComponent(Vector2(x, y));
	circle = CircleCollisionComponent(radius);

	display = DisplayComponent((0, 255, 0), "Circle", circle.radius);

	entityManager.addComponent(enemy, position);
	entityManager.addComponent(enemy, circle);
	entityManager.addComponent(enemy, display);

	return enemy;



def setupDynamicEnemy(entityManager, screenSize):
	radius = 20;
	padding = 20;
	x = screenSize[0] - (2 * radius) - padding;
	y = screenSize[1] - (2 * radius) - padding;

	enemy = entityManager.createEntity();
	position = PositionComponent(Vector2(x, y));
	velocity = VelocityComponent(Vector2(0, 0));

	circle = CircleCollisionComponent(radius);
	display = DisplayComponent((0, 0, 255), "Circle", circle.radius);
	controller = AIControlledComponent();

	entityManager.addComponent(enemy, position);
	entityManager.addComponent(enemy, velocity);
	entityManager.addComponent(enemy, circle);
	entityManager.addComponent(enemy, display);
	entityManager.addComponent(enemy, controller);

	return enemy;



def run(displaySurface, entityManager, screenSize, fps):
	gameRunning = True;
	fpsClock = pygame.time.Clock();

	playerEntity = setupPlayer(entityManager, screenSize);

	for _ in range(10):
		enemy = setupStaticEnemy(entityManager, screenSize);

	setupDynamicEnemy(entityManager, screenSize);

	logicSystem = LogicSystem(entityManager);
	movementSystem = MovementSystem(entityManager);
	renderSystem = RenderSystem(entityManager, displaySurface);

	systems = [logicSystem, movementSystem, renderSystem];

	print(entityManager);

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
	random.seed(6);
	width = config.WIDTH;
	height = config.HEIGHT;
	screenSize = (width, height)

	displaySurface = setupWindow(width, height, config.TITLE, config.RESIZABLE);
	entityManager = EntityManager();
	run(displaySurface, entityManager, screenSize, config.FPS);
	shutdown();



if (__name__ == "__main__"):
	main();