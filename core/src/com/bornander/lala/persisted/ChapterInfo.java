package com.bornander.lala.persisted;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ChapterInfo {
	private final static String[] CHAPTER_IDS = new String[] {
			"blue",
			"beige",
			"green",
			"pink",
			"yellow"
	};
	
	public final int index;
	public final String id;
	public boolean completed;
	public boolean unlocked;
	public final LevelInfo[] levels;
	public ChapterInfo next;
	
	private ChapterInfo(int index, String id, boolean completed, boolean unlocked) {
		this.index = index;
		this.id = id;
		this.completed = completed;
		this.unlocked = unlocked;
		this.levels = new LevelInfo[12];
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ChapterInfo) {
			ChapterInfo other = (ChapterInfo)obj;
			return other.id.equals(id);
		}
		else 
			return super.equals(obj);
	}
	
	public boolean hasNext() {
		return next != null;
	}
	
	private static boolean getCompleted(Preferences preferences, String id) {
		return preferences.getBoolean(String.format("chapter.%s.completed", id), false);
	}

	private static void setCompleted(Preferences preferences, ChapterInfo chapter) {
		preferences.putBoolean(String.format("chapter.%s.completed", chapter.id), chapter.completed);
	}

	private static boolean getUnlocked(Preferences preferences, String id) {
		return preferences.getBoolean(String.format("chapter.%s.unlocked", id), false);
	}

	private static void setUnlocked(Preferences preferences, ChapterInfo chapter) {
		preferences.putBoolean(String.format("chapter.%s.unlocked", chapter.id), chapter.unlocked);
	}
	
	private static Preferences getPreferences() {
		return Gdx.app.getPreferences("com.bornander.lala.chapters");
	}
	
	private static ChapterInfo load(int index, String id) {
		Preferences preferences = getPreferences();
		return new ChapterInfo(index,
				id, 
				getCompleted(preferences, id),
				getUnlocked(preferences, id));
	}
	
	public void save() {
		Preferences preferences = getPreferences();
		setCompleted(preferences, this);
		setUnlocked(preferences, this);
		preferences.flush();
	}
	
	public static ChapterInfo[] load() {
		ChapterInfo[] chapters = new ChapterInfo[CHAPTER_IDS.length];
		for(int i = 0; i < CHAPTER_IDS.length; ++i) {
			ChapterInfo chapter = load(i, CHAPTER_IDS[i]);
			chapter.unlocked = chapter.unlocked || i == 0;
			for(int j = 0; j < chapter.levels.length; ++j) {
				LevelInfo level = LevelInfo.load(chapter, j);
				level.unlocked = level.unlocked || (i == 0 && j == 0);
			
				chapter.levels[j] = level; 
			}
			chapters[i] = chapter;
			if (i > 0)
				chapters[i - 1].next = chapter;
		}
		return chapters;
	}
}
