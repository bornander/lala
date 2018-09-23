package com.bornander.lala;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bornander.libgdx.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MapUtils {
	
	private final static Vector2 TILE_SIZE = new Vector2();
	
	private MapUtils() {
	}
	
	private static MapObject getObject(TiledMap map, String layerName, String objectName) {
		return map.getLayers().get(layerName).getObjects().get(objectName);
	}
	
	private static String getProperty(MapObject object, String propertyName, String defaultValue) {
		return object.getProperties().get(propertyName, defaultValue, String.class);
	}
	
	public static Vector2 getRectanglePosition(TiledMap map, String layerName, String objectName, Vector2 target) {
		MapProperties properties = map.getProperties();
		TILE_SIZE.set(
				(float)properties.get("tilewidth", Integer.class),
				(float)properties.get("tileheight", Integer.class));

		RectangleMapObject rectangleMapObject = (RectangleMapObject)getObject(map, layerName, objectName);
		
		rectangleMapObject.getRectangle().getCenter(target).scl(1.0f / TILE_SIZE.x, 1.0f / TILE_SIZE.y);
		return target;
	}
	
	public static List<Terrain> getPolygons(TiledMap map, String layerName) {
		MapProperties properties = map.getProperties();
		TILE_SIZE.set(
				(float)properties.get("tilewidth", Integer.class),
				(float)properties.get("tileheight", Integer.class));
		
		
		List<Terrain> terrain = new ArrayList<Terrain>();
		MapLayer layer = map.getLayers().get(layerName);
		Array<PolygonMapObject> polygonObjects = layer.getObjects().getByType(PolygonMapObject.class);
		for(PolygonMapObject polygonObject : polygonObjects) {
			String type = getProperty(polygonObject, "type", "");
			Polygon polygon = polygonObject.getPolygon(); 
			
			float[] vertices = polygon.getTransformedVertices();
			Vector2[] vector = new Vector2[vertices.length / 2];
			for(int i = 0, j = 0; i < vertices.length - 1; i += 2, ++j) {
				Vector2 vertex = new Vector2(
						vertices[i + 0] / TILE_SIZE.x,
						vertices[i + 1] / TILE_SIZE.y);
				
				vector[j] = vertex;
			}
			terrain.add(new Terrain(Material.valueOf(type), vector));
		}
		return terrain;
	}
	
	public static ParticleEffect[] getEffects(TiledMap map, String layerName) {
		TILE_SIZE.set(
				(float)map.getProperties().get("tilewidth", Integer.class),
				(float)map.getProperties().get("tileheight", Integer.class));		
		List<ParticleEffect> effects = new ArrayList<ParticleEffect>();
		
		MapLayer layer = map.getLayers().get(layerName);
		Array<RectangleMapObject> rectangleObjects = layer.getObjects().getByType(RectangleMapObject.class);
		for(RectangleMapObject rectangleObject : rectangleObjects) {
			Rectangle rectangle = rectangleObject.getRectangle();
			String type = getProperty(rectangleObject, "type", "");
			if (type.startsWith("effect_")) {
				float x = rectangle.x / TILE_SIZE.x;
				float y = rectangle.y / TILE_SIZE.y;
				String effectName = type.substring(type.indexOf("_") + 1);
				Log.info("Loading effect %s", effectName);
				ParticleEffect effect = Assets.instance.effects.obtain(effectName);

				effect.setPosition(x, y);
				for(ParticleEmitter emitter : effect.getEmitters()) {
					emitter.getXOffsetValue().setLow(0, rectangle.width / TILE_SIZE.x);
					emitter.getYOffsetValue().setLow(0, rectangle.height / TILE_SIZE.y);
					/*
					emitter.getEmission().setLow(
							getProperty(rectangleObject, "emission_low", 1), 
							getProperty(rectangleObject, "emission_high", 1));
					*/
				}
				effects.add(effect);
				
			}
			
		}
		
		return effects.toArray(new ParticleEffect[effects.size()]);
	}	


	public static Npc[] getNpcs(TiledMap map, String layerName) {
		List<Npc> entities = new ArrayList<Npc>();
		MapLayer layer = map.getLayers().get(layerName);
		Array<RectangleMapObject> rectangleObjects = layer.getObjects().getByType(RectangleMapObject.class);
		for(RectangleMapObject rectangleObject : rectangleObjects) {
			Rectangle rectangle = rectangleObject.getRectangle();
			String type = getProperty(rectangleObject, "type", "");
			if (type.equals("flier")) {
				Rectangle scaled = new Rectangle(
						rectangle.x / TILE_SIZE.x,
						rectangle.y / TILE_SIZE.y,
						rectangle.width / TILE_SIZE.x,
						rectangle.height / TILE_SIZE.y);

				entities.add(new Flier(scaled, getProperty(rectangleObject, "skin", "fly")));
			}
		}
		return entities.toArray(new Npc[entities.size()]);
	}
	
	public static ClickableEffect[] getClickable(TiledMap map, String layerName) {
		List<ClickableEffect> effects = new ArrayList<ClickableEffect>();
		MapLayer layer = map.getLayers().get(layerName);
		Array<RectangleMapObject> rectangleObjects = layer.getObjects().getByType(RectangleMapObject.class);
		for(RectangleMapObject rectangleObject : rectangleObjects) {
			if (rectangleObject.getProperties().get("type", "", String.class).equals("clickable")) {
				effects.add(new ClickableEffect(rectangleObject, TILE_SIZE));
			}
		}
		
		return effects.toArray(new ClickableEffect[effects.size()]);
	}
	
	public static List<String> getList(MapProperties properties, String propertyName) {
		List<String> list = new ArrayList<String>();
		String raw = properties.get(propertyName, "", String.class);
		StringTokenizer tokenizer = new StringTokenizer(raw, ",");
		while(tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}
		return list;
	}
	
	public static Vector2 getRectanglePosition(TiledMap map, String layerName, String objectType, String objectName, Vector2 target) {
		TILE_SIZE.set(
				(float)map.getProperties().get("tilewidth", Integer.class),
				(float)map.getProperties().get("tileheight", Integer.class));		
		
		
		MapLayer layer = map.getLayers().get(layerName);
		
		for(MapObject mapObject : layer.getObjects()) {
			if (mapObject.getProperties().get("type", String.class).equals(objectType) && mapObject instanceof RectangleMapObject) {
				RectangleMapObject rectangleMapObject = (RectangleMapObject)mapObject;
				if (mapObject.getName().equals(objectName)) {
					rectangleMapObject.getRectangle().getCenter(target).scl(1.0f / TILE_SIZE.x, 1.0f / TILE_SIZE.y);
					return target;
				}
			}
		}
		
		return null;
	}
}