package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public class BSplineSurface3 {

	private ReadOnlyVector3[][] vtc;
	private int vertsU;
	private int vertsV;
	private int degreeU;
	private int degreeV;
	private int orderU;
	private int orderV;
	private int numUKnots;
	private int numVKnots;
	private int spansU;
	private int spansV;
	private double[] knotsU = new double[0];
	private double[] knotsV = new double[0];

	public BSplineSurface3(final ReadOnlyVector3[][] vtc) {
		setVertices(vtc);
		setDegree(3, 3);
	}

	public int getVertsU() {
		return vertsU;
	}

	public int getVertsV() {
		return vertsV;
	}

	public void setVertices(final ReadOnlyVector3[][] vtc) {
		this.vtc = vtc;
		recalculate();
	}

	private void setDegree(int u, int v) {
		this.degreeU = u;
		this.degreeV = v;
		recalculate();
	}

	private void recalculate() {
		vertsU = vtc.length;
		vertsV = vtc[0].length;
		spansU = vertsU - degreeU;
		spansV = vertsV - degreeV;
		orderU = degreeU + 1;
		orderV = degreeV + 1;
		generateKnots();
	}

	public void getPoint(final double u, final double v, final Vector3 store) {
		double tu = u * spansU;
		double tv = v * spansV;

		int spanU = MathUtils.clamp((int) tu, 0, spansU - 1);
		int spanV = MathUtils.clamp((int) tv, 0, spansV - 1);

		double ttu = tu - spanU;
		double ttv = tv - spanV;

		store.set(0, 0, 0);

		for (int ku = 0; ku < orderU; ku++) {

			for (int kv = 0; kv < orderV; kv++) {
				ReadOnlyVector3 pt = vtc[ku + spanU][kv + spanV];

				double bu = bSplinePoint(ku, ttu);
				double bv = bSplinePoint(kv, ttv);
				double b = bu * bv;

				store.addLocal(pt.getX() * b, pt.getY() * b, pt.getZ() * b);
			}
		}
	}

	public void getTangentU(double u, double v, Vector3 store) {
		double tu = u * spansU;
		double tv = v * spansV;

		int spanU = MathUtils.clamp((int) tu, 0, spansU - 1);
		int spanV = MathUtils.clamp((int) tv, 0, spansV - 1);

		double ttu = tu - spanU;
		double ttv = tv - spanV;

		store.set(0, 0, 0);

		Vector3 tmp = Vector3.fetchTempInstance();

		for (int ku = 0; ku < orderU; ku++) {
			bSplineVelocity(vtc[spanU + ku][spanV + 0],
					vtc[spanU + ku][spanV + 1], vtc[spanU + ku][spanV + 2],
					vtc[spanU + ku][spanV + 3], ttu, tmp);
			store.addLocal(tmp);
		}
		Vector3.releaseTempInstance(tmp);
	}

	public void getNormal(final double u, final double v, final Vector3 store) {
		getTangentU(u, v, store);
		store.set(0, 1, 0);
	}

	public static double bSplinePoint(int k, double t) {
		double it = 1 - t;
		switch (k) {
		case 0:
			return it * it * it / 6;
		case 1:
			return (3 * t * t * t - 6 * t * t + 4) / 6;
		case 2:
			return (-3 * t * t * t + 3 * t * t + 3 * t + 1) / 6;
		case 3:
			return t * t * t / 6;
		default:
			throw new RuntimeException("Wrong k value (0-4 ex): " + k);
		}
	}

	public static Vector3 bSplinePoint(ReadOnlyVector3 a, ReadOnlyVector3 b,
			ReadOnlyVector3 c, ReadOnlyVector3 d, double t, Vector3 store) {
		double it = 1 - t;
		double b0 = it * it * it / 6;
		double b1 = (3 * t * t * t - 6 * t * t + 4) / 6;
		double b2 = (-3 * t * t * t + 3 * t * t + 3 * t + 1) / 6;
		double b3 = t * t * t / 6;

		store.set(0, 0, 0);

		store.addLocal(a.getX() * b0, a.getY() * b0, a.getZ() * b0);
		store.addLocal(b.getX() * b1, b.getY() * b1, b.getZ() * b1);
		store.addLocal(c.getX() * b2, c.getY() * b2, c.getZ() * b2);
		store.addLocal(d.getX() * b3, d.getY() * b3, d.getZ() * b3);

		return store;
	}

	private Vector3 bSplineVelocity(ReadOnlyVector3 a, ReadOnlyVector3 b,
			ReadOnlyVector3 c, ReadOnlyVector3 d, double t, Vector3 store) {

		double it = 1 - t;
		double b0 = it * it;
		double b1 = 1 + 2 * t - 3 * t * t;

		store.set(0, 0, 0);

		double x = (-(a.getX() * b0) + t
				* (-4 * b.getX() + 3 * b.getX() * t + d.getX() * t) + c.getX()
				* b1) * .5;
		double y = (-(a.getX() * b0) + t
				* (-4 * b.getX() + 3 * b.getX() * t + d.getX() * t) + c.getX()
				* b1) * .5;
		double z = (-(a.getX() * b0) + t
				* (-4 * b.getX() + 3 * b.getX() * t + d.getX() * t) + c.getX()
				* b1) * .5;

		return store.set(x, y, z);

	}

	private void generateKnots() {
		// resize if necessary
		numUKnots = vertsU + degreeU + 1;
		if (knotsU.length < numUKnots)
			knotsU = new double[numUKnots];

		int j;

		for (j = 0; j < numUKnots; j++) {
			if (j <= degreeU)
				knotsU[j] = 0;
			else if (j < vertsU)
				knotsU[j] = j - degreeU + 1;
			else if (j >= vertsU)
				knotsU[j] = vertsU - degreeU + 1;
		}

		numVKnots = vertsV + degreeV + 1;
		if (knotsV.length < numVKnots)
			knotsV = new double[numVKnots];

		for (j = 0; j < numVKnots; j++) {
			if (j <= degreeV)
				knotsV[j] = 0;
			else if (j < vertsV)
				knotsV[j] = j - degreeV + 1;
			else if (j >= vertsV)
				knotsV[j] = vertsV - degreeV + 1;
		}
	}

	private double splineBlend(final int i, final int k,
			final boolean useDepth, final double t) {
		double ret_val;

		// Do this just to make the maths traceable with the std algorithm
		final double[] u = useDepth ? knotsV : knotsU;

		if (k == 1) {
			ret_val = u[i] <= t && t < u[i + 1] ? 1 : 0;
		} else {
			final double b1 = splineBlend(i, k - 1, useDepth, t);
			final double b2 = splineBlend(i + 1, k - 1, useDepth, t);

			final double d1 = u[i + k - 1] - u[i];
			final double d2 = u[i + k] - u[i + 1];

			double e, f;

			e = b1 != 0 ? (t - u[i]) / d1 * b1 : 0;
			f = b2 != 0 ? (u[i + k] - t) / d2 * b2 : 0;

			ret_val = e + f;
		}

		return ret_val;
	}

}