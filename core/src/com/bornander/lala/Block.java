package com.bornander.lala;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bornander.libgdx.SAT;

public class Block {
	/*
	 * stone_1x2_triangle_left,
	 * wood_pyramid,
	 * wood_square_small_triangle_left,
	 * wood_square,
	 * wood_small_triangle_left,
	 * wood_pyramid,
	 * wood_square_small_triangle_left,
	 * wood_1x2,
	 * metal_square,
	 * metal_small_triangle_left,
	 * metal_pyramid,
	 * stone_square_small_triangle_left,stone_1x2
	 * metal_1x2
	 */
	
	public final int id;
	
	public final BlockDef definition;
	public final Transform transform = new Transform();
	private Body body;
	
	public Block(int id, BlockDef definition) {
		this.id = id;
		this.definition = definition;
	}
	
	@Override
	public String toString() {
		return String.format("Block %d, transform=%s", id, transform);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Block) {
			Block other = (Block)obj;
			return other.id == id; 
		}
		else 
			return false;
	}
	
	public Block(Block source) {
		this(source.id, new BlockDef(source.definition));
		transform.set(source.transform);
	}

	public Body getBody(World world) {
		
		BodyDef bodyDefinition = new BodyDef();
		bodyDefinition.type = BodyType.DynamicBody;
		
		PolygonShape shape = new PolygonShape();
		shape.set(definition.getTransformed(Vector2.Zero, 0, transform.mirrored, 0.96f));
		FixtureDef fixtureDefinition = new FixtureDef();
		fixtureDefinition.shape = shape;
		fixtureDefinition.friction = 0.25f;
		switch(definition.material) {
			case WOOD: fixtureDefinition.density = 0.5f; break;
			case METAL: fixtureDefinition.density = 1.0f; break;
			case STONE: fixtureDefinition.density = 5.0f; break;
			default: fixtureDefinition.density = 1.0f; 
		}
		fixtureDefinition.restitution = 0.1f;

		body = world.createBody(bodyDefinition);	
		body.setUserData(this);
		body.setTransform(transform.position, transform.angle * MathUtils.degreesToRadians);

		/*Fixture nodeFixture =*/ body.createFixture(fixtureDefinition);
		
		shape.dispose();
		return body;
	}
	
	public boolean collides(Block other) {
		Vector2[] a = definition.getTransformed(transform, 1);
		Vector2[] b = other.definition.getTransformed(other.transform, 1);
		return SAT.check(a, b);
	}

	public boolean collides(Vector2[] polygon) {
		return SAT.check(definition.getTransformed(transform, 0.9f), polygon);
	}

	public boolean isBelow(float limit) {
		float min = Float.MAX_VALUE;
		for(Vector2 vertex : definition.getTransformed(transform)) {
			min = Math.min(min, vertex.y);
		}
		return min <= limit;
	}

	public void mirror() {
		transform.mirror();
	}

	public boolean contains(Vector2 point) {
		Polygon p = definition.getTransformedPolygon(transform);
		return p.contains(point.x, point.y);
		
		// TODO Auto-generated method stub
		
	}
}