package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Quaternion;
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
	private ReadOnlyVector3 defaultNormal = Vector3.UNIT_Y;
	protected ReadOnlyVector3[] cvs;
	protected Mode mode = Mode.Open;

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
		if (mode == Mode.Clamp) {
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
	public ReadOnlyVector3 getVelocity(double t, Vector3 store) {
		return store;
	}

	@Override
	public Vector3 getNormal(double t, Vector3 store) {
		return store.set(getDefaultNormal());
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
