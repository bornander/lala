package com.bornander.lala.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class NpcAssets {
	public final Animation<TextureRegion> bee;
	public final Animation<TextureRegion> fly;
	
	public NpcAssets(TextureAtlas atlas) {
		bee = new Animation<TextureRegion>(0.1f, atlas.findRegion("bee"), atlas.findRegion("bee_move"));
		bee.setPlayMode(PlayMode.LOOP);
		fly = new Animation<TextureRegion>(0.1f, atlas.findRegion("fly"), atlas.findRegion("fly_move"));
		fly.setPlayMode(PlayMode.LOOP);
	}	
}
