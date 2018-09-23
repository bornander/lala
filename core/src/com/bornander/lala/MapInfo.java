package com.bornander.lala;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.bornander.libgdx.Log;

import java.util.ArrayList;
import java.util.List;

public class MapInfo {

	public final int[] background_images;
	public final int[] tiles_background = new int[2];
	public final int[] tiles_foreground = new int[1];
	
	public MapInfo(TiledMap map) {
		List<Integer> imageLayerIndices = new ArrayList<Integer>();
		for(int i = 0; i < map.getLayers().getCount(); ++i) {
			
			MapLayer layer = map.getLayers().get(i);
			Log.debug("Maplayer=%s", layer.getName());
			if (layer instanceof TiledMapImageLayer)
				imageLayerIndices.add(i);
			
			if (layer.getName().equals("tiles_background")) 
				tiles_background[0] = i;
			
			if (layer.getName().equals("tiles_terrain")) 
				tiles_background[1] = i;

			if (layer.getName().equals("tiles_foreground")) 
				tiles_foreground[0] = i;
		}
		
		background_images = toArray(imageLayerIndices);
	}
	
	private int[] toArray(List<Integer> list) {
		int[] array = new int[list.size()];
		for(int i = 0; i < list.size(); ++i) 
			array[i] = list.get(i);
		
		return array;
	}
}
