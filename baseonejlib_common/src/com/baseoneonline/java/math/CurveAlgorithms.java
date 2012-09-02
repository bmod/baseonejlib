package com.baseoneonline.java.math;

public class CurveAlgorithms
{
	public static double coxDeBoor(final double[] knots, final double t,
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
					* coxDeBoor(knots, t, k, deg - 1);
		else
			b1 = 0.0f;

		if (knots[k + deg + 1] != knots[k + 1])
			b2 = ((knots[k + deg + 1] - t) / (knots[k + deg + 1] - knots[k + 1]))
					* coxDeBoor(knots, t, k + 1, deg - 1);
		else
			b2 = 0.0f;

		return b1 + b2;

	}

	public static double bSplinePoint(double a, double b, double c, double d,
			double t)
	{
		double it = 1 - t;

		double b0 = it * it * it / 6;
		double b1 = (3 * t * t * t - 6 * t * t + 4) / 6;
		double b2 = (-3 * t * t * t + 3 * t * t + 3 * t + 1) / 6;
		double b3 = t * t * t / 6;

		return b0 * a + b1 * b + b2 * c + b3 * d;
	}

	public static double bSplineVelocity(double a, double b, double c,
			double d, double t)
	{
		double it = 1 - t;

		double b0 = it * it;
		double b1 = 1 + 2 * t - 3 * t * t;

		return (-(a * b0) + t * (-4 * b + 3 * b * t + d * t) + c * b1) * .5;
	}
}
