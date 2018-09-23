package com.bornander.lala;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bornander.lala.assets.AlienAssets;
import com.bornander.lala.assets.ShipAssets;

public class DropOffShip {
	
	private enum State {
		FLYING_IN,
		JUMPING,
		LANDING 
	}

	private static final float FLY_DURATION = 4.0f;
	private static final float HALF_DURATION = FLY_DURATION / 2.0f;
	
	private final Vector2 unitSize = new Vector2();
	private final Vector2 halfUnitSize = new Vector2();
	
	private final Vector2 startPosition = new Vector2();
	private final Vector2 dropOffPosition = new Vector2();
	private final Vector2 endPosition = new Vector2();
	public final Vector2 alienTargetPosition = new Vector2();
	
	private final Vector2 position = new Vector2();
	private final Vector2 alienPosition = new Vector2();
	private float alienVerticalVelocity = 0;
	private ShipAssets shipAssets;
	public AlienAssets alienAssets;
	//private AlienAssets
	private TextureRegion shipImage;
	private Animation<TextureRegion> alienAnimation;
	private float elapsed;
	private float stateElapsed;
	private float flyInFactor = 2;
	
	private State state;
	
	// TODO: This should take a planet info? Or a alien id?
	public void initialize(float mapHeight, Vector2 dropOffPosition, Vector2 alienTargetPosition, ShipAssets shipAssets, AlienAssets alienAssets) {
		this.startPosition.set(dropOffPosition).add(-flyInFactor, mapHeight-1);
		this.dropOffPosition.set(dropOffPosition);
		this.endPosition.set(dropOffPosition).add(flyInFactor, mapHeight-1);
		this.shipAssets = shipAssets;
		this.alienAssets = alienAssets;
		
		this.alienTargetPosition.set(alienTargetPosition);
		this.alienPosition.set(dropOffPosition).add(0, 0.5f);
		alienVerticalVelocity = 2;
		alienAnimation = null;
		elapsed = 0;
		
		setState(State.FLYING_IN);
		shipImage = shipAssets.manned;
	}
	
	private void setState(State newState) {
		state = newState;
		stateElapsed = 0;
				
	}
	
	
	private void setTexture(TextureRegion texture) {
		float w = (float)texture.getRegionWidth();
		float h = (float)texture.getRegionHeight();
		
		unitSize.set(1, h/w);
		unitSize.scl(1.6f);
		halfUnitSize.set(unitSize).scl(0.5f);
	}	
	
	private void updateFlying() {
		float d = Math.min(1.5f * MathUtils.PI, (elapsed / FLY_DURATION) * MathUtils.PI);
		float x = flyInFactor + startPosition.x - (flyInFactor)*(float)Math.cos(d);
		float y = startPosition.y - (startPosition.y - dropOffPosition.y)*(float)Math.sin(d);
		position.set(x, y);
	}
	
	private void updateFlyingIn() {
		if (elapsed >= HALF_DURATION) {
			setState(State.JUMPING);
			shipImage = shipAssets.unmanned;
		}
		
	}
	
	private void updateJumping(float delta) {
		alienVerticalVelocity -= 4.0f * delta;
		if (alienPosition.y > alienTargetPosition.y) {
			alienPosition.add(0, alienVerticalVelocity * delta);
			alienAnimation = alienAssets.fall;
		}
		else {
			alienPosition.set(alienTargetPosition);
			alienAnimation = alienAssets.hit_ground;
			setState(State.LANDING);
		}
	}
	
	public boolean update(float delta) {
		elapsed += delta;
		stateElapsed += delta;
		
		updateFlying();
		
		switch(state) {
		case FLYING_IN:
			updateFlyingIn();
			break;
		case JUMPING:
			updateFlyingIn();
			updateJumping(delta);
			break;
		case LANDING:
			if (stateElapsed > 0.5f)
				return true;
			break;
		}


		
		setTexture(shipImage);
		return false;
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(shipImage, 
				position.x - halfUnitSize.x,
				position.y - 0.5f/* - halfUnitSize.y*/,
				halfUnitSize.x,
				halfUnitSize.y, 
				unitSize.x, 
				unitSize.y, 
				1, 1,
				0);
		
		
		if (alienAnimation != null) {
			batch.draw(alienAnimation.getKeyFrame(stateElapsed), 
					alienPosition.x - 0.5f,
					alienPosition.y - 0.5f,
					0.5f,
					0.5f, 
					1, 
					2, 
					1, 1,
					0);			
		}
	}
}
