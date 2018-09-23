package com.bornander.libgdx;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Box2DUtils {

	private final static Array<Body> bodies = new Array<Body>(256);
	
	private Box2DUtils() {
	}
	
	public static void destroyAllBodies(World world) {
		world.getBodies(bodies);
		for(Body body : bodies) {
			if (body != null) {
				world.destroyBody(body);
			}
		}
	}
}