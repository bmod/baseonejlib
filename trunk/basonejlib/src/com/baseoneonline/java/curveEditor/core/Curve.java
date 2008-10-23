package com.baseoneonline.java.curveEditor.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class Curve {
	private final List<Point> points = new Vector<Point>();

	public Curve() {}


	public void addPoint(final Point p) {
		points.add(p);
	}

	public Point getPoint(final int i) {
		return points.get(i);
	}

	public int size() {
		return points.size();
	}

	public void sortPoints() {
		Collections.sort(points, xComparator);
	}

	public synchronized Bounds getBounds() {
		final Bounds r = new Bounds();
		for (final Point p : points) {
			addPoint(p);
		}
		return r;
	}

	static final Comparator<Point> xComparator = new Comparator<Point>() {
		public int compare(final Point o1, final Point o2) {
			if (o1.x < o2.x) {
				return -1;
			}
			if (o1.x > o2.x) {
				return 1;
			}
			return 0;
		};
	};

}



