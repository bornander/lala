package com.bornander.lala.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class NpcAssets {
	public final Animation bee;
	public final Animation fly;
	
	public NpcAssets(TextureAtlas atlas) {
		bee = new Animation(0.1f, atlas.findRegion("bee"), atlas.findRegion("bee_move"));
		bee.setPlayMode(PlayMode.LOOP);
		fly = new Animation(0.1f, atlas.findRegion("fly"), atlas.findRegion("fly_move"));
		fly.setPlayMode(PlayMode.LOOP);
	}	
}
