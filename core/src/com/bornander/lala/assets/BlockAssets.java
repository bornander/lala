package com.bornander.lala.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.bornander.lala.Block;
import com.bornander.lala.BlockDef;
import com.bornander.lala.Material;

import java.util.HashMap;
import java.util.Map;

public class BlockAssets {
	
	private static final Vector2[] shape_square = new Vector2[] {
			new Vector2(-0.5f, -0.5f),	
			new Vector2( 0.5f, -0.5f),	
			new Vector2( 0.5f,  0.5f),	
			new Vector2(-0.5f,  0.5f)	
		};
	
	private static final Vector2[] shape_small_triangle_left = new Vector2[] {
			new Vector2(-0.5f, -0.5f),	
			new Vector2( 0.5f, -0.5f),	
			new Vector2(-0.5f,  0.5f)	
		};
	
	private static final Vector2[] shape_small_triangle_right = new Vector2[] {
			new Vector2(-0.5f, -0.5f),	
			new Vector2( 0.5f, -0.5f),	
			new Vector2( 0.5f,  0.5f)	
		};
	
	
	private static final Vector2[] shape_pyramid =	new Vector2[] {
			new Vector2(-1.0f, -0.5f),	
			new Vector2( 1.0f, -0.5f),	
			new Vector2( 0.0f,  0.5f)
		};
	
	private static final Vector2[] shape_square_small_triangle_left = new Vector2[] {
			new Vector2(-0.5f, -1.0f),	
			new Vector2( 0.5f, -1.0f),	
			new Vector2( 0.5f,  0.0f),	
			new Vector2(-0.5f,  1.0f)	
		};
	
	private static final Vector2[] shape_1x2 = new Vector2[] {
			new Vector2(-0.5f, -1.0f),	
			new Vector2( 0.5f, -1.0f),	
			new Vector2( 0.5f,  1.0f),	
			new Vector2(-0.5f,  1.0f)	
		};

	private static final Vector2[] shape_2x1 = new Vector2[] {
			new Vector2(-1.0f, -0.5f),	
			new Vector2(-1.0f,  0.5f),	
			new Vector2( 1.0f,  0.5f),	
			new Vector2( 1.0f, -0.5f)	
		};

	private static final Vector2[] shape_1x2_triangle_left = new Vector2[] {
			new Vector2(-0.5f, -1.5f),	
			new Vector2( 0.5f, -1.5f),	
			new Vector2( 0.5f,  0.5f),	
			new Vector2(-0.5f,  1.5f)	
		};

	public final BlockDef wood_square;
	public final BlockDef wood_small_triangle_left;
	public final BlockDef wood_small_triangle_right;
	public final BlockDef wood_pyramid;
	public final BlockDef wood_square_small_triangle_left;
	public final BlockDef wood_1x2;
	public final BlockDef wood_2x1;
	public final BlockDef wood_1x2_triangle_left;

	public final BlockDef metal_square;
	public final BlockDef metal_small_triangle_left;
	public final BlockDef metal_small_triangle_right;
	public final BlockDef metal_pyramid;
	public final BlockDef metal_square_small_triangle_left;
	public final BlockDef metal_1x2;
	public final BlockDef metal_1x2_triangle_left;
	
	public final BlockDef stone_square;
	public final BlockDef stone_small_triangle_left;
	public final BlockDef stone_small_triangle_right;
	public final BlockDef stone_pyramid;
	public final BlockDef stone_square_small_triangle_left;
	public final BlockDef stone_1x2;
	public final BlockDef stone_1x2_triangle_left;

	
	private final Map<String, BlockDef> definitions = new HashMap<String, BlockDef>(); 
	
	public BlockAssets(TextureAtlas atlas) {
		wood_square = new BlockDef(shape_square, atlas.findRegion("wood_square"), Material.WOOD);	
		wood_small_triangle_left = new BlockDef(shape_small_triangle_left, atlas.findRegion("wood_small_triangle_left"), Material.WOOD);	
		wood_small_triangle_right = new BlockDef(shape_small_triangle_right, atlas.findRegion("wood_small_triangle_right"), Material.WOOD);
		wood_pyramid = new BlockDef(shape_pyramid, atlas.findRegion("wood_pyramid"), Material.WOOD);
		wood_square_small_triangle_left = new BlockDef(shape_square_small_triangle_left, atlas.findRegion("wood_square_small_triangle_left"), Material.WOOD);
		wood_1x2 = new BlockDef(shape_1x2, atlas.findRegion("wood_1x2"), Material.WOOD);
		wood_2x1 = new BlockDef(shape_2x1, atlas.findRegion("wood_2x1"), Material.WOOD);
		wood_1x2_triangle_left = new BlockDef(shape_1x2_triangle_left, atlas.findRegion("wood_1x2_triangle_left"), Material.WOOD);
		
		metal_square = new BlockDef(shape_square, atlas.findRegion("metal_square"), Material.METAL);	
		metal_small_triangle_left = new BlockDef(shape_small_triangle_left, atlas.findRegion("metal_small_triangle_left"), Material.METAL);	
		metal_small_triangle_right = new BlockDef(shape_small_triangle_right, atlas.findRegion("metal_small_triangle_right"), Material.METAL);
		metal_pyramid = new BlockDef(shape_pyramid, atlas.findRegion("metal_pyramid"), Material.METAL);
		metal_square_small_triangle_left = new BlockDef(shape_square_small_triangle_left, atlas.findRegion("metal_square_small_triangle_left"), Material.METAL);
		metal_1x2 = new BlockDef(shape_1x2, atlas.findRegion("metal_1x2"), Material.METAL);
		metal_1x2_triangle_left = new BlockDef(shape_1x2_triangle_left, atlas.findRegion("metal_1x2_triangle_left"), Material.METAL);

		stone_square = new BlockDef(shape_square, atlas.findRegion("stone_square"), Material.STONE);	
		stone_small_triangle_left = new BlockDef(shape_small_triangle_left, atlas.findRegion("stone_small_triangle_left"), Material.STONE);	
		stone_small_triangle_right = new BlockDef(shape_small_triangle_right, atlas.findRegion("stone_small_triangle_right"), Material.STONE);
		stone_pyramid = new BlockDef(shape_pyramid, atlas.findRegion("stone_pyramid"), Material.STONE);
		stone_square_small_triangle_left = new BlockDef(shape_square_small_triangle_left, atlas.findRegion("stone_square_small_triangle_left"), Material.STONE);
		stone_1x2 = new BlockDef(shape_1x2, atlas.findRegion("stone_1x2"), Material.STONE);
		stone_1x2_triangle_left = new BlockDef(shape_1x2_triangle_left, atlas.findRegion("stone_1x2_triangle_left"), Material.STONE);
		
		
		definitions.put("wood_square", wood_square);
		definitions.put("wood_small_triangle_left", wood_small_triangle_left);
		definitions.put("wood_small_triangle_right", wood_small_triangle_right);
		definitions.put("wood_pyramid", wood_pyramid);
		definitions.put("wood_square_small_triangle_left", wood_square_small_triangle_left);
		definitions.put("wood_1x2", wood_1x2);
		definitions.put("wood_2x1", wood_2x1);
		definitions.put("wood_1x2_triangle_left", wood_1x2_triangle_left);
		
		definitions.put("metal_square", metal_square);
		definitions.put("metal_small_triangle_left", metal_small_triangle_left);
		definitions.put("metal_small_triangle_right", metal_small_triangle_right);
		definitions.put("metal_pyramid", metal_pyramid);
		definitions.put("metal_square_small_triangle_left", metal_square_small_triangle_left);
		definitions.put("metal_1x2", metal_1x2);
		definitions.put("metal_1x2_triangle_left", metal_1x2_triangle_left);

		definitions.put("stone_square", stone_square);
		definitions.put("stone_small_triangle_left", stone_small_triangle_left);
		definitions.put("stone_small_triangle_right", stone_small_triangle_right);
		definitions.put("stone_pyramid", stone_pyramid);
		definitions.put("stone_square_small_triangle_left", stone_square_small_triangle_left);
		definitions.put("stone_1x2", stone_1x2);
		definitions.put("stone_1x2_triangle_left", stone_1x2_triangle_left);
	}
	
	public BlockDef get(String name) {
		return definitions.get(name);
	}

	public Block[] getBlocks(String blockString) {
		if (blockString.isEmpty())
			return new Block[0];
		String[] names = blockString.split(",");
		Block[] blocks = new Block[names.length];
		for(int i = 0; i < blocks.length; ++i)
			blocks[i] = new Block(i, get(names[i]));
		
		return blocks;
	}
}
