package com.bornander.lala;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class BlockRenderer {
	
	private final ShapeRenderer shapeRenderer = new ShapeRenderer();

	public BlockRenderer() {
	}
	
	public void render(SpriteBatch spriteBatch, Block block, Vector2 position, float scale) {
		Vector2 hs = block.definition.halfSize;
		spriteBatch.draw(
				block.transform.mirrored ? block.definition.mirroredTexture : block.definition.texture,
				position.x-hs.x,								// x
				position.y-hs.y,								// y
				hs.x,											// originX
				hs.y, 											// originY
				2.0f * hs.x, 									// width
				2.0f * hs.y,									// height 
				scale, scale,									// scale
				block.transform.angle 
				);
	}	
	
	public void render(SpriteBatch spriteBatch, Block block, Vector2 position) {
		Vector2 hs = block.definition.halfSize;
		spriteBatch.draw(
				block.transform.mirrored ? block.definition.mirroredTexture : block.definition.texture,
				position.x-hs.x,								// x
				position.y-hs.y,								// y
				hs.x,											// originX
				hs.y, 											// originY
				2.0f * hs.x, 									// width
				2.0f * hs.y,									// height 
				1, 1,											// scale
				block.transform.angle 
				);
	}
	
	public void render(SpriteBatch spriteBatch, Block block) {
		render(spriteBatch, block, block.transform.position);
	}
	
	public void renderShape(Block block, Color color, OrthographicCamera camera) {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(color);

		shapeRenderer.setProjectionMatrix(camera.combined);
		Vector2[] vertices = block.definition.getTransformed(block.transform, 1.0f);
		float[] v = new float[vertices.length * 2];
		for(int i = 0; i < vertices.length; ++i) {
			v[i * 2 + 0] = vertices[i].x;
			v[i * 2 + 1] = vertices[i].y;
		}
		shapeRenderer.polygon(v);		
		
		
		shapeRenderer.end();
	}
}
