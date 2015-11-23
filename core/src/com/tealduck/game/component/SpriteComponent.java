package com.tealduck.game.component;


import com.badlogic.gdx.graphics.Texture;
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


	/**
	 *
	 * @param texture
	 *                Texture used to create Sprite.
	 */
	public SpriteComponent(Texture texture) {
		sprite = new Sprite(texture);
	}


	@Override
	public String toString() {
		return "SpriteComponent(" + sprite.toString() + ")";
	}
}
