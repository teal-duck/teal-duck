package com.tealduck.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import com.tealduck.game.components.SpriteComponent;
import com.tealduck.game.engine.Component;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;


public class DuckGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	EntityManager entityManager;
	EntityTagManager entityTagManager;


	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		entityManager = new EntityManager();
		entityTagManager = new EntityTagManager();
		entityTagManager.addTag("DUCK", entityManager.createEntity());
		entityManager.addComponent(entityTagManager.getEntity("DUCK"), new SpriteComponent(img));
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for (Component component:entityManager.getAllComponentsOfType(SpriteComponent.class)){
			SpriteComponent renderComponent = (SpriteComponent) component;
			batch.draw(renderComponent.sprite, 0, 0);
		}
		batch.end();
	}
	
}
