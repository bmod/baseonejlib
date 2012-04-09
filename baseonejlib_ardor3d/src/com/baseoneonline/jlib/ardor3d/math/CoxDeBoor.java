package com.baseoneonline.jlib.ardor3d.math;

public class CoxDeBoor
{

	private CoxDeBoor()
	{

	}

	public static double eval(final double[] knots, final double t,
			final int k, final int deg)
	{
		double b1;
		double b2;

		if (deg == 0)
		{
			if (knots[k] <= t && t <= knots[k + 1])
			{
				return 1.0f;
			}
			return 0.0f;
		}

		if (knots[k + deg] != knots[k])
			b1 = ((t - knots[k]) / (knots[k + deg] - knots[k]))
					* eval(knots, t, k, deg - 1);
		else
			b1 = 0.0f;

		if (knots[k + deg + 1] != knots[k + 1])
			b2 = ((knots[k + deg + 1] - t) / (knots[k + deg + 1] - knots[k + 1]))
					* eval(knots, t, k + 1, deg - 1);
		else
			b2 = 0.0f;

		return b1 + b2;

	}
}
