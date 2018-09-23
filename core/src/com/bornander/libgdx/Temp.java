package com.bornander.libgdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

public class Temp {
	
	private static class Vector2Pool extends Pool<Vector2> {
		public Vector2Pool(int initialCapacity) {
			super(initialCapacity);
		}
		
		@Override
		protected Vector2 newObject() {
			return new Vector2();
		}
	}
	
	public static final Pool<Vector2> vector2 = new Vector2Pool(32);
	
	private final static Vector3 unprojectV3 = new Vector3();
	private final static Vector2 unprojectV2 = new Vector2();
	
	public static Vector2 unproject(OrthographicCamera camera, int x, int y) {
		camera.unproject(unprojectV3.set(x, y, 0));
		return unprojectV2.set(unprojectV3.x, unprojectV3.y);
	}
	
}
