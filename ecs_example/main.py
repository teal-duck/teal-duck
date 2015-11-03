import pygame;
from pygame.locals import *;

import sys;
import random;

import config;
from vector2 import Vector2;

from ecs import EntityManager;
from components import *;
from systems import * ;
from collision import *;



"""
Todo:
	- Position could instead be Transform and store a rotation value too if the game requires rotation
	- Velocity, Acceleration, Force and Mass can all be put into 1 Movement component
	- Rethink controllers and the logic system
	- Change list of systems in the run function to be a system manager with a priority queue so that systems can be added/removed at runtime
	- Restructure code to have less constant calls to EntityManager.hasComponent() and EntityManager.getComponent()
		- Possibly make an entity a class that stores a list of its own components?
		- Write EntityManager.getAllComponentsForEntity that returns a dict with keys being classes and values being the components
		- Perhaps the getAllEntitiesPossessingComponents function could return a set of tuples where first element is entity id, second is dict of components as above
			E.g.:
				entities = entityManager.getAllEntitiesPossessingComponents(TransformComponent, MovementComponent)
				for (entityId, components) in entities:
					transformComponent = components[TransformComponent]
					movementComponent = components[MovementComponent]
	- How to handle collision
		- As part of MovementSystem?
			- Would then make that system have multiple responsibilities
		- Intermediate step between logic system and movement system?
			- Possibly require logic system to create a new component PotentialMoveComponent
			- Collision reacts on this, if collides then change the component to not move there
			- Movement looks at all entities with PotentialMoveComponent
				- Updates position
				- Removes PotentialMoveComponent or recycles for next frame
		- What about when 2 entities move and are now colliding?
		- When to process the result of a collision such as player losing health?
	- Look into Observable pattern/event systems
		- Perhaps could solve problem of collision?
		- How to structure the code?
			- Is there 1 observer that handles receiving an event and sending it to an object?
			- Do systems send a message to this 1 observer saying an entity needs to process and event?
			- When is this event then processed?
	- Possibly create systems that only run every X frames/seconds
		- Could be part of an event system where the event is X frames/seconds have elapsed?
	- Entity tagging system?
		- See artemis framework
		- Allows code to just reference "player" despite there not being e.g. Player player = new Player()
		- Possibly just assigns some string to an entity ID
		- Implement as a hash table
	- Base assemblages
		- Single function like the setupPlayer() function below
		- Takes the entity manager and other parameters
		- Creates an entity
		- Attaches its components
		- Returns the generated ID
	- How do GUI, world, quests etc fit into this?
		- GUI
			- For GUI effects like health bars, particles, things part of the game world etc would be entities and components
				- See health bar rendering in RenderSystem
				- Particle effects work well in component systems where they can be parallelised
			- Other GUI elements like HUD
				- After all systems (especially RenderSystem for draw order) have processed
				- Use the tagging system to reference player entity (e.g to retrieve its HealthComponent)

		- World
			-
		- Quests
			-
"""



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
	startingPosition = Vector2(20, 20);
	size = Vector2(30, 30);

	player = entityManager.createEntity();
	position = PositionComponent(startingPosition);
	velocity = VelocityComponent();
	force = ForceComponent();
	acceleration = AccelerationComponent();
	mass = MassComponent(100);

	shape = AABBShape(size);
	collision = CollisionComponent(shape);

	display = DisplayComponent((255, 0, 0), "Rect", size);
	controller = PlayerControlledComponent();
	health = HealthComponent(100);

	entityManager.addComponent(player, position);
	entityManager.addComponent(player, velocity);
	entityManager.addComponent(player, force);
	entityManager.addComponent(player, acceleration);
	entityManager.addComponent(player, mass);
	entityManager.addComponent(player, collision);
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

	shape = CircleShape(radius);
	collision = CollisionComponent(shape);

	display = DisplayComponent((0, 255, 0), "Circle", radius);

	entityManager.addComponent(enemy, position);
	entityManager.addComponent(enemy, collision);
	entityManager.addComponent(enemy, display);

	return enemy;



def setupDynamicEnemy(entityManager, screenSize):
	radius = 20;
	padding = 20;
	x = screenSize[0] - (2 * radius) - padding;
	y = screenSize[1] - (2 * radius) - padding;

	enemy = entityManager.createEntity();
	position = PositionComponent(Vector2(x, y));
	velocity = VelocityComponent();
	force = ForceComponent();
	acceleration = AccelerationComponent();
	mass = MassComponent();

	shape = CircleShape(radius);
	collision = CollisionComponent(shape);

	display = DisplayComponent((0, 0, 255), "Circle", radius);
	controller = AIControlledComponent();

	entityManager.addComponent(enemy, position);
	entityManager.addComponent(enemy, velocity);
	entityManager.addComponent(enemy, force);
	entityManager.addComponent(enemy, acceleration);
	entityManager.addComponent(enemy, mass);
	entityManager.addComponent(enemy, collision);
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
	collisionSystem = CollisionSystem(entityManager);
	forcesSystem = ForcesSystem(entityManager);
	movementSystem = MovementSystem(entityManager);
	renderSystem = RenderSystem(entityManager, displaySurface);

	systems = [logicSystem, forcesSystem, collisionSystem, movementSystem, renderSystem];

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
