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

	public int getVertsU()
	{
		return vertsU;
	}

	public int getVertsV()
	{
		return vertsV;
	}

	public void setVertices(final ReadOnlyVector3[][] vtc)
	{
		this.vtc = vtc;
		recalculate();
	}

	private void setDegree(final int u, final int v)
	{
		degreeU = u;
		degreeV = v;
		recalculate();
	}

	private void recalculate()
	{
		vertsU = vtc.length;
		vertsV = vtc[0].length;
		spansU = vertsU - degreeU;
		spansV = vertsV - degreeV;
		orderU = degreeU + 1;
		orderV = degreeV + 1;
		generateKnots();
	}

	public void getPoint(final double u, final double v, final Vector3 store)
	{
		final double tu = u * spansU;
		final double tv = v * spansV;

		final int spanU = MathUtils.clamp((int) tu, 0, spansU - 1);
		final int spanV = MathUtils.clamp((int) tv, 0, spansV - 1);

		final double ttu = tu - spanU;
		final double ttv = tv - spanV;

		store.set(0, 0, 0);

		final double[] basisU = BSplineSurface3.basisFunc(ttu);
		final double[] basisV = BSplineSurface3.basisFunc(ttv);

		for (int ku = 0; ku < orderU; ku++)
		{

			for (int kv = 0; kv < orderV; kv++)
			{
				final ReadOnlyVector3 pt = vtc[ku + spanU][kv + spanV];

				final double b = basisU[ku] * basisV[kv];

				store.addLocal(pt.getX() * b, pt.getY() * b, pt.getZ() * b);
			}
		}
	}

	public Vector3 getTangentU(final double u, final double v, final Vector3 store)
	{
		final double tu = u * spansU;
		final double tv = v * spansV;

		final int spanU = MathUtils.clamp((int) tu, 0, spansU - 1);
		final int spanV = MathUtils.clamp((int) tv, 0, spansV - 1);

		final double ttu = tu - spanU;
		final double ttv = tv - spanV;

		store.zero();

		final double[] nu = BSplineSurface3.basisFunc(ttu);
		final double[] nv = BSplineSurface3.basisFunc(ttv);
		final double[] dnu = BSplineSurface3.curveFuncDerived(ttu);

		final double w1 = 0;
		final double w2 = 0;

		double val1x = 0;
		double val1y = 0;
		double val1z = 0;
		double val1w = 0;

		double val2x = 0;
		double val2y = 0;
		double val2z = 0;
		double val2w = 0;

		for (int ku = 0; ku < orderU; ku++)
		{
			for (int kv = 0; kv < orderV; kv++)
			{
				final ReadOnlyVector3 pt = vtc[ku + spanU][kv + spanV];

				val1x += pt.getX() * nu[ku] * nv[kv];
				val1y += pt.getY() * nu[ku] * nv[kv];
				val1z += pt.getZ() * nu[ku] * nv[kv];
				val1w += nu[ku] * nv[kv];

				val2x += pt.getX() * dnu[ku] * nv[kv];
				val2y += pt.getY() * dnu[ku] * nv[kv];
				val2z += pt.getZ() * dnu[ku] * nv[kv];
				val2w += dnu[ku] * nv[kv];

			}
		}

		val1x *= val2w;
		val1y *= val2w;
		val1z *= val2w;

		val2x *= val1w;
		val2y *= val1w;
		val2z *= val1w;

		val1w *= val1w;
		store.setX((val2x - val1x) / val1w);
		store.setY((val2y - val1y) / val1w);
		store.setZ((val2z - val1z) / val1w);

		return store;
	}

	private Vector3 getTangentV(final double u, final double v, final Vector3 store)
	{
		final double tu = u * spansU;
		final double tv = v * spansV;

		final int spanU = MathUtils.clamp((int) tu, 0, spansU - 1);
		final int spanV = MathUtils.clamp((int) tv, 0, spansV - 1);

		final double ttu = tu - spanU;
		final double ttv = tv - spanV;

		store.zero();

		final double[] nu = BSplineSurface3.basisFunc(ttu);
		final double[] nv = BSplineSurface3.basisFunc(ttv);
		final double[] dnv = BSplineSurface3.curveFuncDerived(ttv);

		double val1x = 0;
		double val1y = 0;
		double val1z = 0;
		double val1w = 0;

		double val2x = 0;
		double val2y = 0;
		double val2z = 0;
		double val2w = 0;

		for (int ku = 0; ku < orderU; ku++)
		{
			for (int kv = 0; kv < orderV; kv++)
			{
				final ReadOnlyVector3 pt = vtc[ku + spanU][kv + spanV];

				val1x += pt.getX() * nu[ku] * nv[kv];
				val1y += pt.getY() * nu[ku] * nv[kv];
				val1z += pt.getZ() * nu[ku] * nv[kv];
				val1w += nu[ku] * nv[kv];

				val2x += pt.getX() * nu[ku] * dnv[kv];
				val2y += pt.getY() * nu[ku] * dnv[kv];
				val2z += pt.getZ() * nu[ku] * dnv[kv];
				val2w += nu[ku] * dnv[kv];

			}
		}

		val1x *= val2w;
		val1y *= val2w;
		val1z *= val2w;

		val2x *= val1w;
		val2y *= val1w;
		val2z *= val1w;

		val1w *= val1w;
		store.setX((val2x - val1x) / val1w);
		store.setY((val2y - val1y) / val1w);
		store.setZ((val2z - val1z) / val1w);
		return store;
	}

	public void getAxes(final double u, final double v, final Vector3 axisU, final Vector3 axisV, final Vector3 normal)
	{
		getTangentU(u, v, axisU);
		getTangentV(u, v, axisV);
		axisV.cross(axisU, normal);
	}

	public void getNormal(final double u, final double v, final Vector3 store)
	{
		final Vector3 tanU = Vector3.fetchTempInstance();
		final Vector3 tanV = Vector3.fetchTempInstance();

		getAxes(u, v, tanU, tanV, store);

		Vector3.releaseTempInstance(tanU);
		Vector3.releaseTempInstance(tanV);
	}

	private static double[] basisFunc(final double t)
	{
		final double it = 1 - t;
		final double a = it * it * it / 6;
		final double b = (3 * t * t * t - 6 * t * t + 4) / 6;
		final double c = (-3 * t * t * t + 3 * t * t + 3 * t + 1) / 6;
		final double d = t * t * t / 6;
		return new double[] { a, b, c, d };
	}

	public static double bSplinePoint(final int k, final double t)
	{
		final double it = 1 - t;
		switch (k)
		{
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

	public static Vector3 bSplinePoint(final ReadOnlyVector3 a, final ReadOnlyVector3 b, final ReadOnlyVector3 c, final ReadOnlyVector3 d, final double t,
			final Vector3 store)
	{
		final double it = 1 - t;
		final double b0 = it * it * it / 6;
		final double b1 = (3 * t * t * t - 6 * t * t + 4) / 6;
		final double b2 = (-3 * t * t * t + 3 * t * t + 3 * t + 1) / 6;
		final double b3 = t * t * t / 6;

		store.set(0, 0, 0);

		store.addLocal(a.getX() * b0, a.getY() * b0, a.getZ() * b0);
		store.addLocal(b.getX() * b1, b.getY() * b1, b.getZ() * b1);
		store.addLocal(c.getX() * b2, c.getY() * b2, c.getZ() * b2);
		store.addLocal(d.getX() * b3, d.getY() * b3, d.getZ() * b3);

		return store;
	}

	private static double[] curveFuncDerived(final double t)
	{
		final double it = 1 - t;
		final double a = -(it * it) * .5;
		final double b = t * (-4 * 1 + 3 * 1 * t) * .5;
		final double c = t * (1 + 2 * t - 3 * t * t) * .5;
		final double d = t * t * .5;
		return new double[] { a, b, c, d };
	}

	private Vector3 bSplineVelocity(final ReadOnlyVector3 a, final ReadOnlyVector3 b, final ReadOnlyVector3 c, final ReadOnlyVector3 d, final double t,
			final Vector3 store)
	{

		final double it = 1 - t;
		final double b0 = it * it;
		final double b1 = 1 + 2 * t - 3 * t * t;

		store.set(0, 0, 0);

		final double x = (-(a.getX() * b0) + t * (-4 * b.getX() + 3 * b.getX() * t + d.getX() * t) + c.getX() * b1) * .5;
		final double y = (-(a.getX() * b0) + t * (-4 * b.getX() + 3 * b.getX() * t + d.getX() * t) + c.getX() * b1) * .5;
		final double z = (-(a.getX() * b0) + t * (-4 * b.getX() + 3 * b.getX() * t + d.getX() * t) + c.getX() * b1) * .5;

		return store.set(x, y, z);

	}

	private void generateKnots()
	{
		// resize if necessary
		numUKnots = vertsU + degreeU + 1;
		if (knotsU.length < numUKnots)
			knotsU = new double[numUKnots];

		int j;

		for (j = 0; j < numUKnots; j++)
		{
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

		for (j = 0; j < numVKnots; j++)
		{
			if (j <= degreeV)
				knotsV[j] = 0;
			else if (j < vertsV)
				knotsV[j] = j - degreeV + 1;
			else if (j >= vertsV)
				knotsV[j] = vertsV - degreeV + 1;
		}
	}

}