package com.bornander.lala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.Random;

public class LevelData {
	
	private final String path;
	private final Block[] available;
	
	public final TiledMap map; 
	
	public final Vector2 position_start;
	public final Vector2 position_target;
	
	public final float width;
	public final float height;
	
	public final String alienName;
	
	private final Vector2 tileSize; 
	
	public final List<Terrain> terrain;
	
	
	public final float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
	public final float viewportWidth; 
	
	public final float maxSide;
	public final float oneOverMaxSize;
	
	public final ParticleEffect[] background_effects;
	public final ParticleEffect[] foreground_effects;
	
	public final ClickableEffect[] clickable_effects;
	
	public final Npc[] entities;
	
	public final String message;
	public final int[] unlocks_levels;
	public final boolean unlocks_next_chapter;
	public final String music_track;
	
	private final static Random RND = new Random();
	
	public LevelData(String alienName, String path, int[] unlocks_levels, boolean unlocks_next_chapter) {
		
		this.alienName = alienName;
		
		
		this.path = path;
		//this.available = available;
		this.unlocks_levels = unlocks_levels;
		this.unlocks_next_chapter = unlocks_next_chapter;
		// TODO: Make this asynchronous
		map = Assets.instance.levels.load(path);
		MapProperties properties = map.getProperties();

		music_track = properties.get("music", Assets.instance.music.default_track_name, String.class);
		this.available = Assets.instance.blocks.getBlocks(properties.get("blocks", "", String.class));
		
		this.message = Assets.instance.texts.get(properties.get("message", null, String.class));

		width = (float)properties.get("width", Integer.class);
		height = (float)properties.get("height", Integer.class);
		tileSize = new Vector2(
				(float)properties.get("tilewidth", Integer.class),
				(float)properties.get("tileheight", Integer.class));
		
		viewportWidth = height / aspectRatio;
		
		position_start = MapUtils.getRectanglePosition(map, "entities", "start", new Vector2());
		position_target = MapUtils.getRectanglePosition(map, "entities", "target", new Vector2());
		
		terrain = MapUtils.getPolygons(map, "terrain");
		background_effects = MapUtils.getEffects(map, "background_effects");
		foreground_effects = MapUtils.getEffects(map, "foreground_effects");
		entities = MapUtils.getNpcs(map, "npc");
		clickable_effects = MapUtils.getClickable(map, "entities");
				
		
		// TODO: Break out to method
		float max = 0;
		for(Block block : available) {
			max = Math.max(max, Math.max(block.definition.size.x, block.definition.size.y));
		}		
		maxSide = max;
		oneOverMaxSize = 1.0f / maxSide;

	}
	
	private static Block[] copy(Block[] source) {
		Block[] copy = new Block[source.length];
		for(int i = 0; i < source.length; ++i)
			copy[i] = new Block(source[i]);
		
		return copy;
	}
	
	public Block[] getAvailable() {
		return copy(available);
	}
	
	public int getAvailableCount() {
		return available.length;
	}

	public void trackPosition(OrthographicCamera camera, Vector2 position, float trackFactor) {
		float x = position.x;
		float cameraPosition = camera.position.x;
		float targetPosition = (x + position_target.x) / 2.0f;
		float delta = targetPosition - cameraPosition;
		cameraPosition += delta * trackFactor;
		camera.viewportHeight = height;
		camera.viewportWidth = viewportWidth;
		
		camera.position.x = cameraPosition;
		camera.position.y = height / 2.0f;
		snapCamera(camera);
	}

	public void snapCamera(OrthographicCamera camera) {
		float hw = camera.viewportWidth / 2.0f;
		float minX = hw;
		float maxX = width - hw;
		camera.position.x = Math.max(minX, Math.min(maxX, camera.position.x));
	}
}