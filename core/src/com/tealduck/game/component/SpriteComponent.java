package com.tealduck.game.component;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tealduck.game.engine.Component;


/**
 * Component containing the sprite for an entity.
 *
 * @author andrew
 *
 */
public class SpriteComponent extends Component {
	// TODO: Change SpriteComponent to use sprite sheets + animation
	/**
	 *
	 */
	public Sprite sprite;
	public Animation animation;
	public float stateTime = 0f;


	public SpriteComponent(Texture texture) {
		this(texture, null);
	}


	public SpriteComponent(Texture texture, Animation animation) {
		sprite = new Sprite(texture);
		this.animation = animation;
		setSpriteToAnimationFrame();
	}


	public void setSpriteToAnimationFrame() {
		if (animation != null) {
			// TODO: Fix animation hack
			sprite.setSize(64, 64);
			sprite.setRegion(animation.getKeyFrame(stateTime, true));
		}
	}


	@Override
	public String toString() {
		return "SpriteComponent(" + sprite.toString() + ")";
	}
}
