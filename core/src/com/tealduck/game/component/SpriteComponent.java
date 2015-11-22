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
}