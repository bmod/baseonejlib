package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public class CatmullRom3 implements Curve3 {
	protected ReadOnlyVector3[] cvs;
	private boolean clamped;

	public CatmullRom3(ReadOnlyVector3[] cvs) {
		setCVs(cvs);
		if (cvs.length < 4)
			throw new IllegalArgumentException(
					"Requiring at least 4 control points, " + cvs.length
							+ " given.");
	}

	@Override
	public ReadOnlyVector3[] getCVS() {
		return cvs;
	}

	@Override
	public ReadOnlyVector3 getPoint(double t, Vector3 store) {
		// How many segments does this curve have
		int segCount = getSegmentCount();

		// Calculate current segment and u value
		double u = t * segCount;
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
	public ReadOnlyVector3 getVelocity(double t, Vector3 store) {
		// How many segments does this curve have
		int segCount = getSegmentCount();

		// Calculate current segment and u value
		double u = t * segCount;
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
	private int[] getAffectedCVS(int s, int segCount) {

		if (clamped) {

			if (s == 0)
				return new int[] { s + 0, s + 0, s + 1, s + 2 };
			else if (s == segCount - 1)
				return new int[] { s - 1, s + 0, s + 1, s + 1 };
			else
				return new int[] { s - 1, s + 0, s + 1, s + 2 };
		} else {
			return new int[] { s + 0, s + 1, s + 2, s + 3 };
		}
		// case Loop:
		// if (s == 0)
		// return new int[] { segCount - 1, s + 0, s + 1, s + 2 };
		// else if (s == segCount - 2)
		// return new int[] { s - 1, s + 0, s + 1, 0 };
		// else if (s == segCount - 1)
		// return new int[] { s - 1, s + 0, 0, 1 };
		// else
		// return new int[] { s - 1, s, s + 1, s + 2 };

	}

	private int getSegmentCount() {
		if (clamped)
			return cvs.length - 1;
		else
			return cvs.length - 3;
	}

	private double catmullRom(double a, double b, double c, double d, double t) {
		double b0 = (2 * b);
		double b1 = (-a + c) * t;
		double b2 = (2 * a - 5 * b + 4 * c - d) * (t * t);
		double b3 = (-a + 3 * b - 3 * c + d) * (t * t * t);
		return 0.5 * (b0 + b1 + b2 + b3);
	}

	private double catmullRomDerived(double a, double b, double c, double d,
			double t) {
		return 0.5 * (3 * t * t * (-a + 3 * b - 3 * c + d) + 2 * t
				* (2 * a - 5 * b + 4 * c - d) - a + c);
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
	public Matrix3 getOrientation(double t, ReadOnlyVector3 normal,
			Matrix3 store) {
		Vector3 tangent = Vector3.fetchTempInstance();
		Quaternion q = Quaternion.fetchTempInstance();

		getVelocity(t, tangent);
		q.lookAt(tangent, normal);
		store.set(q);

		Quaternion.releaseTempInstance(q);
		Vector3.releaseTempInstance(tangent);
		return store;
	}

	public Quaternion getCVOrientation(int index, ReadOnlyVector3 normal,
			Quaternion store) {
		Vector3 tangent = Vector3.fetchTempInstance();

		double t = (double) index / (double) cvs.length;

		getVelocity(t, tangent);

		store.lookAt(tangent, normal);

		Vector3.releaseTempInstance(tangent);
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
