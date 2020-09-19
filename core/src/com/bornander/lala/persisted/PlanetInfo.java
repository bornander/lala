package com.bornander.lala.persisted;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanetInfo {
	private final List<String> pending;
	private final List<String> landed;
	
	public PlanetInfo(List<String> pending, List<String> landed) {
		this.pending = new ArrayList<String>(pending);
		this.landed = new ArrayList<String>(landed);
	}

	public void setAsPending(LevelInfo level) {
		String id = String.format("%s.%d", level.chapter.id, level.id);
		if (!landed.contains(id)) {
			landed.addAll(pending);
			pending.clear();
			pending.add(id); 
		}
	}
	
//	public void setAsLanded(String id) {
//		pending.remove(id);
//		landed.add(id);
//	}
	
	public boolean anyPending() {
		return !pending.isEmpty();
	}
	
	public String getNextToLand() {
		String next = pending.get(0);
		pending.remove(0);
		landed.add(next);
		return next;
	}
	
	public void save() {
		Preferences preferences = getPreferences();
		
		for(String s : pending) {
			preferences.putBoolean(String.format("pending.%s", s), true);
		}
		
		for(String s : landed) {
			preferences.putBoolean(String.format("landed.%s", s), true);
			preferences.putBoolean(String.format("pending.%s", s), false);
		}
		
		preferences.flush();
	}
	
	public static String getPrefix(String id) {
		return id.substring(0, id.indexOf('.'));
	}
	
	public static PlanetInfo load() {
		List<String> pending = new ArrayList<String>();
		List<String> landed = new ArrayList<String>();
		Preferences preferences = getPreferences();
		
		Map<String, ?> map = preferences.get();
		for(String key : map.keySet()) {
			if (key.startsWith("pending.") && preferences.getBoolean(key))
				pending.add(stripPrefix(key, "pending."));
			if (key.startsWith("landed.") && preferences.getBoolean(key)) 
				landed.add(stripPrefix(key, "landed."));
		}
		return new PlanetInfo(pending, landed);
	}
	
	private static String stripPrefix(String string, String prefix) {
		return string.substring(prefix.length());
	}
	
	
	private static Preferences getPreferences() {
		return Gdx.app.getPreferences("com.bornander.lala.homeworld");
	}
	
	public int getSavedCount() {
		return landed.size() + pending.size();
	}

	public Iterable<String> getLanded() {
		return landed;
	}
}