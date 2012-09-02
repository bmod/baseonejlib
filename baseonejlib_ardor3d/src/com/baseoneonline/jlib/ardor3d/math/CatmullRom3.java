package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public class CatmullRom3 extends Curve3
{

	public CatmullRom3(ReadOnlyVector3[] cvs)
	{
		super(cvs);
		if (cvs.length < 4)
			throw new IllegalArgumentException(
					"Requiring at least 4 control points, " + cvs.length
							+ " given.");
	}

	@Override
	public ReadOnlyVector3 getPoint(double t, Vector3 store)
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

		// Seek out which cv's affect the current u value
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

		// Calculate actual point on curve
		final double t2 = v * v;
		final double t3 = t2 * v;

		store.setX(0.5 * ((2.0 * b.getX()) + (-a.getX() + c.getX()) * v
				+ (2.0 * a.getX() - 5.0 * b.getX() + 4.0 * c.getX() - d.getX())
				* t2 + (-a.getX() + 3.0 * b.getX() - 3.0 * c.getX() + d.getX())
				* t3));

		store.setY(0.5 * ((2.0 * b.getY()) + (-a.getY() + c.getY()) * v
				+ (2.0 * a.getY() - 5.0 * b.getY() + 4.0 * c.getY() - d.getY())
				* t2 + (-a.getY() + 3.0 * b.getY() - 3.0 * c.getY() + d.getY())
				* t3));

		store.setZ(0.5 * ((2.0 * b.getZ()) + (-a.getZ() + c.getZ()) * v
				+ (2.0 * a.getZ() - 5.0 * b.getZ() + 4.0 * c.getZ() - d.getZ())
				* t2 + (-a.getZ() + 3.0 * b.getZ() - 3.0 * c.getZ() + d.getZ())
				* t3));

		return store;
	}
}
