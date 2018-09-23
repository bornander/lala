package com.bornander.libgdx;

public class Timeout {

	public enum State {
		STOPPED,
		RUNNING,
		ELAPSED,
		EXPIRED
	}
	
	private final float duration;
	private float elapsed;
	
	private State state = State.STOPPED;
	
	public Timeout(float duration) {
		this.duration = duration;
	}
	
	public boolean start() {
		if (state == State.STOPPED) {
			state = State.RUNNING;
			return true;
		}
		else {
			return false;
		}
	}
	
	public void reset() {
		elapsed = 0;
		state = State.STOPPED;
	}
	
	public void update(float delta) {
		if (state != State.STOPPED)
			elapsed += delta;
	}
	
	public State getState() {
		if (elapsed >= duration) {
			if (state == State.RUNNING) {
				state = State.ELAPSED;
			}
			else {
				state = State.EXPIRED;
			}
		}
		return state;
	}
	
}