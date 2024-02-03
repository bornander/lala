package com.bornander.lala.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.bornander.lala.Material;

import java.util.Locale;

public class MenuAssets {
	private final Skin skin;
	
	public final TextureRegion starGold;
	public final TextureRegion locked;

	public MenuAssets(TextureAtlas atlas) {
		skin = new Skin(Gdx.files.internal("graphics/menu.skin"), atlas);
	
	
		starGold = atlas.findRegion("starGold");
		locked = atlas.findRegion("locked");
	}
	
	public Button getAlienBeige() {
		return new Button(skin, "alien_beige");
	}
	
	public Button getAlienBlue() {
		return new Button(skin, "alien_blue");
	}
	
	public Button getAlienGreen() {
		return new Button(skin, "alien_green");
	}
	
	public Button getAlienPink() {
		return new Button(skin, "alien_pink");
	}
	
	public Button getAlienYellow() {
		return new Button(skin, "alien_yellow");
	}

	public Button getByAlien(String alien) {
		return new Button(skin, String.format("alien_%s", alien));
	}
	
	public Button getByMaterial(Material material) {
		try {
			return new Button(skin, String.format("type_%s", material.toString().toLowerCase(Locale.US)));
		}
		catch (GdxRuntimeException e) {
			return new Button(skin, "type_dirt");
		}
	}
}
