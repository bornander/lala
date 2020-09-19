package com.bornander.lala.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bornander.lala.Assets;
import com.bornander.lala.GameScreen;

public class FadeScreen extends GameScreen {
	
	private static final ShapeRenderer RENDERER = new ShapeRenderer();
	
	private final OrthographicCamera camera = new OrthographicCamera();
	private final float fadeDuration = 2.0f;

	private final Stage stage;
	private final Label caption;
	private final Color captionColor = new Color(Color.WHITE);
	private final GameScreen from;
	private final GameScreen to;
	
	private float totalTime;
	private boolean updateTo;


	public FadeScreen(Game game, GameScreen from, GameScreen to, boolean updateTo, String message) {
		super(game);
		this.from = from;
		this.to = to;
		this.updateTo = updateTo;
		camera.setToOrtho(false, 64, 64);
		
		ScreenViewport viewport = new ScreenViewport();
		float vHeight = 1024;
		viewport.setUnitsPerPixel(vHeight / Gdx.graphics.getHeight());

		stage = new Stage(viewport);
		
		
		caption = new Label(message, new LabelStyle(Assets.instance.fonts.dialog, Color.WHITE));
		caption.setAlignment(Align.center);
		stage.addActor(caption);

		float vWidth = vHeight / ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth());
		caption.setPosition((vWidth - caption.getWidth())/2, vHeight *0.35f);
	}
	
	public FadeScreen(Game game, GameScreen from, GameScreen to, String message) {
		this(game, from, to, false, message);
	}
	
	private float getFraction() {
		return Math.min(1.0f, totalTime / fadeDuration);
	}
	
	private float getHalfFraction() {
		if (totalTime < 1) {
			return 1.0f;
		}
		else{
			return 1.0f-Math.min(1.0f, (totalTime - 1) / (fadeDuration/2));
		}
	}
	
	private void draw(final float fraction) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);		
		RENDERER.setColor(1, 1, 1, fraction);
		RENDERER.setProjectionMatrix(camera.combined);
		RENDERER.begin(ShapeType.Filled);
		RENDERER.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		RENDERER.end();
		stage.draw();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		
	}

	@Override
	public void update(float delta) {
		from.update(delta);
		if (updateTo)
			to.update(delta);
		totalTime += Math.min(delta, 1/60f);
		if (getFraction() >= 1.0f) {
			game.setScreen(to);
		}
		
		Color color = caption.getColor();
		captionColor.set(color.r, color.g, color.b, getHalfFraction());
		caption.setColor(captionColor);
		
	}

	@Override
	public void render() {
		final float fraction = getFraction();
		if (fraction < 0.5f) {
			from.render();
			draw(fraction / 0.5f);
		}
		else {
			to.render();
			draw(1.0f - ((fraction - 0.5f) / 0.5f));
		}
	}
}
