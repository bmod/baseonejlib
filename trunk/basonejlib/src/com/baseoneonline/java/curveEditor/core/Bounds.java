package com.baseoneonline.java.curveEditor.core;



public class Bounds {
	public float left;
	public float right;
	public float top;
	public float bottom;
	
	public Bounds() {
		reset();
	}
	
	public void reset() {
		left = Float.MAX_VALUE;
		right = Float.MIN_VALUE;
		top = Float.MAX_VALUE;
		bottom = Float.MIN_VALUE;
	}
	
	
	public void merge(final Point p) {
		if (p.x < left) {
			left = p.x;
		}
		if (p.y < top) {
			top = p.y;
		}
		if (p.x > right) {
			right = p.x;
		}
		if (p.y > bottom) {
			bottom = p.y;
		}
		
	}
	
}