package com.tealduck.game.weapon;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.Team;
import com.tealduck.game.component.BulletComponent;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.KnockbackComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.component.TeamComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.event.BulletCollision;
import com.tealduck.game.event.BulletRemove;
import com.tealduck.game.event.BulletWorldCollision;
import com.tealduck.game.event.EventName;
import com.tealduck.game.world.EntityConstants;
import com.tealduck.game.world.EntityLoader;


public class MachineGun extends Weapon {
	private Texture bulletTexture;


	public MachineGun(Texture bulletTexture) {
		this.bulletTexture = bulletTexture;
	}


	@Override
	public float getCooldownTime() {
		return EntityConstants.COOLDOWN_TIME;
	}


	@Override
	public float getReloadTime() {
		return EntityConstants.RELOAD_TIME;
	}


	@Override
	public int ammoRequiredToFire() {
		return 1;
	}


	@Override
	public int fire(EntityEngine entityEngine, int shooterId, Vector2 position, Vector2 direction, Team team) {
		direction.nor();

		Vector2 bulletSize = new Vector2(64, 64);
		position.add(bulletSize.cpy().scl(-0.5f));

		int bulletsFired = 0;

		while (bulletsFired < ammoRequiredToFire()) {
			createBullet(entityEngine, shooterId, position, direction, bulletSize, team);
			bulletsFired += 1;
		}

		return bulletsFired;
	}


	private void createBullet(EntityEngine entityEngine, int shooterId, Vector2 position, Vector2 direction,
			Vector2 bulletSize, Team team) {

		EntityManager entityManager = entityEngine.getEntityManager();
		int bulletId = entityManager.createEntity();

		entityManager.addComponent(bulletId,
				new PositionComponent(position.cpy(), direction.cpy(), bulletSize.cpy()));

		float bulletSpeed = EntityConstants.BULLET_SPEED;
		entityManager.addComponent(bulletId,
				new MovementComponent(direction.cpy().scl(bulletSpeed), bulletSpeed, 1));

		entityManager.addComponent(bulletId, new SpriteComponent(bulletTexture));

		int bulletRadius = EntityConstants.BULLET_RADIUS;
		EntityLoader.addEntityCircleCollisionComponent(entityManager, bulletId, position, bulletRadius);

		entityManager.addComponent(bulletId, new BulletComponent(shooterId));

		entityManager.addComponent(bulletId, new KnockbackComponent(EntityConstants.BULLET_KNOCKBACK_FORCE));

		entityManager.addComponent(bulletId, new DamageComponent(1));

		if (team != null) {
			entityManager.addComponent(bulletId, new TeamComponent(team));
		}

		EventManager eventManager = entityEngine.getEventManager();
		eventManager.addEvent(bulletId, EventName.COLLISION, BulletCollision.instance);

		eventManager.addEvent(bulletId, EventName.REMOVE, BulletRemove.instance);

		eventManager.addEvent(bulletId, EventName.WORLD_COLLISION, BulletWorldCollision.instance);
	}


	@Override
	public String toString() {
		return "MachineGun()";
	}


	@Override
	public int getClipSize() {
		return EntityConstants.MACHINE_GUN_CLIP_SIZE;
	}
}
