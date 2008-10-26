package com.baseoneonline.java.curveEditor.core;

import java.util.ArrayList;
import java.util.List;

public class Curve {

	private final List<Point> points = new ArrayList<Point>();

	private final Bounds bounds = new Bounds();

	public Curve() {

	}

	public List<Point> getPoints() {
		return points;
	}

	public Point getPoint(final int i) {
		return points.get(i);
	}

	public void addPoint(final Point p) {
		points.add(p);
		updateBounds();
	}

	public void removePoint(final Point p) {
		points.remove(p);
	}

	public void removePoint(final int index) {
		points.remove(index);
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

	public int size() {
		return points.size();
	}

}
