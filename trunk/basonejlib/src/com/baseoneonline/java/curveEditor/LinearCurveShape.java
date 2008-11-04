package com.baseoneonline.java.curveEditor;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class LinearCurveShape implements Shape {

	public boolean contains(final Point2D p) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(final Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(final double x, final double y) {
		return false;
	}

	public boolean contains(final double x, final double y, final double w, final double h) {
		// TODO Auto-generated method stub
		return false;
	}

	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	public PathIterator getPathIterator(final AffineTransform at) {
		// TODO Auto-generated method stub
		return null;
	}

	public PathIterator getPathIterator(final AffineTransform at, final double flatness) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean intersects(final Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean intersects(final double x, final double y, final double w, final double h) {
		// TODO Auto-generated method stub
		return false;
	}

}
