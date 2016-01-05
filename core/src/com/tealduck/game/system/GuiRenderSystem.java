package com.tealduck.game.system;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.Tag;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.world.EntityConstants;


public class GuiRenderSystem extends GameSystem {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;

	private GlyphLayout healthText;
	private GlyphLayout ammoText;
	private float healthTextX = 10;
	private float healthTextYOffset = 10;
	private int healthSize = 20;
	private float healthOffset = 8;

	private Texture healthTexture;
	private Texture healthBackgroundTexture;

	private Texture ammoBackgroundTexture;


	public GuiRenderSystem(EntityEngine entityEngine, SpriteBatch batch, OrthographicCamera camera,
			BitmapFont font) {
		super(entityEngine);
		this.batch = batch;
		this.camera = camera;
		this.font = font;

		healthText = new GlyphLayout(font, "Health:");
		ammoText = new GlyphLayout(font, "Ammo:  ");

		Color backgroundColour = Color.BLUE;

		Pixmap healthPixmap = new Pixmap(healthSize, healthSize, Format.RGBA8888);
		healthPixmap.setColor(Color.RED);
		healthPixmap.fill();
		healthTexture = new Texture(healthPixmap);

		// TODO: Replace healthBackground with 9-patch
		int maxHealth = EntityConstants.PLAYER_MAX_HEALTH;
		float healthBackgroundPixmapWidth = (healthTextX + healthText.width
				+ (maxHealth * (healthSize + healthOffset)) + healthOffset);
		float healthBackgroundPixmapHeight = (((healthTextYOffset + healthSize) - 1) + healthOffset);
		Pixmap healthBackgroundPixmap = new Pixmap((int) healthBackgroundPixmapWidth,
				(int) healthBackgroundPixmapHeight, Format.RGBA8888);
		healthBackgroundPixmap.setColor(backgroundColour);
		// healthBackgroundPixmap.fill();
		int healthBackgroundRadius = (int) healthOffset * 2;
		healthBackgroundPixmap.fillRectangle(0, 0, healthBackgroundPixmap.getWidth(),
				healthBackgroundPixmap.getHeight() - healthBackgroundRadius);
		healthBackgroundPixmap.fillRectangle(0, 0, healthBackgroundPixmap.getWidth() - healthBackgroundRadius,
				healthBackgroundPixmap.getHeight());
		healthBackgroundPixmap.fillCircle(healthBackgroundPixmap.getWidth() - healthBackgroundRadius,
				healthBackgroundPixmap.getHeight() - healthBackgroundRadius, healthBackgroundRadius);

		healthBackgroundTexture = new Texture(healthBackgroundPixmap);

		float ammoBackgroundPixmapWidth = healthBackgroundPixmapWidth;
		float ammoBackgroundPixmapHeight = healthBackgroundPixmapHeight;
		Pixmap ammoBackgroundPixmap = new Pixmap((int) ammoBackgroundPixmapWidth,
				(int) ammoBackgroundPixmapHeight, Format.RGBA8888);
		ammoBackgroundPixmap.setColor(backgroundColour);
		// healthBackgroundPixmap.fill();
		int ammoBackgroundRadius = healthBackgroundRadius;
		ammoBackgroundPixmap.fillRectangle(0, 0, ammoBackgroundPixmap.getWidth(),
				ammoBackgroundPixmap.getHeight() - ammoBackgroundRadius);
		ammoBackgroundPixmap.fillRectangle(ammoBackgroundRadius, 0,
				ammoBackgroundPixmap.getWidth() - ammoBackgroundRadius,
				ammoBackgroundPixmap.getHeight());
		ammoBackgroundPixmap.fillCircle(ammoBackgroundRadius,
				ammoBackgroundPixmap.getHeight() - ammoBackgroundRadius, ammoBackgroundRadius);

		ammoBackgroundTexture = new Texture(ammoBackgroundPixmap);
	}


	@Override
	public void update(float deltaTime) {
		EntityManager entityManager = getEntityManager();
		EntityTagManager entityTagManager = getEntityTagManager();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.enableBlending();

		try {
			int playerId = entityTagManager.getEntity(Tag.PLAYER);

			HealthComponent healthComponent = entityManager.getComponent(playerId, HealthComponent.class);
			renderHealthBar(healthComponent.health);

			WeaponComponent weaponComponent = entityManager.getComponent(playerId, WeaponComponent.class);
			renderWeaponAmmo(weaponComponent);
		} catch (NullPointerException e) {
		} catch (IllegalArgumentException e) {
		}

		batch.end();
	}


	private void renderWeaponAmmo(WeaponComponent weaponComponent) {
		float ammoX = camera.viewportWidth - ammoBackgroundTexture.getWidth();
		float ammoTextY = camera.viewportHeight - healthTextYOffset;

		batch.draw(ammoBackgroundTexture, ammoX, camera.viewportHeight - ammoBackgroundTexture.getHeight());

		float ammoTextX = ammoX + healthTextX;
		font.draw(batch, ammoText, ammoTextX, ammoTextY);

		ammoTextX += ammoText.width;

		int ammoInClip = weaponComponent.ammoInClip;
		int clipSize = weaponComponent.getClipSize();
		int extraAmmo = weaponComponent.extraAmmo;
		boolean isReloading = weaponComponent.isReloading();
		String currentAmmoText = ammoInClip + "/" + clipSize + " (" + extraAmmo + ")";
		if (isReloading) {
			currentAmmoText += " R";
		}
		font.draw(batch, currentAmmoText, ammoTextX, ammoTextY);
	}


	public void renderHealthBar(int health) {
		if (health < 0) {
			health = 0;
		}

		batch.draw(healthBackgroundTexture, 0, camera.viewportHeight - healthBackgroundTexture.getHeight());

		float healthX = healthTextX;
		float healthY = camera.viewportHeight - healthTextYOffset;

		font.draw(batch, healthText, healthTextX, healthY);

		healthX += healthText.width + healthOffset;
		healthY -= healthSize - 1;

		for (int i = 0; i < health; i += 1) {
			batch.draw(healthTexture, healthX, healthY);
			healthX += healthSize + healthOffset;
		}
	}

}
