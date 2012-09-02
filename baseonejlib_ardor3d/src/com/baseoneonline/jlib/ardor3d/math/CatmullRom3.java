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

		return store.set(catmullRom(a.getX(), b.getX(), c.getX(), d.getX(), v),
				catmullRom(a.getY(), b.getY(), c.getY(), d.getY(), v),
				catmullRom(a.getZ(), b.getZ(), c.getZ(), d.getZ(), v));
	}

	@Override
	public ReadOnlyVector3 getVelocity(double t, Vector3 store)
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
				catmullRomDerived(a.getX(), b.getX(), c.getX(), d.getX(), v),
				catmullRomDerived(a.getY(), b.getY(), c.getY(), d.getY(), v),
				catmullRomDerived(a.getZ(), b.getZ(), c.getZ(), d.getZ(), v));
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

	private double catmullRom(double a, double b, double c, double d, double t)
	{
		return 0.5 * ((2 * b) + (-a + c) * t + (2 * a - 5 * b + 4 * c - d)
				* (t * t) + (-a + 3 * b - 3 * c + d) * (t * t * t));
	}

	private double catmullRomDerived(double a, double b, double c, double d,
			double t)
	{
		return 0.5 * (3 * t * t * (-a + 3 * b - 3 * c + d) + 2 * t
				* (2 * a - 5 * b + 4 * c - d) - a + c);
	}

}
