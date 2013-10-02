package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.baseoneonline.java.math.CurveAlgorithms;

/**
 * A clamped NURBS curve.
 * 
 */
public class Nurbs3 implements Curve3 {

	private double[] knots;
	private final int degree = 3;
	private int order;
	private double maxKnotValue;
	protected ReadOnlyVector3[] normals;
	private final ReadOnlyVector3 defaultNormal = Vector3.UNIT_Y;
	protected ReadOnlyVector3[] cvs;
	private boolean clamped;

	public Nurbs3(final ReadOnlyVector3[] cvs) {
		setCVs(cvs);
	}

	public ReadOnlyVector3[] getCvs() {
		return cvs;
	}

	@Override
	public void setCVs(final ReadOnlyVector3[] cvs) {
		this.cvs = cvs;
		rebuild();
	}

	private void rebuild() {
		if (clamped) {
			order = degree + 1;
			final int numKnots = cvs.length + order;

			knots = new double[numKnots];
			float v = 0;
			for (int i = 0; i < numKnots; i++) {
				knots[i] = v;
				if (i >= degree && i < numKnots - degree - 1)
					v += 1;
			}
			maxKnotValue = v;
		} else {
			order = degree + 1;
			final int numKnots = cvs.length + order;

			knots = new double[numKnots];
			float v = 0;
			for (int i = 0; i < numKnots; i++) {
				knots[i] = v;
				if (i >= degree && i < numKnots - degree - 1)
					v += 1;
			}
			maxKnotValue = v;
		}
	}

	@Override
	public Vector3 getPoint(final double t, Vector3 store) {
		if (t <= 0)
			return store.set(cvs[0]);
		if (t >= 1)
			return store.set(cvs[cvs.length - 1]);

		store.zero();

		double valSum = 0;
		double v = 0;
		final Vector3 tmp = Vector3.fetchTempInstance();
		for (int i = 0; i < cvs.length; i++) {
			v = CurveAlgorithms.coxDeBoor(knots, t * maxKnotValue, i, degree);
			valSum += v;
			tmp.set(cvs[i]);
			tmp.multiplyLocal(v);
			store.addLocal(tmp);
		}
		Vector3.releaseTempInstance(tmp);
		store.divideLocal(valSum);

		return store;
	}

	@Override
	public ReadOnlyVector3 getTangent(double t, Vector3 store) {
		return store;
	}

	public ReadOnlyVector3 getDefaultNormal() {
		return defaultNormal;
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

		getTangent(t, tangent);
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

		getTangent(t, tangent);
		// tangent.normalizeLocal();

		store.lookAt(tangent, normal);

		Vector3.releaseTempInstance(tangent);
		return store;
	}

	@Override
	public double getLinearVelocity(double t) {
		Vector3 tmp = Vector3.fetchTempInstance();
		getTangent(t, tmp);
		double velocity = tmp.length();
		Vector3.releaseTempInstance(tmp);
		return velocity;
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

	public void setClamped(boolean clamped) {
		this.clamped = clamped;
	}

	@Override
	public ReadOnlyVector3[] getCVS() {
		return cvs;
	}

	@Override
	public void getTransform(double t, ReadOnlyVector3 normal, Transform store) {
		// TODO Auto-generated method stub

	}

}
