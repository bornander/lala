package com.bornander.libgdx;

import com.badlogic.gdx.math.Vector2;

public class SAT {
	private static Vector2 projected = new Vector2();
	private static Vector2 project(Vector2[] polygon, Vector2 axis) {
		if (!axis.isUnit(0.01f))
			throw new RuntimeException("bad axis");

		float min = axis.dot(polygon[0]);
		float max = min;
		for (int i = 1; i < polygon.length; ++i) {
			float p = axis.dot(polygon[i]);
			if (p < min) {
				min = p;
			} else if (p > max) {
				max = p;
			}
		}
		return projected.set(min, max);
	}

	public static boolean getOverlap(Vector2 a, Vector2 b) {
		float minA;
		float maxA;
		float minB;
		float maxB;
		if (a.x < b.x) {
			minA = a.x;
			maxA = a.y;
			minB = b.x;
			maxB = b.y;
		}
		else {
			minA = b.x;
			maxA = b.y;
			minB = a.x;
			maxB = a.y;
		}
		
		if (maxA <= minB || minA >= maxB) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static float getOverlapDepth(Vector2 a, Vector2 b) {
		float minA;
		float maxA;
		float minB;
		float maxB;
		if (a.x < b.x) {
			minA = a.x;
			maxA = a.y;
			minB = b.x;
			maxB = b.y;
		}
		else {
			minA = b.x;
			maxA = b.y;
			minB = a.x;
			maxB = a.y;
			
		}
		
		if (maxA <= minB || minA >= maxB) {
			return 0.0f;
		}
		else {
			if (minB > minA && maxB < maxA) {
				return 100.0f;
			}
			else {
				return maxA - minB;
			}
		}
	}

	
	private static Vector2 p1 = new Vector2();
	private static Vector2 p2 = new Vector2();
	private static Vector2 normal = new Vector2();
	private static Vector2[] axes = new Vector2[100];
	private static Vector2 projectionA = new Vector2();
	private static Vector2 projectionB = new Vector2();
	
	static {
		for(int i = 0; i < axes.length; ++i) {
			axes[i] = new Vector2();
		}
	}
	
	public static boolean check(Vector2[] polygonA, Vector2[] polygonB) {
		int a = 0;
		for (int i = 0; i < polygonA.length; ++i) {
			p1.set(polygonA[i]);
			p2.set(polygonA[(i + 1) %  polygonA.length]);
			Vector2 edge = p1.sub(p2);
			normal.set(edge.y, -edge.x);
			axes[a++].set(normal.nor());
		}
		
		for (int i = 0; i < polygonB.length; ++i) {
			p1.set(polygonB[i]);
			p2.set(polygonB[(i + 1) %  polygonB.length]);
			Vector2 edge = p1.sub(p2);
			normal.set(edge.y, -edge.x);
			axes[a++].set(normal.nor());
		}
		
		for (int i = 0; i < polygonA.length + polygonB.length; ++i) {
			Vector2 axis = axes[i];
			projectionA.set(project(polygonA, axis));
			projectionB.set(project(polygonB, axis));
			
			if (!getOverlap(projectionA, projectionB)) {
				return false;
			}
			else {
				if (getOverlapDepth(projectionA, projectionB) < 0.1f) {
					return false;
				}
			}
		}
		return true;
	}
}
