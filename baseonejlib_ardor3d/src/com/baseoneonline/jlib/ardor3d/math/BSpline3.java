package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public class BSpline3 implements Curve3 {

	protected ReadOnlyVector3[] normals;
	private ReadOnlyVector3 defaultNormal = Vector3.UNIT_Y;
	protected ReadOnlyVector3[] cvs;
	protected Mode mode = Mode.Open;

	public BSpline3(final ReadOnlyVector3[] cvs) {
		if (cvs.length < 4)
			throw new IllegalArgumentException(
					"Requiring at least 4 control points, " + cvs.length
							+ " given.");
		setCVs(cvs);
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
		case Clamp:
			if (s == 0)
				return new int[] { s + 0, s + 0, s + 1, s + 2 };
			else if (s == segCount - 1)
				return new int[] { s - 1, s + 0, s + 1, s + 1 };
			else
				return new int[] { s - 1, s + 0, s + 1, s + 2 };
		case Loop:
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

	public int getSegmentCount() {
		switch (mode) {
		case Clamp:
			return cvs.length - 1;
		case Loop:
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

	@Override
	public void setNormals(ReadOnlyVector3[] normals) {
		this.normals = normals;
	}

	@Override
	public void setDefaultNormal(ReadOnlyVector3 defaultNormal) {
		if (null == defaultNormal)
			throw new IllegalArgumentException("Cannot provide a null normal");
		this.defaultNormal = defaultNormal;
	}

	public ReadOnlyVector3 getDefaultNormal() {
		return defaultNormal;
	}

	@Override
	public ReadOnlyVector3 getCVNormal(int index) {
		if (null == normals)
			return defaultNormal;
		return normals[index];
	}

	/**
	 * Calculates the orientation at the provided time on the curve. Z-axis will
	 * point towards the curve end. Y-axis will be aligned with the normal.
	 * 
	 * @param t
	 * @param store
	 * @return
	 */
	@Override
	public Matrix3 getOrientation(double t, Matrix3 store) {
		Vector3 tangent = Vector3.fetchTempInstance();
		Vector3 normal = Vector3.fetchTempInstance();
		Quaternion q = Quaternion.fetchTempInstance();

		getVelocity(t, tangent);
		getNormal(t, normal);
		q.lookAt(tangent, normal);
		store.set(q);

		Quaternion.releaseTempInstance(q);
		Vector3.releaseTempInstance(tangent);
		Vector3.releaseTempInstance(normal);
		return store;
	}

	public Quaternion getCVOrientation(int index, Quaternion store) {
		Vector3 tangent = Vector3.fetchTempInstance();
		Vector3 normal = Vector3.fetchTempInstance();

		double t = (double) index / (double) cvs.length;

		getVelocity(t, tangent);
		// tangent.normalizeLocal();

		getNormal(t, normal);
		store.lookAt(tangent, normal);

		Vector3.releaseTempInstance(tangent);
		Vector3.releaseTempInstance(normal);
		return store;
	}

	@Override
	public double getLinearVelocity(double t) {
		Vector3 tmp = Vector3.fetchTempInstance();
		getVelocity(t, tmp);
		double velocity = tmp.length();
		Vector3.releaseTempInstance(tmp);
		return velocity;
	}

	@Override
	public Mode getMode() {
		return mode;
	}

	@Override
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	/**
	 * Replace this curve's control vertices.
	 * 
	 * @param cvs
	 *            The new set of control vertices.
	 */
	@Override
	public void setCVs(ReadOnlyVector3[] cvs) {
		this.cvs = cvs;
	}

	/**
	 * @param i
	 *            Index of the control vertex to retrieve.
	 * @return Instance of a control vertex used by this curve.
	 */
	@Override
	public ReadOnlyVector3 getCV(int i) {
		return cvs[i];
	}

	/**
	 * @return The number of control vertices in this curve.
	 */
	@Override
	public int getCVCount() {
		return cvs.length;
	}

}
