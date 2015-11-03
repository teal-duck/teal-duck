class EventManager(object):
	def __init__(self):
		"""
		events :: dict<EntityID, dict<String, (Int, Int, Data) -> None>>
		Each entity has a dictionary of events
		Events are named by a string
		The function takes 3 parameters:
			myId : EntityId,
			senderId : EntityId,
			otherData : SomeDataStructure

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



	def addEvent(self, entityId, eventName, eventFunction):
		"""(Int, String, (Int, Int, Data) -> None) -> None"""
		entityEvents = self.events.get(entityId, { });
		entityEvents[eventName] = eventFunction;


	
	def triggerEvent(self, senderId, receiverId, eventName, *args, **kwargs):
		"""(Int, Int, String, Data) -> None"""
		if (receiverId not in self.events):
			# The receiver can't receive any events
			return;

		receiverEvents = self.events[receiverId];
		if (eventName not in receiverEvents):
			# The receiver can't receive this event
			return;

		receiverEvents[eventName](receiverId, senderId, *args, **kwargs);



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


