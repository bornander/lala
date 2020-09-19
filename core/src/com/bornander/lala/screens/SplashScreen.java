package com.bornander.lala.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bornander.lala.Assets;
import com.bornander.lala.GameScreen;
import com.bornander.lala.TiledBackground;
import com.bornander.lala.assets.TextAssets;

public class SplashScreen extends GameScreen {
	private final Stage stage;
	
	private final TiledBackground background;
	
	private float elapsed;
	private boolean fadeStarted = false;
	
	public SplashScreen(Game game) {
		super(game);
		
		ScreenViewport viewport = new ScreenViewport();
		float vHeight = 1024;
		viewport.setUnitsPerPixel(vHeight / Gdx.graphics.getHeight());

		stage = new Stage(viewport);

		float vWidth = vHeight / ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth());
		background = new TiledBackground(vWidth, Assets.instance.backgrounds.coloredGrass, stage);
	
		Label title = new Label("LALA", new LabelStyle(Assets.instance.fonts.menu_title, Color.WHITE));
		title.setAlignment(Align.center);
		stage.addActor(title);
		title.setPosition((vWidth - title.getWidth()) / 2.0f, vHeight /2.0f);

		Label caption = new Label(Assets.instance.texts.get("tagline"), new LabelStyle(Assets.instance.fonts.dialog, Color.WHITE));
		stage.addActor(caption);
		caption.setPosition((vWidth - caption.getWidth())/2, vHeight *0.35f);

		Label tagLine = new Label(Assets.instance.texts.getRandom(TextAssets.SUB_TAGLINE_IDS), new LabelStyle(Assets.instance.fonts.tag_line, Color.WHITE));
		stage.addActor(tagLine);
		tagLine.setPosition(vWidth - (tagLine.getWidth() + 32), 16);

		
		stage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SplashScreen.this.startFade();
			}
		});
	}
	
	private void startFade() {
		if (!fadeStarted) {
			fadeStarted = true;
			game.setScreen(new FadeScreen(game, SplashScreen.this, new AlienSelectScreen(game), true, null));
		}
	}
	
	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void update(float delta) {
		elapsed += delta;
		if (elapsed > 3.0f)
			startFade();
		background.update(delta);
		stage.act(delta);
	}
	
	@Override
	public void render() {
		//Gdx.gl.glClearColor(0,0,0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		background.dispose();
	}
}