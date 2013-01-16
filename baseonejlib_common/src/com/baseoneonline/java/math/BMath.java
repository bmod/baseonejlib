package com.baseoneonline.java.math;

public class BMath
{

	/** A "close to zero" double epsilon value for use */
	public static float EPSILON;

	static
	{
		float machEps = 1.0f;
		do
			machEps /= 2.0f;
		while ((float) (1.0 + (machEps / 2.0)) != 1.0);
		EPSILON = machEps;

	}

	public final static float inverseSqrt(float x)
	{
		final double xhalves = 0.5d * x;
		x = (float) Double.longBitsToDouble(0x5FE6EB50C7B537AAl - (Double
				.doubleToRawLongBits(x) >> 1));
		return (float) (x * (1.5d - xhalves * x * x));
	}

	public static float atan2(final float y, final float x)
	{
		return (float) java.lang.Math.atan2(y, x);
	}
}
