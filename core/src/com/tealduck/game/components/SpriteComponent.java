package com.tealduck.game.components;


import com.tealduck.game.engine.Component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class SpriteComponent extends Component {
	public Sprite sprite;
	
	public SpriteComponent(Texture texture){
		sprite = new Sprite(texture);
	}
}
