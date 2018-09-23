package com.bornander.lala;

import com.badlogic.gdx.math.Vector2;

public class Transform {

	public final Vector2 position = new Vector2();
	
	public float angle;
	public boolean mirrored;
	
	private boolean hasPushed = false;
	private final Vector2 position_stored = new Vector2();
	private float angle_stored = 0.0f;
	
	public Transform(Vector2 position, float angle) {
		this.position.set(position);
		this.angle = angle;
	}
	
	public Transform() {
		this(Vector2.Zero, 0);
	}
	
	@Override
	public String toString() {
		return String.format("(%.3f, %.3f)@%.2f %s", position.x, position.y, angle, mirrored ? "Mirrored" : "Normal");
	}
	
	public void set(Transform other) {
		position.set(other.position);
		angle = other.angle;
		mirrored = other.mirrored;
	}
	
	public void set(Vector2 position, float angle) {
		this.position.set(position);
		this.angle = angle;
	}

	public void reset() {
		position.set(Vector2.Zero);
		angle = 0.0f;
	}

	public void mirror() {
		mirrored = !mirrored;
		switch((int)angle) {
		case 90: angle = 270; break;
		case 270: angle = 90; break;
		}
	}
	
	public void push() {
		if (hasPushed)
			throw new RuntimeException("Cannot push when already pushed!");
		
		hasPushed = true;
		position_stored.set(position);
		angle_stored = angle;
	}
	
	public void pop() {
		if (!hasPushed)
			throw new RuntimeException("Cannot pop when not pushed!");
		
		hasPushed = false;
		position.set(position_stored);
		angle = angle_stored;
	}
}