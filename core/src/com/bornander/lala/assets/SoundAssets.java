package com.bornander.lala.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.bornander.lala.GameSettings;
import com.bornander.lala.Material;
import com.bornander.libgdx.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SoundAssets implements Disposable {
	
	public static class LoopingSound {
		
		public static final LoopingSound NONE = new LoopingSound(null, -1);
		
		public final Sound sound;
		public final long id;
		public LoopingSound(Sound sound, long id) {
			this.sound = sound;
			this.id = id;
		}
		
		public void stop() {
			if (sound != null)
				sound.stop(id);
		}
	}

	private static final Random RND = new Random();
	public static final long NO_SOUND = -1;

	private final Sound step_dirt;
	private final Sound step_grass;
	private final Sound step_metal;
	private final Sound step_snow;
	private final Sound step_stone;
	private final Sound step_wood;
	private final Sound step_mushroom;
	
	private final Sound teleport_out;
	private final Sound teleport_in;
	
	private final Sound ufo_fly;
	private final Sound level_completed_first_time;
	
	private final Sound alien_speak_1;
	private final Sound alien_speak_2;
	private final Sound alien_speak_3;
	private final Sound alien_speak_4;
	private final Sound alien_speak_5;
	private final Sound alien_speak_6;
	private final Sound alien_speak_7;
	private final Sound alien_speak_8;
	private final Sound alien_speak_9;
	private final Sound alien_speak_10;
	
	private final Sound alien_blocked_1;
	private final Sound alien_hurt_1;
	private final Sound alien_hurt_2;
	
	private final Sound mushroom_1;
	private final Sound mushroom_2;
	private final Sound mushroom_3;
	private final Sound mushroom_4;
	
	private final Sound[] sounds;
	private final Sound[] alienSpeak;
	private final Sound[] alienBlocked;
	private final Sound[] alienHurt;
	private final Sound[] mushroom;
	private final Map<Material, Sound> materialSounds = new HashMap<Material, Sound>(); 
	
	public SoundAssets() {
		sounds = new Sound[] {
				step_dirt = load("step_dirt.wav"),
				step_grass = load("step_grass.wav"),
				step_metal = load("step_metal.wav"),
				step_snow = load("step_snow.wav"),
				step_stone = load("step_stone.wav"),
				step_wood = load("step_wood.wav"),
				step_mushroom = load("step_snow.wav"),
				teleport_out = load("teleport_out.wav"),
				teleport_in = load("teleport_in.wav"),
				ufo_fly = load("ufo_fly.wav"),
				level_completed_first_time = load("level_completed_first_time.wav"),
				alien_speak_1 = load("alien_speak_1.wav"),
				alien_speak_2 = load("alien_speak_2.wav"),
				alien_speak_3 = load("alien_speak_3.wav"),
				alien_speak_4 = load("alien_speak_4.wav"),
				alien_speak_5 = load("alien_speak_5.wav"),
				alien_speak_6 = load("alien_speak_6.wav"),
				alien_speak_7 = load("alien_speak_7.wav"),
				alien_speak_8 = load("alien_speak_8.wav"),
				alien_speak_9 = load("alien_speak_9.wav"),
				alien_speak_10 = load("alien_speak_10.wav"),
				alien_blocked_1 = load("alien_blocked_1.wav"),
				alien_hurt_1 = load("alien_hurt_1.wav"),
				alien_hurt_2 = load("alien_hurt_2.wav"),
				mushroom_1 = load("mushroom_1.wav"),
				mushroom_2 = load("mushroom_2.wav"),
				mushroom_3 = load("mushroom_3.wav"),
				mushroom_4 = load("mushroom_4.wav"),
		};
		
		alienSpeak = new Sound[] {
			alien_speak_1,	
			alien_speak_2,	
			alien_speak_3,	
			alien_speak_4,	
			alien_speak_5,
			alien_speak_6,
			alien_speak_7,
			alien_speak_8,
			alien_speak_9,
			alien_speak_10
		};
		
		alienBlocked = new Sound[] {
			alien_blocked_1	
		};
		
		alienHurt = new Sound[] {
			alien_hurt_1,
			alien_hurt_2
		};
		
		mushroom = new Sound[] {
			mushroom_1,
			mushroom_2,
			mushroom_3,
			mushroom_4,
		};
		
		materialSounds.put(Material.DIRT, step_dirt);
		materialSounds.put(Material.SAND, step_dirt);
		materialSounds.put(Material.GRASS, step_grass);
		materialSounds.put(Material.METAL, step_metal);
		materialSounds.put(Material.SNOW, step_snow);
		materialSounds.put(Material.STONE, step_stone);
		materialSounds.put(Material.WOOD, step_wood);
		materialSounds.put(Material.MUSHROOM, step_mushroom);
	}
	
	private static Sound load(String filename) {
		return Gdx.audio.newSound(Gdx.files.internal(String.format("sounds/%s", filename)));
	}
	
	private void play(Sound sound) {
		if (GameSettings.soundEnabled) {
			sound.play(GameSettings.soundVolume);
		}
	}

	private LoopingSound loop(Sound sound) {
		if (GameSettings.soundEnabled) {
			long id = sound.loop(GameSettings.soundVolume);
			return new LoopingSound(sound, id);
		}
		else {
			return LoopingSound.NONE;
		}
	}
	
	private void playRandom(Sound[] source, float volumeFactor) {
		if (GameSettings.soundEnabled) {
			source[RND.nextInt(source.length)].play(GameSettings.soundVolume * volumeFactor);
		}
	}

	private void playRandom(Sound[] source) {
		playRandom(source, 1.0f);
	}
	
	public void playRandomSpeak() {
		playRandom(alienSpeak);
	}
	
	public void playRandomHurt() {
		playRandom(alienHurt);
	}
	
	public void playRandomBlocked() {
		playRandom(alienBlocked);
	}
	
	public void playRandomMushroom() {
		playRandom(mushroom, 0.5f);
	}
	
	public void stopAll() {
		for(Sound sound : sounds)
			sound.stop();
	}

	@Override
	public void dispose() {
		Log.info("Disposing sounds");
		for(Sound sound : sounds) {
			sound.dispose();
		}
	}

	public void playStep(Material material) {
		Sound sound = materialSounds.get(material);
		if (sound != null)
			play(sound);
	}
	
	public LoopingSound loopStep(Material material) {
		if (GameSettings.soundEnabled) {
			Sound sound = materialSounds.get(material);
			if (sound != null)
				return loop(sound);
		}
		return LoopingSound.NONE;
	}
	
	public void playTeleportOut() {
		play(teleport_out);
	}
	
	public void playTeleportIn() {
		play(teleport_in);
	}
	
	public void playUfoFly() {
		play(ufo_fly);
	}
	
	public void playLevelCompletedFirstTime() {
		play(level_completed_first_time);
	}
}
