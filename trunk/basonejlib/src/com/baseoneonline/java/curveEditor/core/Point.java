package com.baseoneonline.java.curveEditor.core;

import java.awt.geom.Point2D;

public class Point {
	public float x;
	public float y;

	public Point() {
		this(0, 0);
	}

	public Point(final float x, final float y) {
		this.x = x;
		this.y = y;
	}
	
	public Point2D toPoint2D() {
		return new Point2D.Float(x, y);
	}

}