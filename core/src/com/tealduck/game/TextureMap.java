package com.tealduck.game;


import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;


public class TextureMap {
	// TODO: Texture map tests and docs
	private HashMap<String, Texture> textures;


	public TextureMap() {
		textures = new HashMap<String, Texture>();
	}


	public void putTexture(String name, Texture texture) {
		textures.put(name, texture);
	}


	public void putTextureFromAssetManager(String name, AssetManager assetManager) {
		putTexture(name, (Texture) assetManager.get(name));
	}


	public void putTextureFromAssetManager(String textureName, String assetName, AssetManager assetManager) {
		putTexture(textureName, (Texture) assetManager.get(assetName));
	}


	public Texture getTexture(String name) {
		return textures.get(name);
	}


	public void clear() {
		textures.clear();
	}
}
