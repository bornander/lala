package com.bornander.lala.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffectAssets implements Disposable {
	public final ParticleEffectPool teleport_out;
	public final ParticleEffectPool teleport_in;
	public final ParticleEffectPool ship_smoke;
	public final ParticleEffectPool glow_sprites;
	public final ParticleEffectPool clouds;
	public final ParticleEffectPool star_rain;
	public final ParticleEffectPool leafs;
	public final ParticleEffectPool snow;
	public final ParticleEffectPool planet_dust;
	
	public final ParticleEffectPool walk_dust;
	public final ParticleEffectPool walk_dust_sand;
	public final ParticleEffectPool walk_dust_grass;

	public final ParticleEffectPool mushroom_red;
	public final ParticleEffectPool mushroom_brown;
	public final ParticleEffectPool mushroom_white;

	
	private final Map<String, ParticleEffectPool> namedMap = new HashMap<String, ParticleEffectPool>();
	private final Map<String, ParticleEffectPool> clickables = new HashMap<String, ParticleEffectPool>();
	private final List<ParticleEffect> effects = new ArrayList<ParticleEffect>(32);
	
	public EffectAssets(TextureAtlas textureAtlas) {
		teleport_out = new ParticleEffectPool(load(textureAtlas, "effects/teleport_out.effect"), 2, 2);
		teleport_in = new ParticleEffectPool(load(textureAtlas, "effects/teleport_in.effect"), 2, 2);
		ship_smoke = new ParticleEffectPool(load(textureAtlas, "effects/ship_smoke.effect"), 2, 2);
		glow_sprites = new ParticleEffectPool(load(textureAtlas, "effects/glow_sprites.effect"), 8, 8);
		clouds = new ParticleEffectPool(load(textureAtlas, "effects/clouds.effect"), 8, 8);
		star_rain = new ParticleEffectPool(load(textureAtlas, "effects/star_rain.effect"), 2, 2);
		leafs = new ParticleEffectPool(load(textureAtlas, "effects/leafs.effect"), 8, 8);
		snow = new ParticleEffectPool(load(textureAtlas, "effects/snow.effect"), 8, 8);
		
		walk_dust = new ParticleEffectPool(load(textureAtlas, "effects/walk_dust.effect"), 2, 2);
		walk_dust_sand = new ParticleEffectPool(load(textureAtlas, "effects/walk_dust_sand.effect"), 2, 2);
		walk_dust_grass = new ParticleEffectPool(load(textureAtlas, "effects/walk_dust_grass.effect"), 2, 2);
		
		planet_dust = new ParticleEffectPool(load(textureAtlas, "effects/planet_dust.effect"), 8, 8);

		mushroom_red = new ParticleEffectPool(load(textureAtlas, "effects/mushroom_red.effect"), 2, 2);
		mushroom_brown = new ParticleEffectPool(load(textureAtlas, "effects/mushroom_brown.effect"), 2, 2);
		mushroom_white = new ParticleEffectPool(load(textureAtlas, "effects/mushroom_white.effect"), 2, 2);
		
		
		namedMap.put("glow_sprites", glow_sprites);
		namedMap.put("clouds", clouds);
		namedMap.put("leafs", leafs);
		namedMap.put("snow", snow);
		namedMap.put("planet_dust", planet_dust);
		
		
		clickables.put("mushroom_red", mushroom_red);
		clickables.put("mushroom_brown", mushroom_brown);
		clickables.put("mushroom_white", mushroom_white);
		
		
	}
	
	private ParticleEffect load(TextureAtlas textureAtlas, String filename) {
		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal(filename), textureAtlas);
		effects.add(effect);
		return effect;
	}
	
	public ParticleEffect obtain(String name) {
		return namedMap.get(name).obtain();
	}

	public PooledEffect obtainClickable(String name) {
		return clickables.get(name).obtain();
	}
	
	public void releaseClickable(String name, PooledEffect effect) {
		clickables.get(name).free(effect);
	}

	@Override
	public void dispose() {
		for(ParticleEffect effect : effects)
			effect.dispose();
	}
}