package com.tealduck.game.pickup;


import com.tealduck.game.AssetLocations;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.engine.EntityManager;


/**
 *
 */
public class HealthPickup extends Pickup {
	public int health;


	/**
	 * @param health
	 */
	public HealthPickup(int health) {
		this.health = health;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tealduck.game.pickup.Pickup#applyToEntity(com.tealduck.game.engine.EntityManager, int)
	 *
	 * Only applyable if the entity has a health component and is not already at full health.
	 */
	@Override
	public boolean applyToEntity(EntityManager entityManager, int entity) {
		if (entityManager.entityHasComponent(entity, HealthComponent.class)) {
			HealthComponent healthComponent = entityManager.getComponent(entity, HealthComponent.class);
			if (healthComponent.health != healthComponent.maxHealth) {
				healthComponent.health += health;
				if (healthComponent.health > healthComponent.maxHealth) {
					healthComponent.health = healthComponent.maxHealth;
				}
				return true;
			}
		}
		return false;
	}


	@Override
	public String getTextureName() {
		return AssetLocations.HEALTH_PICKUP;
	}


	@Override
	public String toString() {
		return "HealthPickup(" + health + ")";
	}
}
