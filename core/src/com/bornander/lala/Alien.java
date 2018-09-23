package com.bornander.lala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bornander.lala.assets.AlienAssets;
import com.bornander.lala.assets.SoundAssets;
import com.bornander.libgdx.Log;

public class Alien {
	
	public enum State {
		TRANSFORMING,
		WALKING,
		COLLIDED,
		FELL,
		BLOCKED,
		SAD,
		TELEPORT_OUT,
		TELEPORT_IN,
		CLIMBING_SHIP,
		IN_SHIP,
		RETURNED,
		SPEAKING
	}
	
	private final Vector2 startPosition; 
	private final AlienAssets assets;
	public final Transform transform = new Transform();
	
	public Body body;
	public boolean isInContact;
	private final Vector2 target;
	private float elapsed;
	private TextureRegion keyFrame;
	private float maxX = Float.MIN_VALUE;
	
	private State state = State.TRANSFORMING;
	
	private PooledEffect teleportOutEffect;
	private PooledEffect teleportInEffect;
	
	private PooledEffect walkDustEffectCurrent;
	private PooledEffect walkDustEffectPrevious;
	
	private Material contactMaterial;
	private SoundAssets.LoopingSound stepSound = SoundAssets.LoopingSound.NONE;
	private Animation<TextureRegion> speakAnimation = null;
	
	private float blockedDuration = 0;
	
	public Alien(LevelData levelData) {
		assets = Assets.instance.characters.getAlien(levelData.alienName);
		startPosition = new Vector2(levelData.position_start);
		transform.set(startPosition, 0.0f);
		this.target = levelData.position_target;
		keyFrame = assets.stand.getKeyFrame(0);
		
	}
	
	// TODO: Rename this method
	public Body getBody(World world) {
		if (body != null)
			throw new RuntimeException("Cannot take body for body that already exist!");
		BodyDef bodyDefinition = new BodyDef();
		bodyDefinition.type = BodyType.DynamicBody;
		
		CircleShape shape = new CircleShape();
		shape.setRadius(0.49f);
		
		FixtureDef fixtureDefinition = new FixtureDef();
		fixtureDefinition.shape = shape;
		fixtureDefinition.friction = 8f;
		fixtureDefinition.density = 1f;
		fixtureDefinition.restitution = 0.1f;

		this.body = world.createBody(bodyDefinition);	
		body.setUserData(this);
		body.setTransform(transform.position, transform.angle);

		body.createFixture(fixtureDefinition);

		shape.dispose();
		return body;
	}
	
	public void speak() {
		if (state == State.SPEAKING)
			return;
		
		setState(State.SPEAKING);
		Assets.instance.sounds.playRandomSpeak();
		Gdx.input.vibrate(100);
	}
	
	private void setState(State newState) {
		if (newState == state)
			return;
		
		Log.info("Alien state %s -> %s, lastFrame=%s", state, newState, keyFrame);
		state = newState;
		if (state != State.WALKING)
			stepSound.stop();
		elapsed = 0.0f;
	}
	
	private TextureRegion updateActive(float delta) {
		transform.set(body.getTransform().getPosition(), body.getAngle() * MathUtils.radiansToDegrees);
		float distanceToTarget = Vector2.dst(transform.position.x, transform.position.y, target.x, target.y);
		
		if (transform.position.x > target.x + 1.25f) {
			setState(State.SAD);
			return assets.stand.getKeyFrame(elapsed);
		}
		
		if (transform.position.y < -1) {
			setState(State.SAD);
			return assets.stand.getKeyFrame(elapsed);
			
		}
		
		if (distanceToTarget < 0.25f) {
			setState(State.CLIMBING_SHIP);
			return assets.stand.getKeyFrame(elapsed);
		}
		else {
			if (body.getWorldCenter().x <= maxX) { 
				blockedDuration += delta;
			}
			
			if (blockedDuration > 0.2f) {
				Assets.instance.sounds.playRandomBlocked();
				setState(State.BLOCKED);
			}
			
			if (isInContact) {
				if (walkDustEffectCurrent != null)
					walkDustEffectCurrent.setPosition(transform.position.x, transform.position.y - 0.5f);
				
				// TODO: FIX THIS! Sound should alter the pitch
				//if (stepSound != null && MathUtils.randomBoolean(0.1f)) {
				//	stepSound.setPitch(stepSoundId, MathUtils.random(0.8f, 1.2f));
				//}
					
				float targetSpeed = 0.85f;
				final float torqueFactor = 22;
				applyTorque(targetSpeed, torqueFactor);
				return assets.walk.getKeyFrame(elapsed);
			}
			else 
				return assets.fall.getKeyFrame(elapsed);
		}
	}

	private void applyTorque(float targetSpeed, final float torqueFactor) {
		Vector2 lv = body.getLinearVelocity();
		float speed = lv.len();
		Log.info("speed=%.3f, sx=%.3f", speed, lv.y);
		if (speed < targetSpeed) {
			float t = -torqueFactor * (1.0f - speed);
			body.applyTorque(t, true);
		}
		else {
			if (speed > targetSpeed) {
				float t = 1.2f * torqueFactor * Math.max(1.0f, speed - 1.0f);
				body.applyTorque(t, true);
			}
		}
	}

	private void lockTorque() {
		body.setAngularVelocity(0);
	}
	
	private TextureRegion updateBlocked() {
		transform.set(body.getTransform().getPosition(), body.getAngle() * MathUtils.radiansToDegrees);
		if (elapsed > 0.5f) 
			setState(State.SAD);
		
		return assets.stand.getKeyFrame(elapsed);
	}	
	
	private TextureRegion updateCollided() {
		if (elapsed > 0.5f) 
			setState(State.SAD);

		return assets.sad.getKeyFrame(elapsed);
	}

	private TextureRegion updateFell() {
		transform.set(body.getTransform().getPosition(), body.getAngle() * MathUtils.radiansToDegrees);

		if (elapsed > 0.5f) {
			setState(State.SAD);
			return assets.sad.getKeyFrame(elapsed);
		}
		else {
			return assets.hit_ground.getKeyFrame(elapsed);
		}
	}
	
	private TextureRegion updateSad(float delta) {
		transform.set(body.getTransform().getPosition(), body.getAngle() * MathUtils.radiansToDegrees);

		if (elapsed > 1.0) {
			Assets.instance.sounds.playTeleportOut();
			setState(State.TELEPORT_OUT);
		}
		
		return assets.sad.getKeyFrame(elapsed);
	}
	
	private TextureRegion updateTeleportOut(float delta) {
		if (elapsed >= 1f) {
			Assets.instance.sounds.playTeleportIn();
			setState(State.TELEPORT_IN);
		}
		
		if (teleportOutEffect == null) {
			 teleportOutEffect = Assets.instance.effects.teleport_out.obtain();
			 teleportOutEffect.setPosition(transform.position.x, transform.position.y - 0.5f);
			 teleportOutEffect.start();
		}
		return assets.sad.getKeyFrame(elapsed);
	}
	
	private TextureRegion updateTeleportIn(float delta) {
		if (teleportInEffect == null) {
			 teleportInEffect = Assets.instance.effects.teleport_in.obtain();
			 teleportInEffect.setPosition(startPosition.x, startPosition.y + 2.0f);
			 teleportInEffect.start();
		}
		
		if (elapsed > 2.0f) {
			setState(State.RETURNED);
		}
		
		return assets.sad.getKeyFrame(elapsed);
	}
	
	private TextureRegion updateClimbingShip(float delta) {
		transform.position.y += 0.25f * delta;
		
		if (transform.position.y > target.y + 0.25f) {
			setState(State.IN_SHIP);
		}
		
		return assets.climb.getKeyFrame(elapsed);
	}
	
	private TextureRegion updateSpeaking() {
		if (speakAnimation == null) {
			speakAnimation = assets.getRandomSpeak();
		}

		TextureRegion keyFrame = speakAnimation.getKeyFrame(elapsed);
		
		if (elapsed > 1.0f) {
			setState(State.TRANSFORMING);
			speakAnimation = null;
		}

		return keyFrame;
	}
	
	
	private static void safeUpdateEffect(PooledEffect effect, float delta) {
		if (effect != null)
			effect.update(delta);
	}

	boolean wasInContact = false;
	public State update(float delta) {

		switch(state) {
		case TRANSFORMING:
			keyFrame = assets.stand.getKeyFrame(0);
			break;
		case WALKING:
			keyFrame = updateActive(delta);
			break;
		case COLLIDED:
			lockTorque();
			keyFrame = updateCollided();
			break;
		case FELL:
			lockTorque();
			keyFrame = updateFell();
			break;
		case BLOCKED:
			lockTorque();
			keyFrame = updateBlocked();
			break;
		case SAD:
			lockTorque();
			
			keyFrame = updateSad(delta);			
			break;
		case TELEPORT_OUT:
			keyFrame = updateTeleportOut(delta);
			break;
		case TELEPORT_IN:
			keyFrame = updateTeleportIn(delta);
			break;
		case CLIMBING_SHIP:
			lockTorque();
			keyFrame = updateClimbingShip(delta);
			break;
		case IN_SHIP:
			keyFrame = null;
			break;
		case RETURNED:
			break;
		case SPEAKING:
			keyFrame = updateSpeaking();
			break;
		default:
			break;
		}
		

		
		safeUpdateEffect(teleportOutEffect, delta);
		safeUpdateEffect(teleportInEffect, delta);
		
		maxX = Math.max(body.getWorldCenter().x, maxX);
		elapsed += delta;
		if (!isInContact) {
			stepSound.stop();
		}
		wasInContact = isInContact;
 
		isInContact = false;

		
		safeUpdateEffect(walkDustEffectPrevious, delta);
		safeUpdateEffect(walkDustEffectCurrent, delta);
		
		return state;
	}	
	

	private Vector2 getRenderPosition() {
		switch(state) {
		case TELEPORT_IN:
			return startPosition;
		default:
			return transform.position;
		}
	}
	
	public void render(SpriteBatch spriteBatch) {
		if (keyFrame != null) {
			
			if (wasInContact) {
				if (state == State.WALKING) {
					if (walkDustEffectPrevious != null)
						walkDustEffectPrevious.draw(spriteBatch);
					
					if (walkDustEffectCurrent != null)
						walkDustEffectCurrent.draw(spriteBatch);
				}
			}
			
			switch(state) {
			case TELEPORT_OUT:
				spriteBatch.setColor(1.0f, 1.0f, 1.0f, Math.max(0.0f, 1 - elapsed));
				break;
			case TELEPORT_IN:
				spriteBatch.setColor(1.0f, 1.0f, 1.0f, Math.min(1.0f, Math.max(0.0f, elapsed-0.5f)));
				break;
			default:
				spriteBatch.setColor(Color.WHITE);
				break;
			}
	
			Vector2 position = getRenderPosition();
			spriteBatch.draw(keyFrame, 
					position.x - 0.5f,
					position.y - 0.5f,
					0.5f,
					0.5f, 
					1, 
					2, 
					1, 1,
					0*transform.angle);
			
			spriteBatch.setColor(Color.WHITE);
		}
		if (teleportOutEffect != null)
			teleportOutEffect.draw(spriteBatch);
		if (teleportInEffect != null)
			teleportInEffect.draw(spriteBatch);
		
	}

	// TODO: Clean up this impactAngle stuff, I don't think it's needed.
	public void badCollision(float impactAngle) {
		if (state == State.WALKING) {
			transform.set(body.getTransform().getPosition(), body.getAngle() * MathUtils.radiansToDegrees);
			//if (impactAngle < 22.5f) {
				setState(State.FELL);
				Assets.instance.sounds.playRandomHurt();
			//}
			//else {
				// TODO: What to do here?
			//}
		}
	}
	
	public Vector2 getTrackingTarget() {
		return getRenderPosition();
	}
	
	public void dispose() {
		if (teleportOutEffect != null)
			teleportOutEffect.dispose();
		
		if (teleportInEffect != null)
			teleportInEffect.dispose();

		
		stepSound.stop();
	}
	private Object contactObject = null;
	public boolean setContactObject(Object newContactObject) {
		boolean changed = newContactObject != contactObject;
		contactObject = newContactObject;
		return changed;
	}
	
	private int contactChangeCounter = 0;
	public void setContactMaterial(Material material) {
		if (state != State.WALKING)
			return;
		
		if (material != contactMaterial) {
			++contactChangeCounter;
			// TODO: Fix this counter thingie!
			if (contactChangeCounter < 32)
				return;
			
			contactChangeCounter = 0;
			
			Log.debug("Moving from %s to %s", contactMaterial, material);
			if (walkDustEffectPrevious != null) {
				switch (contactMaterial) {
				case GRASS: Assets.instance.effects.walk_dust_grass.free(walkDustEffectPrevious); break;
				case DIRT: Assets.instance.effects.walk_dust_sand.free(walkDustEffectPrevious); break;
				default: 
					break;
				}
			}

			stepSound.stop();
			
			walkDustEffectPrevious = walkDustEffectCurrent;
			if (walkDustEffectPrevious != null)
				walkDustEffectPrevious.allowCompletion();

			walkDustEffectCurrent = null;
			switch (material) {
			case GRASS: 
				walkDustEffectCurrent = Assets.instance.effects.walk_dust_grass.obtain();
				break;
			case SAND: 
				walkDustEffectCurrent = Assets.instance.effects.walk_dust_sand.obtain(); 
				break;
			case WOOD:
				break;
			case METAL:
				break;
			case STONE:
				break;
			case SNOW:
				break;
			default:
				break;
			}
			
			stepSound = Assets.instance.sounds.loopStep(material);

			contactMaterial = material;
		}
	}

	public Vector2 getPosition(Vector2 target) {
		return target.set(body.getWorldCenter().x, body.getWorldCenter().y);
	}

	public void startWalking() {
		setState(State.WALKING);
	}
}
