package com.tealduck.game;


import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;


/**
 * Stores all the loaded textures as a map from strings to the instances.
 */
public class TextureMap {
	private HashMap<String, Texture> textures;


	/**
	 * 
	 */
	public TextureMap() {
		textures = new HashMap<String, Texture>();
	}


	/**
	 * Puts a texture into the map.
	 * 
	 * @param name
	 * @param texture
	 */
	public void putTexture(String name, Texture texture) {
		textures.put(name, texture);
	}


	/**
	 * Gets the texture from the asset manager and puts it into the map under the same key as used for the asset
	 * manager.
	 * 
	 * @param name
	 * @param assetManager
	 */
	public void putTextureFromAssetManager(String name, AssetManager assetManager) {
		putTexture(name, (Texture) assetManager.get(name));
	}


	/**
	 * Gets the texture from the asset manager with key assetName, then puts it into the map with key textureName.
	 * 
	 * @param textureName
	 * @param assetName
	 * @param assetManager
	 */
	public void putTextureFromAssetManager(String textureName, String assetName, AssetManager assetManager) {
		putTexture(textureName, (Texture) assetManager.get(assetName));
	}


	/**
	 * Gets a texture from the map. Returns null if there is no texture for the key.
	 * 
	 * @param name
	 * @return texture or null
	 */
	public Texture getTexture(String name) {
		return textures.get(name);
	}


	/**
	 * Clears the texture map. This does not dispose of the textures.
	 */
	public void clear() {
		textures.clear();
	}
}
