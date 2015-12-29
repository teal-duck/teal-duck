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
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.GameSystem;


public class GuiRenderSystem extends GameSystem {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;

	private GlyphLayout healthText;
	private float healthTextX = 10;
	private float healthTextYOffset = 10;
	private int healthSize = 20;
	private float healthOffset = 8;

	private Texture healthTexture;
	private Texture healthBackgroundTexture;


	public GuiRenderSystem(EntityEngine entityEngine, SpriteBatch batch, OrthographicCamera camera,
			BitmapFont font) {
		super(entityEngine);
		this.batch = batch;
		this.camera = camera;
		this.font = font;

		healthText = new GlyphLayout(font, "Health:");

		Pixmap healthPixmap = new Pixmap(healthSize, healthSize, Format.RGBA8888);
		healthPixmap.setColor(Color.RED);
		healthPixmap.fill();
		healthTexture = new Texture(healthPixmap);

		// TODO: Replace healthBackground with 9-patch
		int maxHealth = 4;
		Pixmap healthBackgroundPixmap = new Pixmap(
				(int) (healthTextX + healthText.width + (maxHealth * (healthSize + healthOffset))
						+ healthOffset),
				(int) (((healthTextYOffset + healthSize) - 1) + healthOffset), Format.RGBA8888);
		healthBackgroundPixmap.setColor(Color.BLUE);
		// healthBackgroundPixmap.fill();
		int radius = (int) healthOffset * 2;
		healthBackgroundPixmap.fillRectangle(0, 0, healthBackgroundPixmap.getWidth(),
				healthBackgroundPixmap.getHeight() - radius);
		healthBackgroundPixmap.fillRectangle(0, 0, healthBackgroundPixmap.getWidth() - radius,
				healthBackgroundPixmap.getHeight());
		healthBackgroundPixmap.fillCircle(healthBackgroundPixmap.getWidth() - radius,
				healthBackgroundPixmap.getHeight() - radius, radius);

		healthBackgroundTexture = new Texture(healthBackgroundPixmap);
	}


	float time = 0;
	float dieTime = 3.5f;


	@Override
	public void update(float deltaTime) {
		time += deltaTime;

		EntityManager entityManager = getEntityManager();
		EntityTagManager entityTagManager = getEntityTagManager();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.enableBlending();

		try {
			int playerId = entityTagManager.getEntity(Tag.PLAYER);
			HealthComponent healthComponent = entityManager.getComponent(playerId, HealthComponent.class);
			decreaseHealthForTesting(healthComponent);
			renderHealthBar(healthComponent.health);
		} catch (NullPointerException e) {
		} catch (IllegalArgumentException e) {
		}

		batch.end();
	}


	private void decreaseHealthForTesting(HealthComponent healthComponent) {
		while (time >= dieTime) {
			time -= dieTime;

			int newHealth = healthComponent.health - 1;
			if (newHealth < 0) {
				newHealth = 0;
			}
			healthComponent.health = newHealth;
		}
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
