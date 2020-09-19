package com.bornander.lala.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public class AlienAssets {
	
	private final static Random RND = new Random();
	
	private final TextureAtlas atlas;
	private final String prefix;
	
	public final Animation<TextureRegion> stand;
	public final Animation<TextureRegion> walk;
	public final Animation<TextureRegion> fall;
	public final Animation<TextureRegion> hit_ground;
	public final Animation<TextureRegion> sad;
	public final Animation<TextureRegion> climb;
	public final Animation<TextureRegion> sing;
	
	private final Animation<TextureRegion>[] speaks;
	private final Animation<TextureRegion>[] dances;
	
	
	public AlienAssets(TextureAtlas atlas, String prefix) {
		this.atlas = atlas;
		this.prefix = prefix; 
				

		stand = new Animation<TextureRegion>(0.16f, find("stand"));
		walk = new Animation<TextureRegion>(0.25f, find("walk2"), find("walk1"));
		walk.setPlayMode(PlayMode.LOOP);
		fall = new Animation<TextureRegion>(0.16f, find("hit"));
		hit_ground = new Animation<TextureRegion>(0.2f, find("duck"), find("stand"));
		sad = new Animation<TextureRegion>(0.16f, find("sad"));
		climb = new Animation<TextureRegion>(0.2f, find("climb1"), find("climb2"));
		climb.setPlayMode(PlayMode.LOOP);
		
		speaks = new Animation[] {
				new Animation<TextureRegion>(1.0f / 6.0f, find("stand"), find("front"), find("duck"), find("front"), find("duck"), find("front")),
				new Animation<TextureRegion>(1.0f / 6.0f, find("stand"), find("front"), find("jump"), find("front"), find("duck"), find("front")),
				new Animation<TextureRegion>(1.0f / 6.0f, find("stand"), find("front"), find("climb1"), find("climb2"), find("climb1"), find("front"))
		};

		dances = new Animation[] {
				new Animation<TextureRegion>(0.2f, find("stand"), findAndFlip("stand"), findAndFlip("front"), find("front"), find("duck"), find("front"), find("duck"), find("front")),
				new Animation<TextureRegion>(0.2f, find("stand"), findAndFlip("stand"), findAndFlip("front"), find("front"), find("climb1"), find("climb2"), find("climb1"), find("climb2")),
				new Animation<TextureRegion>(0.2f, find("stand"), findAndFlip("front"), findAndFlip("stand"), find("front"), find("duck"), findAndFlip("duck"), find("front"), find("jump")),
		};
		
		sing = new Animation<TextureRegion>(0.2f, find("duck"), findAndFlip("duck"), find("duck"), findAndFlip("duck"), find("stand"), findAndFlip("stand"), find("front"), findAndFlip("front"), find("jump"), findAndFlip("jump"));
		
		for(Animation animation : dances)
			animation.setPlayMode(PlayMode.LOOP);
	}	
	
	private TextureRegion find(String name) {
		return atlas.findRegion(String.format("%s_%s", prefix, name));
	}
	
	private TextureRegion findAndFlip(String name) {
		TextureRegion textureRegion = new TextureRegion(find(name));
		textureRegion.flip(true, false);
		return textureRegion;
	}
	
	
	public Animation<TextureRegion> getRandomSpeak() {
		return speaks[RND.nextInt(speaks.length)];
	}

	public Animation<TextureRegion> getRandomDance() {
		return dances[RND.nextInt(dances.length)];
	}

}
