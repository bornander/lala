package com.bornander.lala.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

public class AliensAssets {

	private final AlienAssets blue;
	private final AlienAssets beige;
	private final AlienAssets green;
	private final AlienAssets pink;
	private final AlienAssets yellow;
	
	private final Map<String, AlienAssets> aliens = new HashMap<String, AlienAssets>();
	
	public AliensAssets(TextureAtlas atlas) {
		beige = new AlienAssets(atlas, "alienBeige");
		blue = new AlienAssets(atlas, "alienBlue");
		green = new AlienAssets(atlas, "alienGreen");
		pink = new AlienAssets(atlas, "alienPink");
		yellow = new AlienAssets(atlas, "alienYellow");
		
		aliens.put("beige", beige);
		aliens.put("blue", blue);
		aliens.put("green", green);
		aliens.put("pink", pink);
		aliens.put("yellow", yellow);
	}
	
	public final AlienAssets getAlien(String name) {
		return aliens.get(name);
	}
}
