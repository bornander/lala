package com.bornander.lala;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Npc {
	void update(float delta);
	void render(SpriteBatch spriteBatch);
	void setAlien(Alien alien);
}
