package com.bornander.lala.assets;

import com.bornander.libgdx.StringResolver;

import java.util.Random;

public class TextAssets {
	
	private final static Random rnd = new Random();
	public final static String[] SUB_TAGLINE_IDS = new String[] {
		"sub_tagline_0",	
		"sub_tagline_1",	
		"sub_tagline_2",	
		"sub_tagline_3",	
		"sub_tagline_4",	
		"sub_tagline_5",	
		"sub_tagline_6"	
	};
	public final static String[] COMPLETED_IDS = new String[] { 
		"completed_0",
		"completed_1",
		"completed_2",
		"completed_3",
		"completed_4"
	};
	
	private final StringResolver r;
	
	
	public TextAssets(StringResolver resolver) {
		this.r = resolver;
	}
	
	
	public String get(String id) {
		try {
			return r.resolveString(id);
		}
		catch(Throwable t) {
			return null;
		}
	}
	
	public String getRandom(String[] possibleIds) {
		return get(possibleIds[rnd.nextInt(possibleIds.length)]);
	}
}
