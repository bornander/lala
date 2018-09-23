package com.bornander.lala.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.bornander.lala.Assets;
import com.bornander.lala.Block;
import com.bornander.lala.DialogController;
import com.bornander.lala.GameHud;
import com.bornander.lala.GameScreen;
import com.bornander.lala.Level;
import com.bornander.lala.huds.RunningHud;
import com.bornander.lala.huds.TransformingHud;
import com.bornander.libgdx.Log;

import java.util.HashMap;
import java.util.Map;

public class GameplayScreen extends GameScreen implements DialogController {
	
	public enum State {
		TRANSFORMING,
		RUNNING
	}
	
	private final FPSLogger fpsLogger = new FPSLogger();
	
	public final Level level;
	
	private State state = State.TRANSFORMING;
	private Map<State, GameHud> gameHuds = new HashMap<State, GameHud>();
	private GameHud hud; 
	private AlienSelectScreen homeScreen;
	
	public GameplayScreen(Game game, AlienSelectScreen homeScreen, Level level) {
		super(game);
		this.homeScreen = homeScreen;
		this.level = level;
		this.level.dialogController = this;

		
		gameHuds.put(State.TRANSFORMING, new TransformingHud(homeScreen, level.availableTrayScreenHeight, this, level.levelData.music_track));
		gameHuds.put(State.RUNNING,  new RunningHud(homeScreen, level.availableTrayScreenHeight, this, level.levelData.music_track));
		setState(State.TRANSFORMING, null);
		
		TransformingHud thud = (TransformingHud) gameHuds.get(State.TRANSFORMING);
		if (level.levelInfo.chapter.index == 0) {
			switch(level.levelInfo.id) {
			case 0: thud.runTutorial1();
			break;
			case 1: thud.runTutorial2();
			break;
			case 4: thud.runTutorial3();
			break;
			case 5: thud.runTutorial4();
			break;
			}
		}
		
		if (this.level.levelData.message != null)
			openDialog(this.level.levelData.message);
		
		Assets.instance.music.play(level.levelData.music_track);
	}
	
	public void openDialog(String message) {
		hud.openDialog(message);
	}
	
	public void setState(State newState, Object hudState) {
		Log.info("Change state from %s to %s (hudState=%s)", state, newState, hudState);
		state = newState;
		hud = gameHuds.get(state);
		hud.initialize(hudState);
	}
	
	public void setState(State newState) {
		setState(newState, null);
	}
	
	public void placeBlock(Block block) {
		level.place(block);
	}
	
	@Override
	public void update(float delta) {
		if (hud.update(delta)) {
			game.setScreen(new FadeScreen(homeScreen.game, (GameScreen)game.getScreen(), homeScreen, ""));
		}
	}

	@Override
	public void render() {
		//Gdx.gl.glClearColor(0,0,0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		hud.draw();
		level.render();
		hud.postDraw();
		//fpsLogger.log();
	}
	
	@Override
	public void hide() {
		super.hide();
		Assets.instance.sounds.stopAll();
	}
}
