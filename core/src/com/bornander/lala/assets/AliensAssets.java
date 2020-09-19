package com.bornander.lala.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

public class AliensAssets {
	private final Map<String, AlienAssets> aliens = new HashMap<String, AlienAssets>();
	
	public AliensAssets(TextureAtlas atlas) {
		aliens.put("beige", new AlienAssets(atlas, "alienBeige"));
		aliens.put("blue", new AlienAssets(atlas, "alienBlue"));
		aliens.put("green", new AlienAssets(atlas, "alienGreen"));
		aliens.put("pink", new AlienAssets(atlas, "alienPink"));
		aliens.put("yellow", new AlienAssets(atlas, "alienYellow"));
	}
	
	public final AlienAssets getAlien(String name) {
		return aliens.get(name);
	}
}
