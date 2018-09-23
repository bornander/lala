package com.bornander.lala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.bornander.libgdx.FloatConsumer;

public class BlockVibrator {
	
	private final FloatConsumer consumer;
	private final float duration;
	private final float initial;

	private float elapsed;
	
	public BlockVibrator(FloatConsumer consumer, float duration, float initial) {
		this.consumer = consumer;
		this.duration = duration;
		this.initial = initial; 
	}
	
	public boolean update(float delta) {
		if (elapsed == 0.0f)
			Gdx.input.vibrate(100);
		
		elapsed += delta;
		float fraction = Math.min(elapsed / duration, 1.0f);
		float value = MathUtils.sin(fraction * MathUtils.PI2 * 2) * 10;
		consumer.updateValue(initial + value);
		return fraction >= 1.0f;
	}
}