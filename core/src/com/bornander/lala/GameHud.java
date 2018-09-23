package com.bornander.lala;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bornander.lala.screens.AlienSelectScreen;
import com.bornander.lala.screens.FadeScreen;
import com.bornander.libgdx.Log;

public class GameHud {
	
	private final SpriteBatch spriteBatch = new SpriteBatch();
	
	protected final float vHeight = 240 * 3f;
	protected final float vWidth = vHeight / ((float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());
	
	protected final GlyphLayout dialogGlyphLayout = new GlyphLayout();
	protected final float verticalBase;
	
	protected final Stage stage;
	
	protected final Button play;
	protected final Button stop;
	protected final Button reset;
	protected final Button home;
	protected final AlienSelectScreen homeScreen;
	protected final String musicTrack;
	protected final ScreenViewport viewport;
	protected final Button sound;
	protected final Button music;
	
	private boolean dialogEnabled = false;
	
	private final Game game;
	
	public GameHud(float screenVerticalBase, AlienSelectScreen homeScreen, Game game, String musicTrack) {
		this.homeScreen = homeScreen;
		this.game = game;
		this.musicTrack = musicTrack;
		viewport = new ScreenViewport();
		viewport.setUnitsPerPixel(vHeight / Gdx.graphics.getHeight());
		
		verticalBase = viewport.getUnitsPerPixel() * screenVerticalBase;
		
		stage = new Stage(viewport) {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        		boolean isHudTouch = super.touchDown(screenX, screenY, pointer, button);
        		if (!isHudTouch && pointer == 0) 
        			GameHud.this.touchDown(screenX, screenY, pointer);

        		return isHudTouch;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				boolean isHudTouchDragged = super.touchDragged(screenX, screenY, pointer);
				if (!isHudTouchDragged && pointer == 0) 
					GameHud.this.touchDragged(screenX, screenY, pointer);
				
				return isHudTouchDragged;
			}
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        		boolean isHudTouch = super.touchUp(screenX, screenY, pointer, button);
        		if (!isHudTouch && pointer == 0)
        			GameHud.this.touchUp(screenX, screenY, pointer);

        		return isHudTouch;
			}
		};
		
		
		
		play = Assets.instance.hud.getPlay();
		stop = Assets.instance.hud.getStop();
		reset = Assets.instance.hud.getReset();
		home = Assets.instance.hud.getHome();
		sound = Assets.instance.hud.getSound();
		music = Assets.instance.hud.getMusic();
		

		layout();
		addButtonListeners();
		
		
	}
	
	private void addButtonListeners() {
		play.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (event.getPointer() == 0) {
					closeDialog();
					GameHud.this.onPlayPressed();
				}
			}
		});
		
		stop.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (event.getPointer() == 0) {
					closeDialog();
					GameHud.this.onStopPressed();
				}
			}
		});
		
		reset.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (event.getPointer() == 0) {
					closeDialog();
					GameHud.this.onResetPressed();
				}
			}
		});

		home.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (event.getPointer() == 0) {
					Assets.instance.sounds.stopAll();
					closeDialog();
					game.setScreen(new FadeScreen(homeScreen.game, (GameScreen)game.getScreen(), homeScreen, ""));
				}
			}
		});		

		sound.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (event.getPointer() == 0) {
					GameSettings.soundEnabled = sound.isChecked();
					if (!GameSettings.soundEnabled)
						Assets.instance.sounds.stopAll();
				}
			}
		});		
		
		music.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (event.getPointer() == 0) {
					GameSettings.musicEnabled = music.isChecked();
					if (!GameSettings.musicEnabled)
						Assets.instance.music.stopAll();
					else {
						Assets.instance.music.play(musicTrack);
					}
				}
			}
		});		
	}	
	
	protected void onPlayPressed() {
		Log.debug("onPlayPressed");
	}

	protected void onStopPressed() {
		Log.debug("onStopPressed");
	}
	
	protected void onResetPressed() {
		Log.debug("onResetPressed");
	}
	
	private void layout() {
		float bs = 108;
		
		play.setTransform(true);
		stop.setTransform(true);
		reset.setTransform(true);
		home.setTransform(true);
		sound.setTransform(true);
		music.setTransform(true);
		
		play.setPosition(4, 0 * verticalBase + bs * 0 + 4);
		stop.setPosition(4, 0 * verticalBase + bs * 1 + 4);
		reset.setPosition(4, 0 * verticalBase + bs * 2 + 4);
		home.setPosition(4, stage.getHeight() - bs);
		sound.setPosition(stage.getWidth() - bs, stage.getHeight() - bs);
		music.setPosition(stage.getWidth() - 2 * bs, stage.getHeight() - bs);
		
		stage.addActor(play);
		stage.addActor(stop);
		stage.addActor(reset);
		stage.addActor(home);
		stage.addActor(sound);
		stage.addActor(music);

		play.setOrigin(Align.center);
		stop.setOrigin(Align.center);
		reset.setOrigin(Align.center);
		home.setOrigin(Align.center);
		sound.setOrigin(Align.center);
		music.setOrigin(Align.center);
	}
	
	public void initialize(Object state) {
		Gdx.input.setInputProcessor(stage);
	}
	
	protected void touchDown(int screenX, int screenY, int pointer) {
		//Log.debug("touchDown(%d, %d)", screenX, screenY);
		closeDialog();
	}

	protected void touchDragged(int screenX, int screenY, int pointer) {
		//Log.debug("touchDragged(%d, %d)", screenX, screenY);
	}
	
	protected void touchUp(int screenX, int screenY, int pointer) {
		//Log.debug("touchUp(%d, %d)", screenX, screenY);
	}
	
	protected void setButtonsEnabled(boolean enabled, Button...buttons) {
		for(Button button : buttons)
			button.setTouchable(enabled ? Touchable.enabled : Touchable.disabled);
	}

	protected void hideButtons(Button...buttons) {
		for(Button button : buttons)
			button.setVisible(false);
	}
	
	private static void disableButton(Button button) {
		button.setDisabled(true);
		button.setTouchable(Touchable.disabled);
		button.addAction(Actions.fadeOut(0.2f));
	}
	
	public void hideButtonsWhenCompleted() {
		disableButton(home);
		disableButton(play);
		disableButton(stop);
		disableButton(reset);
	}
	
	public boolean update(float elapsed) {
		stage.act(elapsed);
		return false;
	}
	
	public void draw() {
	}
	
	public void postDraw() {
		stage.draw();
		if (dialogEnabled) {
			spriteBatch.setProjectionMatrix(stage.getCamera().combined);
			spriteBatch.begin();
			
			float tw = dialogGlyphLayout.width;
			float th = dialogGlyphLayout.height;
					
			float dw = tw + 0.05f * vWidth;
			float dh = th + 0.05f * vWidth;
			
			float bx = (vWidth - dw) / 2.0f;
			float by = vHeight - dh * 1.2f;
			
			Assets.instance.hud.panel_metal.draw(spriteBatch, bx, by, dw, dh);
			Assets.instance.fonts.dialog.draw(spriteBatch, dialogGlyphLayout, bx + (dw-tw) / 2.0f, by + dh - (dh-th) / 2.0f);
			
			spriteBatch.end();
		}
	}
	
	public void openDialog(String text) {
		dialogEnabled = true;
		float dw = vWidth * 0.8f;
		dialogGlyphLayout.setText(Assets.instance.fonts.dialog, text, Color.WHITE, dw, Align.topLeft, true);		
	}
	
	public void closeDialog() {
		dialogEnabled = false;
	}

	/*
	public void fireButtonHighlights(List<String> buttons) {
		float scale = 1.5f;
		float duration = 0.5f;
		if (buttons.contains("PLAY")) {
			play.setZIndex(100);
			play.setOrigin(0, 0);
			play.addAction(repeat(2, sequence(scaleTo(scale, scale, duration, Interpolation.sine),scaleTo(1, 1, duration, Interpolation.sine))));
		}
		if (buttons.contains("STOP")) {
			stop.setZIndex(100);
			stop.setOrigin(0, 0);
			stop.addAction(repeat(2, sequence(scaleTo(scale, scale, duration, Interpolation.sine),scaleTo(1, 1, duration, Interpolation.sine))));
		}
		if (buttons.contains("RESET")) {
			reset.setZIndex(100);
			reset.setOrigin(0, 0);
			reset.addAction(repeat(2, sequence(scaleTo(scale, scale, duration, Interpolation.sine),scaleTo(1, 1, duration, Interpolation.sine))));
		}
	}
	*/

	public float getTrayTarget(float showing, float hidden) {
		return hidden;
	}
}