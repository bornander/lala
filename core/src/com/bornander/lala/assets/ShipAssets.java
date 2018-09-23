package com.bornander.lala.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ShipAssets {
	public final TextureRegion unmanned;
	public final TextureRegion manned;
	
	public ShipAssets(TextureAtlas atlas, String prefix) {
		unmanned = atlas.findRegion(prefix);
		manned = atlas.findRegion(prefix + "_manned");
	}
}
