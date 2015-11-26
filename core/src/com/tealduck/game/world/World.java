package com.tealduck.game.world;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class World {
	private TiledMap tiledMap;
	private OrthographicCamera mapCamera;
	private OrthogonalTiledMapRenderer mapRenderer;


	public World(TiledMap tiledMap) {
		this.tiledMap = tiledMap;
	}
}
