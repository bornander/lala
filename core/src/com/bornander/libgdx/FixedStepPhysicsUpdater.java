package com.bornander.libgdx;

import com.badlogic.gdx.physics.box2d.World;

public class FixedStepPhysicsUpdater {

	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	private static final float STEP_SIZE = 1.0f / 120.0f;
	
	private final World world;
	
	private float accumulator = 0;
	
	public FixedStepPhysicsUpdater(World world) {
		this.world = world;
	}
	
	public void update(float delta) {
		accumulator += delta;
		while(accumulator >= STEP_SIZE) {
			world.step(STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			accumulator -= STEP_SIZE;
		}
	}

	public void doEmptyStep() {
		world.step(0, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}
}
