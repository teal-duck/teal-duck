package com.tealduck.game.component;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tealduck.game.engine.Component;
import com.tealduck.game.world.EntityConstants;


/**
 * Component containing the sprite for an entity.
 *
 * @author andrew
 *
 */
public class SpriteComponent extends Component {
	public Sprite sprite;
	public Animation animation;
	public float stateTime = 0f;


	/**
	 * @param texture
	 */
	public SpriteComponent(Texture texture) {
		this(texture, null);
	}


	/**
	 * @param texture
	 * @param animation
	 */
	public SpriteComponent(Texture texture, Animation animation) {
		sprite = new Sprite(texture);
		this.animation = animation;
		setSpriteToAnimationFrame();
	}


	/**
	 * Sets the region of the sprite to the current key frame in the animation.
	 */
	public void setSpriteToAnimationFrame() {
		if (animation != null) {
			sprite.setSize(EntityConstants.TILE_SIZE, EntityConstants.TILE_SIZE);
			sprite.setRegion(animation.getKeyFrame(stateTime, true));
		}
	}


	@Override
	public String toString() {
		return "SpriteComponent(" + sprite.toString() + ", " + animation.toString() + ", " + stateTime + ")";
	}
}
