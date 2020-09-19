package com.bornander.lala.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.bornander.lala.GameSettings;
import com.bornander.libgdx.Log;

import java.util.HashMap;
import java.util.Map;

public class MusicAssets implements Disposable {

	private enum State {
		STOPPED, FADE_IN, PLAYING, FADE_OUT
	}

	public String default_track_name = "happy_level";

	public final Music eight_bit_metal;
	public final Music frantic_level;
	public final Music happy_level;
	public final Music main_theme;
	public final Music subdued_theme;
	public final Music swinging_level;

	private final Music[] allMusic;
	private final Map<String, Music> musicMap = new HashMap<String, Music>();

	private static final float FADE_DURATION = 1.0f;
	private State state = State.STOPPED;
	private float elapsed;
	private Music current;
	private static String queued;

	public MusicAssets() {
		allMusic = new Music[] { 
				eight_bit_metal = load("8BitMetal.mp3"), 
				frantic_level = load("FranticLevel.mp3"),
				happy_level = load("HappyLevel.mp3"), 
				main_theme = load("MainTheme.mp3"),
				subdued_theme = load("SubduedTheme.mp3"), 
				swinging_level = load("SwingingLevel.mp3") 
		};

		musicMap.put("eight_bit_metal", eight_bit_metal);
		musicMap.put("frantic_level", frantic_level);
		musicMap.put("happy_level", happy_level);
		musicMap.put("main_theme", main_theme);
		musicMap.put("subdued_theme", subdued_theme);
		musicMap.put("swinging_level", swinging_level);
	}

	private static Music load(String filename) {
		return Gdx.audio.newMusic(Gdx.files.internal(String.format("music/%s", filename)));
	}

	public void update(float delta) {
		elapsed += delta;
		float progress = Math.min(elapsed / FADE_DURATION, 1.0f);
		switch (state) {
		case FADE_IN:
			current.setVolume(MathUtils.lerp(0, GameSettings.musicVolume * 0.25f, progress));
			if (progress >= 1.0f)
				state = State.PLAYING;
			break;
		case PLAYING:
			break;
		case FADE_OUT:
			current.setVolume(MathUtils.lerp(0.25f * GameSettings.musicVolume, 0.0f, progress));
			if (progress >= 1.0f) {
				state = State.STOPPED;
				current.stop();
				current = null;
				if (queued != null) {
					play(queued);
				}
			}
			break;
		case STOPPED:
			break;
		}
	}

	public void play(String trackName) {
		Log.info("MusicAssets.play(%s)", trackName);
		if (current == null || !current.isPlaying()) {
			elapsed = 0;
			state = State.FADE_IN;
			current = musicMap.get(trackName);
			if (GameSettings.musicEnabled) {
				current.setVolume(0);
				current.setLooping(true);
				current.play();
			}
			queued = null;
		} else {
			queued = trackName;
			elapsed = 0;
			state = State.FADE_OUT;
		}

	}

	public void resume() {
		if (queued != null) {
			Log.info("MusicAssets.resuming(%s)", queued);
			play(queued);
		}
	}

	public void stopAll() {
		Log.info("MusicAssets.stopAll()");
		if (queued == null && current != null && current.isPlaying()) {
			for (String track : musicMap.keySet()) {
				if (musicMap.get(track).equals(current)) {
					queued = track;
				}
			}
		}

		for (Music music : allMusic)
			music.stop();
	}

	@Override
	public void dispose() {
		Log.info("Disposing music");
		stopAll();
		for (Music music : allMusic) {
			music.dispose();
		}
	}
}
