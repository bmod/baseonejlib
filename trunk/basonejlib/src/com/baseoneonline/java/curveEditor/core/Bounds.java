package com.baseoneonline.java.curveEditor.core;

public class Bounds {

	public float left;
	public float right;
	public float top;
	public float bottom;

	public Bounds() {
		reset();
	}

	public float getWidth() {
		return right-left;
	}

	public float getHeight() {
		return bottom-top;
	}

	public void addPoint(final Point p) {
		if (p.x < left) {
			left = p.x;
		} else if (p.x > right) {
			right = p.x;
		}
		if (p.y < top) {
			top = p.y;
		} else if (p.y > bottom) {
			bottom = p.y;
		}
	}

	public void merge(final Bounds b) {
		if (b.left < left) {
			left = b.left;
		} else if (b.right > right) {
			b.right = right;
		}
		if (b.top < top) {
			top = b.top;
		} else if (b.bottom > bottom) {
			bottom = b.bottom;
		}
	}

	public void reset() {
		left = Float.MAX_VALUE;
		right = Float.MIN_VALUE;
		top = Float.MAX_VALUE;
		bottom = Float.MIN_VALUE;
	}

}
