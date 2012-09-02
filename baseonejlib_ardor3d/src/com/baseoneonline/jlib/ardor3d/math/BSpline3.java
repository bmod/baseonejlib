package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public class BSpline3 extends Curve3
{

	public BSpline3(ReadOnlyVector3[] cvs)
	{
		super(cvs);
		if (cvs.length < 4)
			throw new IllegalArgumentException(
					"Requiring at least 4 control points, " + cvs.length
							+ " given.");
	}

	@Override
	public Vector3 getPoint(final double t, final Vector3 store)
	{
		// How many segments does this curve have
		int segments;
		switch (mode)
		{
		case Clamped:
			segments = cvs.length - 1;
			break;
		case Loop:
			segments = cvs.length;
			break;
		default:
			segments = cvs.length - 3;
		}

		// Calculate current segment and u value
		double u = t * segments;
		double v = u % 1;
		int segment = (int) u;
		if (t >= 1)
		{
			v = 1;
			segment = segments - 1;
		} else if (t <= 0)
		{
			v = 0;
			segment = 0;
		}

		// Find corresponding cvs
		ReadOnlyVector3 a;
		ReadOnlyVector3 b;
		ReadOnlyVector3 c;
		ReadOnlyVector3 d;

		switch (mode)
		{
		case Clamped:
			if (segment == 0)
			{
				a = cvs[segment + 0];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[segment + 2];
			} else if (segment == segments - 1)
			{
				a = cvs[segment - 1];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[segment + 1];
			} else
			{
				a = cvs[segment - 1];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[segment + 2];
			}
			break;

		case Loop:
			if (segment == 0)
			{
				a = cvs[segments - 1];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[segment + 2];
			} else if (segment == segments - 2)
			{
				a = cvs[segment - 1];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[0];

			} else if (segment == segments - 1)
			{
				a = cvs[segment - 1];
				b = cvs[segment + 0];
				c = cvs[0];
				d = cvs[1];
			} else
			{
				a = cvs[segment - 1];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[segment + 2];
			}
			break;

		default:
			a = cvs[segment + 0];
			b = cvs[segment + 1];
			c = cvs[segment + 2];
			d = cvs[segment + 3];
		}

		// Do actual point calculation
		final double it = 1 - v;
		final double b0 = it * it * it / 6;
		final double b1 = (3 * v * v * v - 6 * v * v + 4) / 6;
		final double b2 = (-3 * v * v * v + 3 * v * v + 3 * v + 1) / 6;
		final double b3 = v * v * v / 6;

		store.setX(b0 * a.getX() + b1 * b.getX() + b2 * c.getX() + b3
				* d.getX());
		store.setY(b0 * a.getY() + b1 * b.getY() + b2 * c.getY() + b3
				* d.getY());
		store.setZ(b0 * a.getZ() + b1 * b.getZ() + b2 * c.getZ() + b3
				* d.getZ());

		return store;
	}

	public Vector3 getVelocity(final double t, final Vector3 store)
	{
		// How many segments does this curve have
		int segments;
		switch (mode)
		{
		case Clamped:
			segments = cvs.length - 1;
			break;
		case Loop:
			segments = cvs.length;
			break;
		default:
			segments = cvs.length - 3;
		}

		// Calculate current segment and u value
		double u = t * segments;
		double v = u % 1;
		int segment = (int) u;
		if (t >= 1)
		{
			v = 1;
			segment = segments - 1;
		} else if (t <= 0)
		{
			v = 0;
			segment = 0;
		}

		// Find corresponding cvs
		ReadOnlyVector3 a;
		ReadOnlyVector3 b;
		ReadOnlyVector3 c;
		ReadOnlyVector3 d;

		switch (mode)
		{
		case Clamped:
			if (segment == 0)
			{
				a = cvs[segment + 0];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[segment + 2];
			} else if (segment == segments - 1)
			{
				a = cvs[segment - 1];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[segment + 1];
			} else
			{
				a = cvs[segment - 1];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[segment + 2];
			}
			break;

		case Loop:
			if (segment == 0)
			{
				a = cvs[segments - 1];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[segment + 2];
			} else if (segment == segments - 2)
			{
				a = cvs[segment - 1];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[0];

			} else if (segment == segments - 1)
			{
				a = cvs[segment - 1];
				b = cvs[segment + 0];
				c = cvs[0];
				d = cvs[1];
			} else
			{
				a = cvs[segment - 1];
				b = cvs[segment + 0];
				c = cvs[segment + 1];
				d = cvs[segment + 2];
			}
			break;

		default:
			a = cvs[segment + 0];
			b = cvs[segment + 1];
			c = cvs[segment + 2];
			d = cvs[segment + 3];
		}

		final double it = 1 - v;
		final double b0 = it * it;
		final double b1 = 1 + 2 * v - 3 * v * v;

		store.setX((-(a.getX() * b0) + v
				* (-4 * b.getX() + 3 * b.getX() * v + d.getX() * v) + c.getX()
				* b1) * .5);
		store.setY((-(a.getY() * b0) + v
				* (-4 * b.getY() + 3 * b.getY() * v + d.getY() * v) + c.getY()
				* b1) * .5);
		store.setZ((-(a.getZ() * b0) + v
				* (-4 * b.getZ() + 3 * b.getZ() * v + d.getZ() * v) + c.getZ()
				* b1) * .5);

		return store;
	}

}
