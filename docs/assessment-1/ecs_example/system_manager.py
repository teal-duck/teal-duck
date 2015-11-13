class SystemManager(object):
	def __init__(self):
		# Todo: how to implement a priority queue
		self.systemQueue = None;


	
	def addSystem(self, system, priority):
		"""(System, Int) -> None
		Adds the system to the queue based on priority
		"""
		pass;



	def removeSystem(self, systemType):
		"""Class -> None
		Uses __class__ attribute of objects in systemQueue
		If system.__class__ = systemType then remove"""
		pass;



	def __iter__(self):
		"""Returns the systemQueue as an iterable
		E.g. map(lambda s: s.update(dt), systemManager)
		"""
		pass;



	def clear(self):
		"""None -> None"""
		pass;



"""
Also need a way to manage systems that only run every X frames or every Y seconds etc
"""
