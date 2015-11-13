class EventManager(object):
	def __init__(self, entityManager):
		"""
		events :: dict<EntityID, dict<String, (EntityManager, Int, Int) -> None>>
		Each entity has a dictionary of events
		Events are named by a string
		The function takes 3 parameters:
			entityManager: EntityManager
			myId : EntityId,
			senderId : EntityId,

		E.g:
		events = {
			[playerId]: {
				"collision": doPlayerCollision,
				"takeDamage": takeDamage,
			},
			[goose1]: {
			}
		}
		"""
		self.events = { };
		self.entityManager = entityManager;



	def addEvent(self, entityId, eventName, eventFunction):
		"""(Int, String, (EntityManager, Int, Int) -> None) -> None"""
		entityEvents = self.events.get(entityId, { });
		entityEvents[eventName] = eventFunction;


	
	def triggerEvent(self, senderId, receiverId, eventName):
		"""(Int, Int, String) -> None"""
		if (receiverId not in self.events):
			# The receiver can't receive any events
			return;

		receiverEvents = self.events[receiverId];
		if (eventName not in receiverEvents):
			# The receiver can't receive this event
			return;

		receiverEvents[eventName](entityManager, receiverId, senderId);



	def removeEvent(self, entityId, eventName):
		"""(Int, String) -> None"""
		if (entityId in self.events and eventName in self.events[entityId]):
			del self.events[entityId][eventName];



	def removeEntity(self, entityId):
		"""Int -> None"""
		if (entityId in self.events):
			del self.events[entityId];



	def clear(self):
		"""None -> None"""
		# I think you need this line
		del self.events;		
		self.events = { };


