# https://github.com/adamgit/Entity-System-RDBMS-Inspired-Java/blob/master/EntitySystemJava/src/com/wikidot/entitysystems/rdbmswithcodeinsystems/EntityManager.java

import abc;



class Component(object):
	pass;



class System(object):
	__metaclass__ = abc.ABCMeta;



	def __init__(self, entityManager):
		self.entityManager = entityManager;



	def entityHasComponent(self, entity, componentType):
		return self.entityManager.entityHasComponent(entity, componentType);



	def getAllEntitiesPossessingComponent(self, componentType):
		return self.entityManager.getAllEntitiesPossessingComponent(componentType);



	def getAllEntitiesPossessingComponents(self, *componentTypes):
		return self.entityManager.getAllEntitiesPossessingComponents(*componentTypes);



	def getComponent(self, entity, component):
		return self.entityManager.getComponent(entity, component);



	@abc.abstractmethod
	def update(deltaTime):
		pass;



class EntityManager(object):
	def __init__(self):
		self.lowestUnassignedEntityId = 1;
		# Set of integers represent entities
		self.entities = set();
		# components is dictionary with keys being type(some component), values being another dictionary
		# sub-dictionary has key entityId and value is a specific instance of a component
    # HashMap<Class<?>, HashMap<Integer, ? extends Component>>
		self.componentStore = dict();


    

	def getComponent(self, entity, componentType):
		"""(Int, Class) -> Component"""
		if (componentType in self.componentStore):
			store = self.componentStore[componentType];
			component = store[entity];
			return component;
		else:
			return None;



	def getAllComponentsOfType(self, componentType):
		"""Class -> [Component]"""
		if (componentType in self.componentStore):
			return self.componentStore[componentType].values();
		else:
			return None;



	def getAllEntitiesPossessingComponent(self, componentType):
		"""Class -> set(Int)"""
		return set(self.componentStore.get(componentType, { }).keys());



	def getAllEntitiesPossessingComponents(self, *componentTypes):
		"""[Class] -> set(Int)
		Can take a single class"""
		return reduce(lambda a, b: a.intersection(b), \
			map(self.getAllEntitiesPossessingComponent, componentTypes));



	def entityHasComponent(self, entity, componentType):
		"""(Int, Class) -> Bool"""
		return entity in self.componentStore.get(componentType, {});



	def addComponent(self, entity, component):
		"""(Int, Component) -> Void"""
		store = self.componentStore.setdefault(component.__class__, dict());
		store[entity] = component;



	def removeComponent(self, entity, componentType):
		"""(Int, Class) -> Void"""
		store = self.componentStore.get(componentType, None);
		if (store):
			if (entity in store):
				del store[entity];



	def createEntity(self):
		"""Void -> Int"""
		newId = self.lowestUnassignedEntityId;
		self.lowestUnassignedEntityId += 1;
		self.entities.add(newId);
		return newId;



	def killEntity(self, entity):
		"""Int -> Void"""
		if (entity in self.entities):
			self.entities.remove(entity);

			for store in self.componentStore.values():
				if (entity in store):
					del store[entity];



	def __str__(self):
		return "EntityManager(" \
			+ "lowestUnassignedEntityId=" \
			+ str(self.lowestUnassignedEntityId) \
			+ ", entities=" \
			+ str(self.entities) \
			+ ", componentStore=" \
			+ str(self.componentStore) \
			+ ")";



from components import *;
def main():

	print("Instantiating EntityManager");
	entityManager = EntityManager();
	print(entityManager);

	print("");

	print("Creating ent1")
	ent1 = entityManager.createEntity();
	print(ent1);
	print(entityManager);

	print("Creating ent1Pos");
	ent1Pos = PositionComponent(5);
	print(ent1Pos);

	entityManager.addComponent(ent1, ent1Pos);
	print(entityManager);

	print("");

	print("Creating ent2");
	ent2 = entityManager.createEntity();
	print(ent2);
	print(entityManager);

	print("Creating ent2Pos");
	ent2Pos = PositionComponent(10);
	print(ent2Pos);

	entityManager.addComponent(ent2, ent2Pos);
	print(entityManager);

	print("");

	print("Getting components: Position");
	print(entityManager.getComponent(ent1, PositionComponent));
	print(entityManager.getComponent(ent2, PositionComponent));

	print("");
	print("Getting components: Velocity");
	print(entityManager.getComponent(ent1, VelocityComponent));
	print("");

	print("All entities possessing position + velocity");
	print(entityManager.getAllEntitiesPossessingComponent(PositionComponent));
	print(entityManager.getAllEntitiesPossessingComponent(VelocityComponent));

	print("");

	print("Creating ent1Vel");
	ent1Vel = VelocityComponent(20);
	entityManager.addComponent(ent1, ent1Vel);

	print(entityManager);
	print(entityManager.getComponent(ent1, VelocityComponent));

	print("");
	print("Get all components of type: Position");
	print(entityManager.getAllComponentsOfType(PositionComponent));
	print("");
	print("Get all components of type: Velocity");
	print(entityManager.getAllComponentsOfType(VelocityComponent));

	print("");
	print("Get all entities with Position and Velocity");
	print(entityManager.getAllEntitiesPossessingComponents(PositionComponent, VelocityComponent));

	print("");
	print("Has component: Position");
	print(entityManager.entityHasComponent(ent1, PositionComponent));
	print(entityManager.entityHasComponent(ent2, PositionComponent));
	print("Has component: Velocity");
	print(entityManager.entityHasComponent(ent1, VelocityComponent));
	print(entityManager.entityHasComponent(ent2, VelocityComponent));

	print("");
	print("Killing ent1");
	print("Before");
	print(entityManager);
	print("After");
	entityManager.killEntity(ent1);
	print(entityManager);



if (__name__ == "__main__"):
	main();
