package com.bornander.lala;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.bornander.libgdx.Temp;

public class AlienContactListener implements ContactListener {

	private Vector2 pp = new Vector2();
	private Vector2 cp = new Vector2();
	private static Vector2 sp = new Vector2();
	private static Vector2 tp = new Vector2();
	private boolean firstContact = true;
	
	public void reset() {
		firstContact = true;
	}

	
	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		final Object udA = contact.getFixtureA().getBody().getUserData();
		final Object udB = contact.getFixtureB().getBody().getUserData();
		Alien alien = null;
		Object object = null;
		if (udA instanceof Alien) {
			alien = (Alien) udA;
			object = udB;
		}
		if (udB instanceof Alien) {
			alien = (Alien) udB;
			object = udA;
		}

		if (alien != null) {
			alien.isInContact = true;
			if (object instanceof Material)
				alien.setContactMaterial((Material) object);
			if (object instanceof Terrain)
				alien.setContactMaterial(((Terrain) object).material);
			if (object instanceof Block)
				alien.setContactMaterial(((Block) object).definition.material);

			float force = 0;
			final int count = impulse.getCount();
			for (int i = 0; i < count; ++i) {
				force = Math.max(force, impulse.getNormalImpulses()[i]);
			}
			final WorldManifold manifold = contact.getWorldManifold();
			final int noc = manifold.getNumberOfContactPoints();
			boolean isFall = false;
			for(int i = 0; i < noc; ++i) {
				cp.set(manifold.getPoints()[i]);
				if (firstContact) {
					firstContact = false;
					alien.getPosition(sp);
				}
				else {
					float yd = Math.abs(pp.y - cp.y);
					float xd = Math.abs(pp.x - cp.x);
					
					float sd = tp.set(cp).sub(sp).len();	
					
					
					if (yd > 0.1f && xd > 0.45f && sd > 0.75f) {
						isFall=true;
					}
					
					if (yd >= 0.5) {
						isFall = true;
					}
				}
				pp.set(cp);
			}

			if (isFall) {

				if (noc > 0) {

					Vector2 collisionPoint = Temp.vector2.obtain().set(manifold.getPoints()[0]);
					Vector2 impactVector = Temp.vector2.obtain().set(collisionPoint).sub(alien.body.getWorldCenter()).nor();

					Vector2 down = new Vector2(0, -1);

					float angle = Math.abs(impactVector.angle(down));

					alien.badCollision(angle);

					Temp.vector2.free(impactVector);
					Temp.vector2.free(collisionPoint);
				}
			}
		}
	}

}
