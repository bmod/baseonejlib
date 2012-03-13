package com.baseoneonline.java.math;

public class BSpline
{
	public static double cubic(double a, double b, double c, double d, double t)
	{
		double it = 1 - t;
		// blend functions
		double b0 = it * it * it / 6;
		double b1 = (3 * t * t * t - 6 * t * t + 4) / 6;
		double b2 = (-3 * t * t * t + 3 * t * t + 3 * t + 1) / 6;
		double b3 = t * t * t / 6;

		return b0 * a + b1 * b + b2 * c + b3 * d;
	}

	public static double cubicVelocity(double a, double b, double c, double d,
			double t)
	{
		double it = 1 - t;
		// blend functions
		double b0 = it * it;
		double b1 = 1 + 2 * t - 3 * t * t;

		return (-(a * b0) + t * (-4 * b + 3 * b * t + d * t) + c * b1) * .5;
	}
}
