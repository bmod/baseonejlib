package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.baseoneonline.java.math.CurveAlgorithms;

/**
 * A clamped NURBS curve.
 * 
 */
public class Nurbs3 extends Curve3
{

	private double[] knots;
	private final int degree = 3;
	private int order;
	private double maxKnotValue;

	public Nurbs3(final ReadOnlyVector3[] cvs)
	{
		super(cvs);
	}

	@Override
	public void setCVs(final ReadOnlyVector3[] cvs)
	{
		super.setCVs(cvs);
		rebuild();
	}

	private void rebuild()
	{
		if (mode == Clamped)
		{
			order = degree + 1;
			final int numKnots = cvs.length + order;

			knots = new double[numKnots];
			float v = 0;
			for (int i = 0; i < numKnots; i++)
			{
				knots[i] = v;
				if (i >= degree && i < numKnots - degree - 1)
					v += 1;
			}
			maxKnotValue = v;
		} else
		{
			order = degree + 1;
			final int numKnots = cvs.length + order;

			knots = new double[numKnots];
			float v = 0;
			for (int i = 0; i < numKnots; i++)
			{
				knots[i] = v;
				if (i >= degree && i < numKnots - degree - 1)
					v += 1;
			}
			maxKnotValue = v;
		}
	}

	@Override
	public Vector3 getPoint(final double t, Vector3 store)
	{
		if (null == store)
			store = new Vector3();

		if (t <= 0)
			return store.set(cvs[0]);
		if (t >= 1)
			return store.set(cvs[cvs.length - 1]);

		store.zero();

		double valSum = 0;
		double v = 0;
		final Vector3 tmp = Vector3.fetchTempInstance();
		for (int i = 0; i < cvs.length; i++)
		{
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

}
