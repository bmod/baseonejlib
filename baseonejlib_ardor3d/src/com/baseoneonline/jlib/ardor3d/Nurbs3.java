package com.baseoneonline.jlib.ardor3d;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.baseoneonline.jlib.ardor3d.math.CoxDeBoor;

/**
 * TODO: Allow for unevenly spaced knots
 * 
 */
public class Nurbs3
{

	private ReadOnlyVector3[] cvs;
	private double[] knots;
	private final int degree = 3;
	private int order;
	private double maxKnotValue;

	public Nurbs3(final ReadOnlyVector3[] cvs)
	{
		setCVs(cvs);
	}

	private void setCVs(final ReadOnlyVector3[] cvs)
	{
		this.cvs = cvs;
		rebuild();
	}

	private void rebuild()
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

	public Vector3 eval(final double t, Vector3 store)
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
			v = CoxDeBoor.eval(knots, t * maxKnotValue, i, degree);
			valSum += v;
			// store += currentPoint * weight
			store.addLocal(cvs[i].multiply(v, tmp));
		}
		Vector3.releaseTempInstance(tmp);
		store.divideLocal(valSum);

		return store;
	}

}
