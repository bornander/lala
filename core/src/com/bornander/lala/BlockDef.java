package com.bornander.lala;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class BlockDef {
	
	private final float[] fVertices;
	private final Polygon polygon = new Polygon();
	private final Vector2[] vertices;
	private final Vector2[] transformed;
	
	public final TextureRegion texture;
	public final TextureRegion mirroredTexture;
	public final Vector2 size = new Vector2();
	public final Vector2 halfSize = new Vector2();
	public final Material material;
	

	public BlockDef(Vector2[] vertices, TextureRegion texture, Material material) {
		this.vertices = vertices;
		this.texture = texture;
		this.material = material;
		
		mirroredTexture = new TextureRegion(texture);
		mirroredTexture.flip(true, false);
		
		transformed = new Vector2[vertices.length];
		float minX = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = Float.MIN_VALUE;
		fVertices = new float[vertices.length * 2];
		for(int i = 0; i < vertices.length; ++i) {
			Vector2 v = vertices[i];
			minX = Math.min(minX, v.x);
			maxX = Math.max(maxX, v.x);
			minY = Math.min(minY, v.y);
			maxY = Math.max(maxY, v.y);
			transformed[i] = new Vector2(v);
		}
		size.set(maxX - minX, maxY - minY);
		halfSize.set(size).scl(0.5f);
	}
	
	public BlockDef(BlockDef source) {
		this(source.vertices, source.texture, source.material);
	}
	
	public int getVertexCount() {
		return vertices.length;
	}
	
	public Vector2[] getTransformed(Vector2 offset, float angle, boolean mirrored, float scale) {
		for(int i = 0; i < vertices.length; ++i) {
			if (!mirrored)
				transformed[i].set(vertices[i]);
			else
				transformed[i].set(-vertices[i].x, vertices[i].y);
			
			transformed[i].scl(scale);
			transformed[i].rotate(angle);
			transformed[i].add(offset);
			
		}
		return transformed;
	}
	
	public Vector2[] getTransformed(Transform transform) {
		return getTransformed(transform.position, transform.angle, transform.mirrored, 1.0f);
	}
	
	public Vector2[] getTransformed(Transform transform, float scale) {
		return getTransformed(transform.position, transform.angle, transform.mirrored, scale);
	}
	
	public Polygon getTransformedPolygon(Transform transform) {
		int i = 0;
		for(Vector2 v : getTransformed(transform)) {
			fVertices[i++] = v.x;
			fVertices[i++] = v.y;
		}
		
		polygon.setVertices(fVertices);
		return polygon;
	}
}