package com.bornander.lala;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.bornander.libgdx.Log;

public abstract class GameScreen implements Screen {
	
	public final Game game;
	
	public GameScreen(Game game) {
		this.game = game;
	}
	
	@Override
	public void show() {
		Log.debug("Showing screen %s", this);
	}
	
	public abstract void update(float delta);
	public abstract void render();

	@Override
	public void render(float delta) {
		Assets.instance.music.update(delta);
		update(delta);
		render();
	}

	@Override
	public void resize(int width, int height) {
		Log.debug("Resizing screen %s to (%d, %d)", this, width, height);
	}

	@Override
	public void pause() {
		Log.debug("Pause screen %s", this);
	}

	@Override
	public void resume() {
		Log.debug("Resume screen %s", this);
	}

	@Override
	public void hide() {
		Log.debug("Hide screen %s", this);
	}

	@Override
	public void dispose() {
		Log.debug("Dispose screen %s", this);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
