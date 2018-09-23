package com.bornander.lala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bornander.lala.assets.AlienAssets;
import com.bornander.libgdx.Timeout;
import com.bornander.libgdx.Timeout.State;

import java.util.Random;

public class DancingAlien {

	private static final Random RND = new Random();
	private final Vector2 position = new Vector2();
	private float elapsed = RND.nextFloat()*2.0f;
	private Animation<TextureRegion> animation;
	private Timeout singTimeout = new Timeout(2);
	
	private Rectangle bounds;
	private AlienAssets assets;
	
	
	public DancingAlien(Vector2 position, AlienAssets assets) {
		this.assets = assets;
		this.position.set(position);
		animation = assets.getRandomDance();
		bounds = new Rectangle(position.x-0.5f, position.y-0.5f, 1, 1);
	}

	public void update(float delta) {
		elapsed += delta;
		singTimeout.update(delta);
		if (singTimeout.getState() == State.EXPIRED) {
			elapsed = 0;
			animation = assets.getRandomDance();
			singTimeout.reset();
		}
	}
	
	public boolean contains(Vector2 point) {
		return bounds.contains(point);
	}
	
	public void sing() {
		if (singTimeout.getState() == State.RUNNING)
			return;
		
		elapsed = 0;
		Assets.instance.sounds.playRandomSpeak();
		animation = assets.sing;
		singTimeout.reset();
		singTimeout.start();
		Gdx.input.vibrate(50);
	}
	
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(animation.getKeyFrame(elapsed), 
				position.x - 0.5f,
				position.y - 0.5f,
				0.5f,
				0.5f, 
				1, 
				2, 
				1, 1,
				0);		
	}
	
	public void renderBounds(ShapeRenderer sr) {
		sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
