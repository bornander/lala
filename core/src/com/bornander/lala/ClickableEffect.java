package com.bornander.lala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bornander.libgdx.Log;

public class ClickableEffect {
	
	private final Rectangle area;
	private final String effectName;
	private final Vector2 tileSize;
	private PooledEffect effect = null;
	
	public ClickableEffect(RectangleMapObject rmo, Vector2 tileSize) {
		Rectangle r = rmo.getRectangle();
		this.tileSize = tileSize;
		area = new Rectangle(
				r.x / tileSize.x,
				r.y / tileSize.y,
				r.width / tileSize.x,
				r.height / tileSize.y);		
		
		effectName = rmo.getProperties().get("effect", String.class);
	}
	
	public void check(Vector2 point) {
		if (area.contains(point)) {
			effect = Assets.instance.effects.obtainClickable(effectName);
			
			effect.setPosition(point.x, point.y);
			for(ParticleEmitter emitter : effect.getEmitters()) {
				emitter.getXOffsetValue().setLow(0, area.width / tileSize.x);
				emitter.getYOffsetValue().setLow(0, area.height / tileSize.y);
				/*
				emitter.getEmission().setLow(
						getProperty(rectangleObject, "emission_low", 1), 
						getProperty(rectangleObject, "emission_high", 1));
				*/
			}

			
			
			effect.reset();
			effect.start();
			Assets.instance.sounds.playRandomMushroom();
			Gdx.input.vibrate(150);
			
		}
	}
	
	
	public void update(float delta) {
		if (effect != null) {
			effect.update(delta);
			if (effect.isComplete()) {
				Log.info("EFFECT %s is DONE", effect);
				Assets.instance.effects.releaseClickable(effectName, effect);
				effect = null;
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		if (effect != null) {
			Log.info("render %s", effect);
			effect.draw(batch);
		}
	}
	

}
