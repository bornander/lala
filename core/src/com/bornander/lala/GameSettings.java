package com.bornander.lala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameSettings {

	public static boolean soundEnabled;
	public static float soundVolume;
	public static boolean musicEnabled;
	public static float musicVolume;
	
	private static Preferences getPreferences() {
		return Gdx.app.getPreferences("com.bornander.lala.settings");
	}
	
	public static void load() {
		Preferences settings = getPreferences();
		soundEnabled = settings.getBoolean("soundEnabled", true);
		soundVolume = settings.getFloat("soundVolume", 1.0f);
		musicEnabled = settings.getBoolean("musicEnabled", true);
		musicVolume = settings.getFloat("musicVolume", 0.75f);
	}
	
	public static void save() {
		Preferences settings = getPreferences();
		settings.putBoolean("soundEnabled", soundEnabled);
		settings.putFloat("soundVolume", soundVolume);
		settings.putBoolean("musicEnabled", musicEnabled);
		settings.putFloat("musicVolume", musicVolume);
		settings.flush();
	}
}
