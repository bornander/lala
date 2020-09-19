package com.bornander.lala;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Flier implements Npc {
	
	private enum State {
		PERCHED,
		FLYING,
		RETURNING
	}
	
	private final Rectangle area;
	private final Vector2 perchPosition = new Vector2();
	private final Vector2 position = new Vector2();
	private final Vector2 target = new Vector2();
	private final Vector2 velocity = new Vector2();
	private final Vector2 deltaVector = new Vector2();
	private final Animation<TextureRegion> animation;
	
	
	private State state = State.PERCHED;
	
	private float elapsed;
	private float flyingLeft;
	private float scale;
	
	private Alien alien;
	
	public Flier(Rectangle area, String skin) {
		this.area = area;
		
		animation = skin.equals("bee") ? Assets.instance.npcs.bee : Assets.instance.npcs.fly;
		
		area.getCenter(perchPosition);
		perchPosition.y -= area.height / 2.0f;
		position.set(perchPosition);
		target.set(perchPosition);
		scale = MathUtils.random(0.3f, 0.4f);
	}
	
	private void updatePerched() {
		deltaVector.set(alien.body.getWorldCenter()).sub(position);
		if (deltaVector.len() < 1.0f) {
			state = State.FLYING;
			elapsed = 0;
		}
	}
	
	private void updateFlying(float delta) {
		deltaVector.set(target).sub(position);
		
		if (deltaVector.len() < 0.1f) {
			float x = MathUtils.random(area.x, area.x + area.width);
			float y = MathUtils.random(area.y, area.y + area.height);
			target.set(x, y); 
		}
		
		deltaVector.y /= 2.0f;
		deltaVector.nor().scl(delta).scl(0.1f);
		velocity.add(deltaVector);
		if (velocity.len() > 0.1f) {
			velocity.nor().scl(0.1f);
		}
		position.add(velocity);
		
		deltaVector.set(perchPosition).sub(alien.body.getWorldCenter());
		if (elapsed > 4.0f && deltaVector.len() > 1.0f) {
			state = State.RETURNING;
		}
	}
	
	private void updateReturning(float delta) {
		deltaVector.set(alien.body.getWorldCenter()).sub(position);

		deltaVector.set(perchPosition).sub(position);
		
		if (deltaVector.len() < 0.2f) {
			state = State.PERCHED;
		}
		else {
			deltaVector.scl(delta * 0.1f);
			velocity.add(deltaVector);
			velocity.x = Math.min(Math.max(velocity.x, -0.045f), 0.05f);
			velocity.y = Math.min(Math.max(velocity.y, -0.02f), 0.021f);
			position.add(velocity);
		}
	}	

	public void setAlien(Alien alien) {
		this.alien = alien;
	}
	
	public void update(float delta) {
		
		switch(state){
		case PERCHED:
			updatePerched();
			break;
		case FLYING:
			elapsed += delta;
			updateFlying(delta);
			break;
		case RETURNING:
			elapsed += delta;
			updateReturning(delta);
			break;
		}
		
		if (velocity.x < 0)
			flyingLeft = 1;
		else 
			flyingLeft = -1;
	}
	
	public void render(SpriteBatch spriteBatch) {
		TextureRegion keyFrame = animation.getKeyFrame(elapsed);
		
		spriteBatch.draw(keyFrame, 
				position.x - 0.5f - (flyingLeft < 0 ? -scale : 0),
				position.y - 0.5f,
				0.5f,
				0.5f, 
				flyingLeft, 
				1, 
				scale, scale,
				0);		
	}
}
