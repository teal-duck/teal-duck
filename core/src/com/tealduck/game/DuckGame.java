package com.tealduck.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.components.PositionComponent;
import com.tealduck.game.components.SpriteComponent;
import com.tealduck.game.engine.Component;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.systems.RenderSystem;


public class DuckGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	EntityManager entityManager;
	EntityTagManager entityTagManager;
	RenderSystem renderSystem;


	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		entityManager = new EntityManager();
		entityTagManager = new EntityTagManager();
		entityTagManager.addTag("DUCK", entityManager.createEntity());
		entityManager.addComponent(entityTagManager.getEntity("DUCK"), new SpriteComponent(img));
		entityManager.addComponent(entityTagManager.getEntity("DUCK"), new PositionComponent(new Vector2(0, 0)));
		renderSystem = new RenderSystem(entityManager, batch);
	}


	@Override
	public void render() {
		renderSystem.update(50.0f); 
	}
	
}
