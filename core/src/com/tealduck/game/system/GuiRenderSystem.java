package com.tealduck.game.system;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.tealduck.game.AssetLocations;
import com.tealduck.game.Tag;
import com.tealduck.game.TextureMap;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.ScoreComponent;
import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.world.EntityConstants;


/**
 *
 */
public class GuiRenderSystem extends GameSystem {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;

	private static final float HEALTH_TEXT_X = 10;
	private static final float HEALTH_TEXT_Y_OFFSET = 10;
	private static final int HEALTH_HEART_SIZE = 20;

	private static final int HEALTH_HEART_GAP = 8;
	private static final int CORNER_RADIUS = GuiRenderSystem.HEALTH_HEART_GAP * 2;

	private static final int RIGHT_EXTRA = 0; // 24;

	private static final Color BACKGROUND_COLOUR = Color.DARK_GRAY; // Color.BLUE;

	private final float backgroundWidth;
	private final float backgroundHeight;

	private int screenWidth;
	private int screenHeight;

	private GlyphLayout healthText;

	private Texture healthHeartTexture;
	private Texture backgroundTexture;
	private Texture ammoBulletTexture;

	private ShapeRenderer shapeRenderer;


	/**
	 * @param entityEngine
	 * @param batch
	 * @param camera
	 * @param font
	 */
	public GuiRenderSystem(EntityEngine entityEngine, SpriteBatch batch, OrthographicCamera camera, BitmapFont font,
			TextureMap textureMap) {
		super(entityEngine);
		this.batch = batch;
		this.camera = camera;
		this.font = font;

		healthText = new GlyphLayout(font, "Health:");
		healthHeartTexture = textureMap.getTexture(AssetLocations.HEALTH_BAR);

		ammoBulletTexture = textureMap.getTexture(AssetLocations.AMMO_BAR);

		// TODO: Replace gui backgrounds with 9-patch
		int maxHealth = EntityConstants.PLAYER_MAX_HEALTH;
		backgroundWidth = (GuiRenderSystem.HEALTH_TEXT_X + healthText.width
				+ (maxHealth * (GuiRenderSystem.HEALTH_HEART_SIZE + GuiRenderSystem.HEALTH_HEART_GAP))
				+ GuiRenderSystem.HEALTH_HEART_GAP);
		backgroundHeight = (((GuiRenderSystem.HEALTH_TEXT_Y_OFFSET + GuiRenderSystem.HEALTH_HEART_SIZE) - 1)
				+ GuiRenderSystem.HEALTH_HEART_GAP);

		backgroundTexture = generateBackground(backgroundWidth, backgroundHeight, GuiRenderSystem.CORNER_RADIUS,
				GuiRenderSystem.BACKGROUND_COLOUR);

		shapeRenderer = new ShapeRenderer();
	}


	/**
	 * @param width
	 * @param height
	 * @param cornerRadius
	 * @param colour
	 * @return
	 */
	public Texture generateBackground(float width, float height, int cornerRadius, Color colour) {
		Pixmap backgroundPixmap = new Pixmap((int) width + cornerRadius, (int) height + cornerRadius,
				Format.RGBA8888);
		backgroundPixmap.setColor(colour);

		int cr = cornerRadius;
		int pw = backgroundPixmap.getWidth();
		int ph = backgroundPixmap.getHeight();

		backgroundPixmap.fillRectangle(cr, 0, pw - (cr * 2), cr);
		backgroundPixmap.fillRectangle(cr, ph - cr, pw - (cr * 2), cr);
		backgroundPixmap.fillRectangle(0, cr, pw, ph - (cr * 2));

		backgroundPixmap.fillCircle(cr, cr, cr);
		backgroundPixmap.fillCircle(pw - cr, cr, cr);
		backgroundPixmap.fillCircle(cr, ph - cr, cr);
		backgroundPixmap.fillCircle(pw - cr, ph - cr, cr);

		return new Texture(backgroundPixmap);
	}


	/**
	 * @param windowWidth
	 * @param windowHeight
	 */
	public void resizeCamera(int windowWidth, int windowHeight) {
		screenWidth = windowWidth;
		screenHeight = windowHeight;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tealduck.game.engine.GameSystem#update(float)
	 */
	@Override
	public void update(float deltaTime) {
		EntityManager entityManager = getEntityManager();
		EntityTagManager entityTagManager = getEntityTagManager();

		batch.setProjectionMatrix(camera.combined);
		batch.enableBlending();

		try {
			int playerId = entityTagManager.getEntity(Tag.PLAYER);

			HealthComponent healthComponent = entityManager.getComponent(playerId, HealthComponent.class);
			renderHealthBar(healthComponent.health);

			WeaponComponent weaponComponent = entityManager.getComponent(playerId, WeaponComponent.class);
			renderWeaponAmmo(weaponComponent);

			ScoreComponent scoreComponent = entityManager.getComponent(playerId, ScoreComponent.class);
			renderScore(scoreComponent);

			MovementComponent movementComponent = entityManager.getComponent(playerId,
					MovementComponent.class);
			renderSprint(movementComponent);
		} catch (NullPointerException e) {
		} catch (IllegalArgumentException e) {
		}

	}


	/**
	 * @param health
	 */
	public void renderHealthBar(int health) {
		batch.begin();

		if (health < 0) {
			health = 0;
		}

		float backgroundX = -GuiRenderSystem.CORNER_RADIUS;
		float backgroundY = (screenHeight - backgroundTexture.getHeight()) + GuiRenderSystem.CORNER_RADIUS;
		batch.draw(backgroundTexture, backgroundX, backgroundY);

		float healthX = GuiRenderSystem.HEALTH_TEXT_X;
		float healthY = screenHeight - GuiRenderSystem.HEALTH_TEXT_Y_OFFSET;

		font.draw(batch, healthText, GuiRenderSystem.HEALTH_TEXT_X, healthY);

		healthX += healthText.width + GuiRenderSystem.HEALTH_HEART_GAP;
		healthY -= GuiRenderSystem.HEALTH_HEART_SIZE - 1;

		healthX -= 22;
		healthY -= 22;

		for (int i = 0; i < health; i += 1) {
			batch.draw(healthHeartTexture, healthX, healthY);
			healthX += GuiRenderSystem.HEALTH_HEART_SIZE + GuiRenderSystem.HEALTH_HEART_GAP;
		}

		batch.end();
	}


	/**
	 * @param x
	 * @param y
	 * @param textX
	 * @param textY
	 * @param text
	 */
	private void renderBackgroundWithTexture(float x, float y, float textX, float textY, String text) {
		batch.draw(backgroundTexture, x, y);
		font.draw(batch, text, textX, textY);
	}


	/**
	 * @param scoreComponent
	 */
	private void renderScore(ScoreComponent scoreComponent) {
		batch.begin();

		float scoreX = (screenWidth - backgroundTexture.getWidth()) + GuiRenderSystem.CORNER_RADIUS
				+ GuiRenderSystem.RIGHT_EXTRA;
		float scoreY = (screenHeight - backgroundTexture.getHeight()) + GuiRenderSystem.CORNER_RADIUS;
		float scoreTextX = scoreX + GuiRenderSystem.HEALTH_TEXT_X;
		float scoreTextY = screenHeight - GuiRenderSystem.HEALTH_TEXT_Y_OFFSET;
		String text = "Score: " + scoreComponent.score;

		if (scoreComponent.combo > 0) {
			text += " + " + scoreComponent.workingScore;
			if (scoreComponent.combo > 1) {
				text += " x" + scoreComponent.combo;
			}
		}

		renderBackgroundWithTexture(scoreX, scoreY, scoreTextX, scoreTextY, text);

		batch.end();
	}


	/**
	 * @param weaponComponent
	 */
	private void renderWeaponAmmo(WeaponComponent weaponComponent) {
		batch.begin();

		float ammoX = (screenWidth - backgroundTexture.getWidth()) + GuiRenderSystem.CORNER_RADIUS
				+ GuiRenderSystem.RIGHT_EXTRA;
		float ammoY = -GuiRenderSystem.CORNER_RADIUS;
		float ammoTextX = ammoX + GuiRenderSystem.HEALTH_TEXT_X;
		float ammoTextY = (-GuiRenderSystem.CORNER_RADIUS + backgroundTexture.getHeight())
				- GuiRenderSystem.HEALTH_TEXT_Y_OFFSET;

		String text = "Ammo: ";
		text += weaponComponent.ammoInClip;
		text += "/" + weaponComponent.getClipSize();
		text += " (" + weaponComponent.extraAmmo + ")";
		if (weaponComponent.isReloading()) {
			text += " R";
		}

		renderBackgroundWithTexture(ammoX, ammoY, ammoTextX, ammoTextY, text);

		float x = screenWidth - 50;
		float y = 25;
		for (int i = 0; i < weaponComponent.ammoInClip; i += 1) {
			if (weaponComponent.isReloading()) {
				batch.setColor(Color.RED);
			} else {
				batch.setColor(Color.WHITE);
			}
			batch.draw(ammoBulletTexture, x, y);
			y += 30;
		}
		batch.setColor(Color.WHITE);

		batch.end();
	}


	/**
	 * @param movementComponent
	 */
	private void renderSprint(MovementComponent movementComponent) {
		batch.begin();

		float sprintX = -GuiRenderSystem.CORNER_RADIUS;
		float sprintY = -GuiRenderSystem.CORNER_RADIUS;
		float sprintTextX = GuiRenderSystem.HEALTH_TEXT_X;
		float sprintTextY = (-GuiRenderSystem.CORNER_RADIUS + backgroundTexture.getHeight())
				- GuiRenderSystem.HEALTH_TEXT_Y_OFFSET;

		String text = "Sprint: ";

		renderBackgroundWithTexture(sprintX, sprintY, sprintTextX, sprintTextY, text);
		batch.end();

		float sprintBarX = sprintTextX + 65;
		float sprintBarY = sprintTextY - 20;
		float sprintBarWidth = 130;
		float sprintBarHeight = 20;

		float maxSprintTime = movementComponent.maxSprintTime;
		float sprintTime = movementComponent.sprintTime;

		float sprintBarFillWidth;
		if (movementComponent.sprinting) {
			sprintBarFillWidth = (sprintBarWidth / maxSprintTime) * (sprintTime);
		} else {
			sprintBarFillWidth = (sprintBarWidth / maxSprintTime) * (sprintTime);
		}

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(sprintBarX, sprintBarY, sprintBarWidth, sprintBarHeight);

		if (movementComponent.usedAllSprint) {
			shapeRenderer.setColor(Color.RED);
		} else if (sprintTime >= maxSprintTime) {
			shapeRenderer.setColor(Color.GREEN);
		} else {
			shapeRenderer.setColor(Color.YELLOW);
		}
		shapeRenderer.rect(sprintBarX, sprintBarY, sprintBarFillWidth, sprintBarHeight);

		shapeRenderer.end();
	}
}
