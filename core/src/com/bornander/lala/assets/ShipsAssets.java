package com.bornander.lala.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

public class ShipsAssets {
	private final Map<String, ShipAssets> ships = new HashMap<String, ShipAssets>();
	
	public ShipsAssets(TextureAtlas atlas) {
		ships.put("beige", new ShipAssets(atlas, "shipBeige"));
		ships.put("blue", new ShipAssets(atlas, "shipBlue"));
		ships.put("green", new ShipAssets(atlas, "shipGreen"));
		ships.put("pink", new ShipAssets(atlas, "shipPink"));
		ships.put("yellow", new ShipAssets(atlas, "shipYellow"));
	}
	
	public ShipAssets get(String name) {
		return ships.get(name);
	}
}
