package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public class BSpline3 extends Curve3 {

	public BSpline3(final ReadOnlyVector3[] cvs) {
		super(cvs);
		if (cvs.length < 4)
			throw new IllegalArgumentException(
					"Requiring at least 4 control points, " + cvs.length
							+ " given.");
	}

	@Override
	public Vector3 getPoint(final double t, final Vector3 store) {

		// How many segments does this curve have
		final int segCount = getSegmentCount();

		// Calculate current segment and u value
		final double u = t * segCount;
		double v = u % 1;
		int seg = (int) u;

		if (seg >= segCount) {
			v = 1;
			seg = segCount - 1;
		} else if (seg < 0) {
			v = 0;
			seg = 0;
		}

		// Seek out which cv's affect the current u value
		final int[] idc = getAffectedCVS(seg, segCount);
		final ReadOnlyVector3 a = cvs[idc[0]];
		final ReadOnlyVector3 b = cvs[idc[1]];
		final ReadOnlyVector3 c = cvs[idc[2]];
		final ReadOnlyVector3 d = cvs[idc[3]];

		return store.set(BSpline3.curveFunction(a.getX(), b.getX(), c.getX(),
				d.getX(), v), BSpline3.curveFunction(a.getY(), b.getY(),
				c.getY(), d.getY(), v), BSpline3.curveFunction(a.getZ(),
				b.getZ(), c.getZ(), d.getZ(), v));
	}

	@Override
	public Vector3 getVelocity(final double t, final Vector3 store) {
		// How many segments does this curve have
		final int segCount = getSegmentCount();

		// Calculate current segment and u value
		final double u = t * segCount;
		double v = u % 1;
		int seg = (int) u;

		if (seg >= segCount) {
			v = 1;
			seg = segCount - 1;
		} else if (seg < 0) {
			v = 0;
			seg = 0;
		}

		// Seek out which cv's affect the current u value
		final int[] idc = getAffectedCVS(seg, segCount);
		final ReadOnlyVector3 a = cvs[idc[0]];
		final ReadOnlyVector3 b = cvs[idc[1]];
		final ReadOnlyVector3 c = cvs[idc[2]];
		final ReadOnlyVector3 d = cvs[idc[3]];

		return store.set(
				BSpline3.curveFunctionDerived(a.getX(), b.getX(), c.getX(),
						d.getX(), v),
				BSpline3.curveFunctionDerived(a.getY(), b.getY(), c.getY(),
						d.getY(), v),
				BSpline3.curveFunctionDerived(a.getZ(), b.getZ(), c.getZ(),
						d.getZ(), v));

	}

	/**
	 * @param s
	 *            Current segment index
	 * @param segCount
	 *            The total amount of segments
	 * @return
	 */
	private int[] getAffectedCVS(final int s, final int segCount) {

		switch (mode) {
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

	private int getSegmentCount() {
		switch (mode) {
		case CLAMP:
			return cvs.length - 1;
		case LOOP:
			return cvs.length;
		default:
			return cvs.length - 3;
		}
	}

	private static double curveFunction(final double a, final double b,
			final double c, final double d, final double t) {
		final double it = 1 - t;
		final double t2 = t * t;
		final double t3 = t2 * t;
		final double b0 = it * it * it / 6;
		final double b1 = (3 * t3 - 6 * t2 + 4) / 6;
		final double b2 = (-3 * t3 + 3 * t2 + 3 * t + 1) / 6;
		final double b3 = t3 / 6;
		return b0 * a + b1 * b + b2 * c + b3 * d;
	}

	private static double curveFunctionDerived(final double a, final double b,
			final double c, final double d, final double t) {
		final double it = 1 - t;
		return (-(a * (it * it)) + t * (-4 * b + 3 * b * t + d * t) + c
				* (1 + 2 * t - 3 * t * t)) * .5;
	}

	@Override
	public Vector3 getNormal(final double t, final Vector3 store) {
		// How many segments does this curve have
		final int segCount = getSegmentCount();

		// Calculate current segment and u value
		final double u = t * segCount;
		double v = u % 1;
		int seg = (int) u;

		if (seg >= segCount) {
			v = 1;
			seg = segCount - 1;
		} else if (seg < 0) {
			v = 0;
			seg = 0;
		}

		// Seek out which cv's affect the current u value
		final int[] idc = getAffectedCVS(seg, segCount);
		final ReadOnlyVector3 a = normals[idc[0]];
		final ReadOnlyVector3 b = normals[idc[1]];
		final ReadOnlyVector3 c = normals[idc[2]];
		final ReadOnlyVector3 d = normals[idc[3]];

		return store.set(BSpline3.curveFunction(a.getX(), b.getX(), c.getX(),
				d.getX(), v), BSpline3.curveFunction(a.getY(), b.getY(),
				c.getY(), d.getY(), v), BSpline3.curveFunction(a.getZ(),
				b.getZ(), c.getZ(), d.getZ(), v));

	}

	public Transform getTransform(final ReadOnlyVector3 input,
			final ReadOnlyVector3 up, final Transform store) {
		final Vector3 pos = Vector3.fetchTempInstance();
		final Matrix3 rot = Matrix3.fetchTempInstance();
		final Vector3 tmp = Vector3.fetchTempInstance();
		final Vector3 vel = Vector3.fetchTempInstance();

		final double t = input.getZ();

		getPoint(t, pos);
		getVelocity(t, vel);
		rot.lookAt(vel, up);

		rot.getColumn(0, tmp);
		tmp.multiplyLocal(-input.getX());
		pos.addLocal(tmp);

		rot.getColumn(1, tmp);
		tmp.multiplyLocal(input.getY());
		pos.addLocal(tmp);

		store.setTranslation(pos);
		store.setRotation(rot);

		Vector3.releaseTempInstance(pos);
		Matrix3.releaseTempInstance(rot);
		Vector3.releaseTempInstance(tmp);
		Vector3.releaseTempInstance(vel);

		return store;
	}

}
