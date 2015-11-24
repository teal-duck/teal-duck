package com.tealduck.game.world;


import java.util.Random;


public class World {
	private int width;
	private int height;

	// TODO: Decide on representation of a tile
	// E.g. class Tile, then class Wall extends Tile etc (but then we end up with what ECS tries to avoid)
	// enum Tile { WALL, DOOR } etc
	// Integer ID
	// Bit fields

	// Tiles array is tiles[row][column]
	private boolean[][] tiles;


	public World(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new boolean[height][width];

		initTiles();
	}


	private void initTiles() {
		Random random = new Random();

		for (int y = 0; y < height; y += 1) {
			for (int x = 0; x < width; x += 1) {
				boolean value = false;

				if ((x == 0) || (x == (width - 1)) || (y == 0) || (y == (height - 1))) {
					value = true;
				} else if (y == 1) {
					value = true;
				} else {
					value = (random.nextInt(10) < 2);
				}

				tiles[y][x] = value;
			}
		}
	}


	public boolean isInWidth(int x) {
		return ((x >= 0) && (x < width));
	}


	public boolean isInHeight(int y) {
		return ((y >= 0) && (y < height));
	}


	public boolean isInBounds(int x, int y) {
		return isInWidth(x) && isInHeight(y);
	}


	// TODO: Add bounds checking to tile getters and setters
	public boolean getTile(int x, int y) {
		return tiles[y][x];
	}


	public void setTile(int x, int y, boolean value) {
		tiles[y][x] = value;
	}


	public boolean isTileSolid(int x, int y) {
		return tiles[y][x];
	}


	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}


	public static int projectTileLocationToPixel(int tileLocation, int tileSize) {
		return tileLocation * tileSize;
	}


	public static int projectPixelLocationToTile(float pixel, int tileSize) {
		return (int) Math.floor(pixel / tileSize);
	}
}
