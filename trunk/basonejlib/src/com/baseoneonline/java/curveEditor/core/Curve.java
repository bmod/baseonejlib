package com.baseoneonline.java.curveEditor.core;

public class Curve {

	private final Point[] points;

	private final Bounds bounds = new Bounds();

	public Curve(final Point[] pts) {
		points = pts;
	}

	public Point[] getPoints() {
		return points;
	}

	public void updateBounds() {
		bounds.reset();
		for (final Point p : points) {
			bounds.merge(p);
		}
	}

	public Bounds getBounds() {
		return bounds;
	}

}
