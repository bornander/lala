package com.bornander.lala.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.bornander.lala.GameSettings;

public class HudAssets implements Disposable {
	private final Skin skin;
	
	public final NinePatch panel_metal;
	public final NinePatch panel_metal_scaled;
	
	
	public HudAssets(TextureAtlas atlas) {
		skin = new Skin(Gdx.files.internal("graphics/hud.skin"), atlas);
		
		panel_metal = new NinePatch(atlas.findRegion("panel_metal"), 14, 14, 14, 14);
		panel_metal_scaled = new NinePatch(atlas.findRegion("panel_metal"), 14, 14, 14, 14);
		panel_metal_scaled.scale(1.0f / 100.0f, 1.0f / 100.0f);
	}
	
	public Image getCursor() {
		Image image = new Image(skin, "cursor");
		return image;
	}
	
	public Image getClick() {
		Image image = new Image(skin, "click");
		image.setOrigin(Align.center);
		return image;
	}
	
	public Image getBlock() {
		Image image = new Image(skin, "block");
		image.setOrigin(Align.center);
		return image;
	}	
	
	public Image getTallBlock() {
		Image image = new Image(skin, "tallblock");
		image.setOrigin(Align.center);
		return image;
	}	
	
	
	public Image getTriangleBlock1() {
		Image image = new Image(skin, "triangleblock1");
		image.setOrigin(Align.center);
		return image;
	}		

	public Image getTriangleBlock2() {
		Image image = new Image(skin, "triangleblock2");
		image.setOrigin(Align.center);
		return image;
	}		
	
	
	public Button getRotateLeft() {
		return new Button(skin, "rotate_left");
	}
	
	public Button getRotateRight() {
		return new Button(skin, "rotate_right");
	}

	public Button getMirror() {
		return new Button(skin, "mirror");
	}

	public Button getConfirm() {
		return new Button(skin, "confirm");
	}

	public Button getPlay() {
		return new Button(skin, "play");
	}

	public Button getStop() {
		return new Button(skin, "stop");
	}
	
	public Button getReset() {
		return new Button(skin, "reset");
	}

	public Button getHome() {
		return new Button(skin, "home");
	}

	public Button getSound() {
		Button button = new Button(skin, "sound");
		button.setChecked(GameSettings.soundEnabled);
		return button;
	}

	public Button getMusic() {
		Button button = new Button(skin, "music");
		button.setChecked(GameSettings.musicEnabled);
		return button;
	}
	
	
	@Override
	public void dispose() {
		skin.dispose();
	}

}
