package com.tealduck.game.world;


import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;


@SuppressWarnings("rawtypes")
public class LevelLoader extends AsynchronousAssetLoader {
	// TODO: Write level loader to handle objects and paths unless TmxLoader does this already

	public LevelLoader(FileHandleResolver resolver) {
		super(resolver);
	}


	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, AssetLoaderParameters parameter) {
	}


	@Override
	public Object loadSync(AssetManager manager, String fileName, FileHandle file,
			AssetLoaderParameters parameter) {
		return null;
	}


	@Override
	public Array getDependencies(String fileName, FileHandle file, AssetLoaderParameters parameter) {
		return null;
	}

}
