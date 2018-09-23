package com.bornander.libgdx;

import com.badlogic.gdx.math.MathUtils;

public class Lerper {
	
	private final FloatConsumer consumer;
	private final float duration;
	private final float from;
	private final float to;

	private float elapsed;
	
	public Lerper(FloatConsumer consumer, float duration, float from, float to) {
		this.consumer = consumer;
		this.duration = duration;
		this.from = from;
		this.to = to;
	}
	
	public boolean update(float delta) {
		elapsed += delta;
		float fraction = Math.min(elapsed / duration, 1.0f);
		float value = MathUtils.lerp(from, to, fraction);
		consumer.updateValue(value);
		return fraction >= 1.0f;
	}
}