package com.bornander.lala.assets;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.bornander.lala.Assets;
import com.bornander.libgdx.Log;

public class LevelAssets {
	private String loadedPath;
	private TiledMap loadedMap;
	
	public TiledMap load(String path) {
		Log.info("Loading level; %s", path);
		if (loadedPath != null) {
			Assets.instance.unload(loadedPath);
			loadedMap.dispose();
		}
		
		loadedMap = (TiledMap)Assets.instance.load(path, TiledMap.class);
		loadedPath = path;
		return loadedMap;
	}
}