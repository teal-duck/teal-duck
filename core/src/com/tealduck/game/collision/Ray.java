package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.world.World;


/**
 * Represent a ray as 2 vectors: origin and direction.
 */
public class Ray {
	public Vector2 origin;
	public Vector2 direction;


	/**
	 * Create a ray with origin at (0, 0).
	 * 
	 * @param direction
	 */
	public Ray(Vector2 direction) {
		this(new Vector2(0, 0), direction);
	}


	/**
	 * @param origin
	 * @param direction
	 */
	public Ray(Vector2 origin, Vector2 direction) {
		this.origin = origin;
		this.direction = direction.cpy().nor();
	}


	@Override
	public String toString() {
		return "Ray(" + origin.toString() + ", " + direction.toString() + ")";
	}


	/**
	 * Returns coordinates of tile it intersects, null if no intersection
	 *
	 * @param world
	 * @param maxLengthTiles
	 * @return
	 */
	public Vector2 worldIntersection(World world, float maxLengthTiles) {
		// http://www.cse.yorku.ca/~amana/research/grid.pdf
		// http://stackoverflow.com/questions/12367071/how-do-i-initialize-the-t-variables-in-a-fast-voxel-traversal-algorithm-for-ray

		// http://gamedev.stackexchange.com/questions/47362/cast-ray-to-select-block-in-voxel-game

		float tileSize = 64f;
		Vector2 d = direction.cpy().nor().scl(tileSize);
		Vector2 p0 = origin.cpy();
		// Vector2 p1 = origin.cpy().mulAdd(d, maxLengthTiles);

		float deltaX = d.x;
		float deltaY = d.y;
		float tMaxX = intbound(origin.x, deltaX);
		float tMaxY = intbound(origin.y, deltaY);

		int stepX = (int) Math.signum(deltaX);
		int stepY = (int) Math.signum(deltaY);

		float tDeltaX = stepX / deltaX;
		float tDeltaY = stepY / deltaY;

		int x = (int) (p0.x / tileSize);
		int y = (int) (p0.y / tileSize);

		float radius = maxLengthTiles / d.len();

		while (true) {
			if (tMaxX < tMaxY) {
				if (tMaxX > radius) {
					break;
				}
				tMaxX += tDeltaX;
				x += stepX;
			} else {
				if (tMaxY > radius) {
					break;
				}
				tMaxY += tDeltaY;
				y += stepY;
			}

			if (world.isTileCollidable(x, y)) {
				break;
			}
		}

		if (world.isTileCollidable(x, y)) {
			return new Vector2(x, y);
		} else {
			return null;
		}
	}


	/**
	 * @param s
	 * @param ds
	 * @return
	 */
	private float intbound(float s, float ds) {
		if (ds < 0) {
			if (mod(s, ds) == 0) {
				return 0;
			}
			return intbound(-s, -ds);
		} else {
			s = mod(s, 1);
			return (1 - s) / ds;
		}
	}


	/**
	 * @param value
	 * @param modulus
	 * @return
	 */
	private float mod(float value, float modulus) {
		return ((value % modulus) + modulus) % modulus;
	}
}
