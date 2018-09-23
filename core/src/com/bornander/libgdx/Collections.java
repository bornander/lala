package com.bornander.libgdx;

import java.util.Map;

public class Collections {
	
	public static <K, T> T getOrDefault(Map<K, T> map, K key, T defaultValue) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		else {
			return defaultValue;
		}
	}
}
