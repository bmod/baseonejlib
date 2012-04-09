package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

/**
 * Represents a curve that evaluates like a 3rd degree NURBS curve with evenly
 * spaced knots.
 * 
 */
public class BSplineCurve
{

	private ReadOnlyVector3[] points;
	private int segmentCount = 0;

	public BSplineCurve(final ReadOnlyVector3[] points)
	{
		setPoints(points);
	}

	private void setPoints(final ReadOnlyVector3[] points)
	{
		if (points.length < 4)
			throw new IllegalArgumentException("Needs at least 4 points");
		this.points = points;
		segmentCount = points.length - 3;
	}

	public Vector3 getPoint(final double u, Vector3 store)
	{
		if (null == store)
			store = new Vector3();

		final int segment = (int) Math.floor(u);

		final double t = u % 1;

		final ReadOnlyVector3 a = points[segment];
		final ReadOnlyVector3 b = points[segment + 1];
		final ReadOnlyVector3 c = points[segment + 2];
		final ReadOnlyVector3 d = segment >= segmentCount ? points[segment + 2]
				: points[segment + 3];

		BSpline.getPoint(a, b, c, d, t, store);

		return store;
	}

	public Vector3 getVelocity(final double u, Vector3 store)
	{
		if (null == store)
			store = new Vector3();

		final int segment = (int) Math.floor(u);

		final double t = u % 1;

		final ReadOnlyVector3 a = points[segment];
		final ReadOnlyVector3 b = points[segment + 1];
		final ReadOnlyVector3 c = points[segment + 2];
		final ReadOnlyVector3 d = segment >= segmentCount ? points[segment + 2]
				: points[segment + 1];

		BSpline.getVelocity(a, b, c, d, t, store);

		return store;
	}

	public int getSegmentCount()
	{
		return segmentCount;
	}

}
