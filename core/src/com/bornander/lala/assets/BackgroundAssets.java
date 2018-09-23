package com.bornander.lala.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class BackgroundAssets implements Disposable{
	public final Texture coloredDesert;
	public final Texture coloredGrass;
	
	public BackgroundAssets() {
		coloredDesert = new Texture(Gdx.files.internal("graphics/backgrounds/colored_desert.png"));
		coloredGrass = new Texture(Gdx.files.internal("graphics/backgrounds/colored_grass.png"));
	}

	@Override
	public void dispose() {
		coloredDesert.dispose();
	}	
}
