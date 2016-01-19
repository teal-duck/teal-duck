package com.tealduck.game.world;


import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.EntityEngine;


/**
 *
 */
public class World {
	private final EntityEngine entityEngine;
	private final TiledMap tiledMap;

	private final int mapWidth;
	private final int mapHeight;
	private final int tileWidth;
	private final int tileHeight;

	private HashMap<String, ArrayList<Vector2>> patrolRoutes;


	/**
	 * @param entityEngine
	 * @param tiledMap
	 */
	public World(EntityEngine entityEngine, TiledMap tiledMap) {
		this.entityEngine = entityEngine;
		this.tiledMap = tiledMap;

		MapProperties prop = tiledMap.getProperties();
		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tileWidth = prop.get("tilewidth", Integer.class);
		tileHeight = prop.get("tileheight", Integer.class);

		patrolRoutes = new HashMap<String, ArrayList<Vector2>>();
	}


	/**
	 * @param routeName
	 * @return
	 */
	public ArrayList<Vector2> getPatrolRoute(String routeName) {
		return patrolRoutes.get(routeName);
	}


	/**
	 * @return
	 */
	public HashMap<String, ArrayList<Vector2>> getPatrolRoutes() {
		return patrolRoutes;
	}


	/**
	 * @param patrolRoutes
	 */
	public void addPatrolRoutes(HashMap<String, ArrayList<Vector2>> patrolRoutes) {
		this.patrolRoutes.putAll(patrolRoutes);
	}


	/**
	 * If tile is out of bounds, returns true (assumes collidable).
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isTileCollidable(int x, int y) {
		if ((x < 0) || (y < 0) || (x > mapWidth) || (y > mapHeight)) {
			return true;
		} else {

			TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("Collision");

			Cell cell = layer.getCell(x, y);
			if (cell == null) {
				return false;
			}

			String collidableProperty = cell.getTile().getProperties().get("collidable", String.class);
			return ((collidableProperty != null) && collidableProperty.equals("true"));
		}
	}


	/**
	 * @param pixelX
	 * @return
	 */
	public int xPixelToTile(float pixelX) {
		return MathUtils.floor(pixelX / tileWidth);
	}


	/**
	 * @param pixelY
	 * @return
	 */
	public int yPixelToTile(float pixelY) {
		return MathUtils.floor(pixelY / tileHeight);
	}


	/**
	 * @param pixelX
	 * @return
	 */
	public float xPixelToTileExact(float pixelX) {
		return pixelX / tileWidth;
	}


	/**
	 * @param pixelY
	 * @return
	 */
	public float yPixelToTileExact(float pixelY) {
		return pixelY / tileHeight;
	}


	/**
	 * @param tileX
	 * @return
	 */
	public float xTileToPixel(float tileX) {
		return tileX * tileWidth;
	}


	/**
	 * @param tileY
	 * @return
	 */
	public float yTileToPixel(float tileY) {
		return tileY * tileHeight;
	}


	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2 pixelToTile(float x, float y) {
		return new Vector2(xPixelToTile(x), yPixelToTile(y));
	}


	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2 pixelToTileExact(float x, float y) {
		return new Vector2(xPixelToTileExact(x), yPixelToTileExact(y));
	}


	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2 tileToPixel(float x, float y) {
		return new Vector2(xTileToPixel(x), yTileToPixel(y));
	}


	/**
	 * @param pixel
	 * @return
	 */
	public Vector2 pixelToTile(Vector2 pixel) {
		if (pixel == null) {
			throw new IllegalArgumentException("pixel is null");
		}
		return new Vector2(xPixelToTile(pixel.x), yPixelToTile(pixel.y));
	}


	/**
	 * @param pixel
	 * @return
	 */
	public Vector2 pixelToTileExact(Vector2 pixel) {
		if (pixel == null) {
			throw new IllegalArgumentException("pixel is null");
		}
		return new Vector2(xPixelToTileExact(pixel.x), yPixelToTileExact(pixel.y));
	}


	/**
	 * @param tile
	 * @return
	 */
	public Vector2 tileToPixel(Vector2 tile) {
		if (tile == null) {
			throw new IllegalArgumentException("tile is null");
		}
		return new Vector2(xTileToPixel(tile.x), yTileToPixel(tile.y));
	}


	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isPixelCollidable(float x, float y) {
		return isTileCollidable(xPixelToTile(x), yPixelToTile(y));
	}


	/**
	 * @param pixel
	 * @return
	 */
	public boolean isPixelCollidable(Vector2 pixel) {
		return isPixelCollidable(pixel.x, pixel.y);
	}


	/**
	 * @return
	 */
	public TiledMap getTiledMap() {
		return tiledMap;
	}


	/**
	 * @return
	 */
	public EntityEngine getEntityEngine() {
		return entityEngine;
	}


	/**
	 * @return
	 */
	public int getMapWidth() {
		return mapWidth;
	}


	/**
	 * @return
	 */
	public int getMapHeight() {
		return mapHeight;
	}


	/**
	 * @return
	 */
	public int getTileWidth() {
		return tileWidth;
	}


	/**
	 * @return
	 */
	public int getTileHeight() {
		return tileHeight;
	}

}
