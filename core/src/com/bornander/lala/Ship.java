package com.bornander.lala;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bornander.lala.assets.ShipAssets;

import java.util.Random;

public class Ship {
	
	private final static Random RND = new Random();

	private final ShipAssets assets;
	
	private boolean hasPilot;
	private float elapsed;
	private final Vector2 position = new Vector2();
	private float angle;
	
	private final Vector2 unitSize = new Vector2();
	private final Vector2 halfUnitSize = new Vector2();
	private final ParticleEffect smokeEffect = Assets.instance.effects.ship_smoke.obtain();
	private final float periodModifier = RND.nextFloat();
	
	public Ship(LevelData leveInfo) {
		this.assets = Assets.instance.ships.get(leveInfo.alienName);
		this.position.set(leveInfo.position_target);
		setTexture();
	}
	
	private TextureRegion getTexture() {
		return hasPilot ? assets.manned : assets.unmanned;
	}
	
	private void setTexture() {
		TextureRegion texture = getTexture();
		float w = (float)texture.getRegionWidth();
		float h = (float)texture.getRegionHeight();
		
		unitSize.set(1, h/w);
		unitSize.scl(1.6f);
		halfUnitSize.set(unitSize).scl(0.5f);
	}
	
	public boolean update(float delta) {
		if (hasPilot) {
			elapsed += delta;
			angle = ((MathUtils.sin(elapsed)) * MathUtils.PI/20.0f)*MathUtils.radiansToDegrees;
			position.y += 1.0f * delta;
			position.x -= (1.0f + MathUtils.sin(elapsed * (2.0f * (1.0f + periodModifier)))) * delta;
			smokeEffect.setPosition(position.x, position.y);
			smokeEffect.update(delta);
		}
		return false;
	}
	
	public void render(SpriteBatch batch) {
		smokeEffect.draw(batch);
		batch.draw(getTexture(), 
				position.x - halfUnitSize.x,
				position.y - 0.5f/* - halfUnitSize.y*/,
				halfUnitSize.x,
				halfUnitSize.y, 
				unitSize.x, 
				unitSize.y, 
				1, 1,
				angle);
	}

	public void enterPilot() {
		hasPilot = true;
		setTexture();
		Assets.instance.sounds.playUfoFly();
		smokeEffect.start();
	}
}