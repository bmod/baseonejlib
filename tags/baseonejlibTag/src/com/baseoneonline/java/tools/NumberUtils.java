package com.baseoneonline.java.tools;

import java.awt.geom.Point2D;

public class NumberUtils {
	public static float limit(final float min, final float max, final float n) {
		if (n < min)
			return min;
		if (n > max)
			return max;
		return n;
	}

	public static float wrap(final float min, final float max, float n) {
		final float d = max - min;
		while (n < min) {
			n += d;
		}
		while (n > max) {
			n -= d;
		}
		return n;
	}
	
	public static double distanceToLine(final Point2D p1, final Point2D p2,
			final Point2D point) {

		final double xDelta = p2.getX() - p1.getX();
		final double yDelta = p2.getY() - p1.getY();

		if ((xDelta == 0) && (yDelta == 0)) {
			throw new IllegalArgumentException(
					"p1 and p2 cannot be the same point");
		}

		final double u =
				((point.getX() - p1.getX()) * xDelta + (point.getY() - p1.getY())
						* yDelta)
						/ (xDelta * xDelta + yDelta * yDelta);

		final Point2D closestPoint;
		if (u < 0) {
			closestPoint = p1;
		}
		else
			if (u > 1) {
				closestPoint = p2;
			}
			else {
				closestPoint =
						new Point2D.Double(p1.getX() + u * xDelta, p1.getY()
								+ u * yDelta);
			}

		return closestPoint.distance(point);
	}
}