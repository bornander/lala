package com.bornander.lala;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.bornander.lala.screens.SplashScreen;
import com.bornander.libgdx.StringResolver;

public class LalaGame extends Game {
	public static StringResolver TextResolver = null;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		GameSettings.load();
		Assets.instance.initialize(new AssetManager());
		setScreen(new SplashScreen(this));
	}
	
	@Override
	public void pause() {
		super.pause();
		Assets.instance.sounds.stopAll();
		Assets.instance.music.stopAll();
		GameSettings.save();
	}
	
	@Override
	public void resume() {
		Assets.instance.initialize(new AssetManager());
		Assets.instance.music.resume();
	}
	
	@Override
	public void dispose() {
		Assets.instance.dispose();
		GameSettings.save();
		super.dispose();
	}
}