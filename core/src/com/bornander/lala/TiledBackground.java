package com.bornander.lala;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;

public class TiledBackground implements Disposable {

	private final Texture texture;
	private final float textureWidth;
	private final Image[] backgroundImages;
	private float x;
	
	public TiledBackground(float screenWidth, Texture texture, Stage stage) {
		this.texture = texture;
		this.textureWidth = (float)texture.getWidth();
		backgroundImages = new Image[1 + (int)Math.ceil(screenWidth / textureWidth)];
		for(int i = 0; i < backgroundImages.length; ++i) {
			backgroundImages[i] = new Image(texture);
			backgroundImages[i].setX(textureWidth * i);
			stage.addActor(backgroundImages[i]);
		}		
	}
	
	public void update(float delta){
		x -= delta*48;		
		if (x < -textureWidth) {
			x +=textureWidth;
		}
		for(int i = 0; i < backgroundImages.length; ++i) {
			backgroundImages[i].setX(x + textureWidth * i);
		}
	}

	@Override
	public void dispose() {
		texture.dispose();
	}
}
