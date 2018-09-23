package com.bornander.lala;

import com.badlogic.gdx.math.Vector2;

public class Terrain {
	
	public final Material material;
	public final Vector2[] polygon;
	
	public Terrain(Material material, Vector2[] polygon) {
		this.material = material;
		this.polygon = polygon;
	}
}