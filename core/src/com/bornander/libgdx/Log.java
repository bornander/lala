package com.bornander.libgdx;

import com.badlogic.gdx.Gdx;

public class Log {

	private final static String TAG = "lala";
	
	public static void debug(String format, Object...params) {
		Gdx.app.debug(TAG, String.format(format, params));
	}
	
	public static void debug(Object value) {
		debug("%s", value);
	}	
	
	public static void info(String format, Object...params) {
		Gdx.app.log(TAG, String.format(format, params));
	}

	public static void info(Object value) {
		info("%s", value);
	}	

	public static void error(String format, Object...params) {
		Gdx.app.error(TAG, String.format(format, params));
	}

	public static void error(Throwable exception, String format, Object...params) {
		Gdx.app.error(TAG, String.format(format, params), exception);
	}
}