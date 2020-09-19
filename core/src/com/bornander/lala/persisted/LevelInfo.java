package com.bornander.lala.persisted;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;
import com.bornander.lala.Assets;
import com.bornander.lala.LevelData;
import com.bornander.lala.Material;
import com.bornander.libgdx.Log;

import java.util.Date;
import java.util.StringTokenizer;

public class LevelInfo {
	public final ChapterInfo chapter;
	public final int id;
	public final Material type;
	
	public boolean completed;
	public boolean unlocked;
	
	public long completedAt;
	
	public String name; // TODO: Make me final!

	private String mapPath;
	private String data;
	
	public LevelInfo(ChapterInfo chapter, int id, boolean completed, boolean unlocked, long completedAt) {
		this.chapter = chapter;
		this.id = id;
		this.completed = completed;
		this.unlocked = unlocked;
		this.completedAt = completedAt;

		String dataPath = String.format("chapters/%s/%d.data", chapter.id, id);
		mapPath = String.format("chapters/%s/%d.tmx", chapter.id, id);
		// TODO: Should not try-catch here!
		try {
			data = Gdx.files.internal(dataPath).readString();
			name = getRow("name")[0];
		}
		catch(Throwable t) {
			data = "type:dirt";
			name = "__not found";
		}
		type = Material.valueOf(getRow("type")[0].toUpperCase());
		Log.debug("Loaded level '%s' of type '%s'", name, type);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LevelInfo) {
			LevelInfo other = (LevelInfo)obj;
			return other.id == id && other.chapter.equals(chapter);
		}
		else 
			return super.equals(obj);
	}	
	
	@Override
	public String toString() {
		return "LEVEL " + (chapter.index + 1) + "-" + (id + 1) + "\r\n" + Assets.instance.texts.get(String.format("name_%d_%d", chapter.index, id));
	}
	
	
	public int getDaysSinceCompleted() {
		Date a = new Date(completedAt);
		Date b = new Date(TimeUtils.millis());
        return (int) ((a.getTime() - b.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	private String[] getRow(String title) {
		StringTokenizer tokenizer = new StringTokenizer(data, "\r\n");
		while(tokenizer.hasMoreTokens()) {
			String row = tokenizer.nextToken();
			if (row.startsWith(String.format("%s:", title))) {
				StringTokenizer rowTokenizer = new StringTokenizer(row.substring(row.indexOf(":") + 1), ","); 
				String[] tokens = new String[rowTokenizer.countTokens()];
				int i = 0;
				while(rowTokenizer.hasMoreTokens()) {
					tokens[i++] = rowTokenizer.nextToken();
				}
				return tokens;
			}
		}
		return new String[0];
	}
	
	private int[] getIntegerRow(String title) {
		String[] rowData = getRow(title);
		int[] ints = new int[rowData.length];
		for(int i = 0; i <rowData.length; ++i) {
			ints[i] = Integer.parseInt(rowData[i]);
		}
		
		return ints;
	}
	
	public LevelData getData() {
		int[] unlocks_levels = getIntegerRow("unlocks_levels");
		boolean unlocks_next_chapter = Boolean.parseBoolean(getRow("unlocks_next_chapter")[0]);
		return new LevelData(chapter.id, mapPath, unlocks_levels, unlocks_next_chapter);
	}

	public void save() {
		Log.info("Saving level %d for chapter %s", id, chapter.id);
		Preferences preferences = getPreferences(chapter);
		setCompleted(preferences, this);
		setUnlocked(preferences, this);
		preferences.flush();
	}
	
	public static LevelInfo load(ChapterInfo chapter, int id) {
		Log.info("Loading level %d for chapter %s", id, chapter.id);
		Preferences preferences = getPreferences(chapter);
		return new LevelInfo(
				chapter, 
				id, 
				getCompleted(preferences, id),
				getUnlocked(preferences, id),
				getCompletedAt(preferences, id));
	}
	
	private static boolean getCompleted(Preferences preferences, int id) {
		return preferences.getBoolean(String.format("level.%d.completed", id), false);
	}

	private static void setCompleted(Preferences preferences, LevelInfo level) {
		preferences.putBoolean(String.format("level.%d.completed", level.id), level.completed);
	}

	private static boolean getUnlocked(Preferences preferences, int id) {
		return preferences.getBoolean(String.format("level.%d.unlocked", id), false);
	}
	
	private static long getCompletedAt(Preferences preferences, int id) {
		return preferences.getLong(String.format("level.%d.completedAt", id), 0);
	}	

	private static void setUnlocked(Preferences preferences, LevelInfo level) {
		preferences.putBoolean(String.format("level.%d.unlocked", level.id), level.unlocked);
	}
	
	private static Preferences getPreferences(ChapterInfo chapter) {
		return Gdx.app.getPreferences(String.format("com.bornander.lala.chapter.%s", chapter.id));
	}

	public void setLevelCompleted() {
		completed = true;
		completedAt = Math.min(TimeUtils.millis(), completedAt);
		
	}
}