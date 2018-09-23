package com.bornander.libgdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TiledImage extends Actor {
	
	private final Texture texture;
	
	public TiledImage(FileHandle file) {
		texture = new Texture(file);
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, 0, 0, texture.getWidth()*2, texture.getHeight()*2);
	}
}
