package com.bornander.lala.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.bornander.libgdx.Temp;

public class Hud {
	private static Vector2 lastTouchPoint = new Vector2();
	private static Vector2 temp = new Vector2();
	
	public static void dragTouchDown(int screenX, int screenY) {
		lastTouchPoint.set(screenX, screenY);
	}
	
	public static float dragTouchDrag(int screenX, int screenY, OrthographicCamera camera) {
		Vector2 delta = temp.set(screenX, screenY).sub(lastTouchPoint);
		Vector2 worldDelta = Temp.unproject(camera, (int)delta.x, (int)delta.y);
		float left = camera.position.x - camera.viewportWidth / 2.0f;
		float horizontalDelta = left - worldDelta.x;
		camera.translate(horizontalDelta, 0.0f);
		lastTouchPoint.set(screenX, screenY);
		return horizontalDelta;
	}
}
