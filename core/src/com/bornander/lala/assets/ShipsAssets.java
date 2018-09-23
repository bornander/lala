package com.bornander.lala.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

public class ShipsAssets {

	private final ShipAssets beige;
	private final ShipAssets blue;
	private final ShipAssets green;
	private final ShipAssets pink;
	private final ShipAssets yellow;
	
	
	private final Map<String, ShipAssets> ships = new HashMap<String, ShipAssets>();
	
	public ShipsAssets(TextureAtlas atlas) {
		beige = new ShipAssets(atlas, "shipBeige");
		blue = new ShipAssets(atlas, "shipBlue");
		green = new ShipAssets(atlas, "shipGreen");
		pink = new ShipAssets(atlas, "shipPink");
		yellow = new ShipAssets(atlas, "shipYellow");
		
		ships.put("beige", beige);
		ships.put("blue", blue);
		ships.put("green", green);
		ships.put("pink", pink);
		ships.put("yellow", yellow);
	}
	
	public ShipAssets get(String name) {
		return ships.get(name);
	}
}
