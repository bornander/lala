package com.bornander.lala.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bornander.lala.LalaGame;
import com.bornander.libgdx.StringResolver;

public class AndroidLauncher extends AndroidApplication implements StringResolver {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		LalaGame.TextResolver = this;
		initialize(new LalaGame(), config);
	}

	@Override
	public String resolveString(String id) {
		int xmlId = getResources().getIdentifier(id, "string", getPackageName());
		return getString(xmlId);
	}
	
}
