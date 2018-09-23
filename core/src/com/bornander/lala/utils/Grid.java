package com.bornander.lala.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bornander.lala.Block;

public class Grid {
	
	private final static int XSIZE = 28;
	private final static int YSIZE = 8;
	
	private Grid() {
	}
	
	public static void unsnapCenterGrid(Block block) {
		if (MathUtils.ceil(block.transform.position.x) == block.transform.position.x)
			block.transform.position.x += 0.01f;

		if (MathUtils.ceil(block.transform.position.y) == block.transform.position.y)
			block.transform.position.y += 0.01f;
	}

	public static Vector2 snapToGrid(Block block, Vector2 input, Vector2 target) {
		Vector2 vertex = block.definition.getTransformed(block.transform)[0];
		float x = vertex.x;
		float y = vertex.y;
		float iy = input.y;// Math.max(1, Math.min(YSIZE-1, input.y));
		float ix = input.x;// Math.max(1, Math.min(XSIZE-1, input.x));
		
		float lx = MathUtils.floor(x);
		float hx = MathUtils.ceil(x);

		float ly = MathUtils.floor(y);
		float hy = MathUtils.ceil(y);
		
		float dx = x - lx < hx - x ? lx - x : hx - x;
		float dy = y - ly < hy - y ? ly - y : hy - y;
		
		target.set(ix + dx, iy + dy);
		while(target.y > YSIZE)
			target.y--;
		while(target.y < 0)
			target.y++;

		while(target.x > XSIZE)
			target.x--;
		while(target.x < 0)
			target.x++;
		
		return target;
	}	
	
}
