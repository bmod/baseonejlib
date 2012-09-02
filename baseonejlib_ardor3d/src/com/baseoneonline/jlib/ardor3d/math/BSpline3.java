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
		int segCount = getSegmentCount();

		// Calculate current segment and u value
		double u = t * segCount;
		double v = u % 1;
		int seg = (int) u;

		if (seg >= segCount)
		{
			v = 1;
			seg = segCount - 1;
		} else if (seg < 0)
		{
			v = 0;
			seg = 0;
		}

		// Seek out which cv's affect the current u value
		int[] idc = getAffectedCVS(seg, segCount);
		ReadOnlyVector3 a = cvs[idc[0]];
		ReadOnlyVector3 b = cvs[idc[1]];
		ReadOnlyVector3 c = cvs[idc[2]];
		ReadOnlyVector3 d = cvs[idc[3]];

		return store.set(bSpline(a.getX(), b.getX(), c.getX(), d.getX(), v),
				bSpline(a.getY(), b.getY(), c.getY(), d.getY(), v),
				bSpline(a.getZ(), b.getZ(), c.getZ(), d.getZ(), v));
	}

	@Override
	public Vector3 getVelocity(final double t, final Vector3 store)
	{
		// How many segments does this curve have
		int segCount = getSegmentCount();

		// Calculate current segment and u value
		double u = t * segCount;
		double v = u % 1;
		int seg = (int) u;

		if (seg >= segCount)
		{
			v = 1;
			seg = segCount - 1;
		} else if (seg < 0)
		{
			v = 0;
			seg = 0;
		}

		// Seek out which cv's affect the current u value
		int[] idc = getAffectedCVS(seg, segCount);
		ReadOnlyVector3 a = cvs[idc[0]];
		ReadOnlyVector3 b = cvs[idc[1]];
		ReadOnlyVector3 c = cvs[idc[2]];
		ReadOnlyVector3 d = cvs[idc[3]];

		return store.set(
				bSplineDerived(a.getX(), b.getX(), c.getX(), d.getX(), v),
				bSplineDerived(a.getY(), b.getY(), c.getY(), d.getY(), v),
				bSplineDerived(a.getZ(), b.getZ(), c.getZ(), d.getZ(), v));

	}

	private static double bSpline(double a, double b, double c, double d,
			double t)
	{
		final double it = 1 - t;
		double t2 = t * t;
		double t3 = t2 * t;
		final double b0 = (it * it * it / 6);
		final double b1 = ((3 * t3 - 6 * t2 + 4) / 6);
		final double b2 = ((-3 * t3 + 3 * t2 + 3 * t + 1) / 6);
		final double b3 = (t3 / 6);
		return b0 * a + b1 * b + b2 * c + b3 * d;
	}

	/**
	 * @param s
	 *            Current segment index
	 * @param segCount
	 *            The total amount of segments
	 * @return
	 */
	private int[] getAffectedCVS(int s, int segCount)
	{

		switch (mode)
		{
		case CLAMP:
			if (s == 0)
				return new int[] { s + 0, s + 0, s + 1, s + 2 };
			else if (s == segCount - 1)
				return new int[] { s - 1, s + 0, s + 1, s + 1 };
			else
				return new int[] { s - 1, s + 0, s + 1, s + 2 };
		case LOOP:
			if (s == 0)
				return new int[] { segCount - 1, s + 0, s + 1, s + 2 };
			else if (s == segCount - 2)
				return new int[] { s - 1, s + 0, s + 1, 0 };
			else if (s == segCount - 1)
				return new int[] { s - 1, s + 0, 0, 1 };
			else
				return new int[] { s - 1, s, s + 1, s + 2 };
		default:
			return new int[] { s + 0, s + 1, s + 2, s + 3 };
		}

	}

	private int getSegmentCount()
	{
		switch (mode)
		{
		case CLAMP:
			return cvs.length - 1;
		case LOOP:
			return cvs.length;
		default:
			return cvs.length - 3;
		}
	}

	private static double bSplineDerived(double a, double b, double c,
			double d, double t)
	{
		final double it = 1 - t;
		return (-(a * (it * it)) + t * (-4 * b + 3 * b * t + d * t) + c
				* (1 + 2 * t - 3 * t * t)) * .5;
	}

}
