package com.baseoneonline.java.math;

/**
 * 
 * Shamelessly ripped from <a href=
 * "http://nccastaff.bournemouth.ac.uk/jmacey/RobTheBloke/www/opengl_programming.html"
 * >http://nccastaff.bournemouth.ac.uk/jmacey/RobTheBloke/www/opengl_programming
 * .html</a>
 * 
 * @author bask
 * 
 */
public class NurbsCurve implements Curve
{

	private final Vec2f[] pts;
	private final int degree;
	private final int order;
	private final int num_knots;
	private float maxKnotValue;
	private float[] knots;

	public NurbsCurve(final Vec2f[] pts)
	{
		this(pts, 3);
	}

	public NurbsCurve(final Vec2f[] pts, final int degree)
	{
		this.pts = pts;
		this.degree = degree;
		order = degree + 1;
		num_knots = pts.length + order;
		createKnots();
	}

	private void createKnots()
	{
		knots = new float[num_knots];
		float v = 0;
		for (int i = 0; i < num_knots; i++)
		{
			knots[i] = v;
			if (i >= degree && i < num_knots - degree - 1)
			{
				v += 1;
			}
		}
		maxKnotValue = v;
	}

	public Vec2f getCV(final int i)
	{
		return pts[i];
	}

	public Vec2f getPoint(final float t, Vec2f store)
	{
		if (null == store)
			store = new Vec2f();

		if (t <= 0)
			return store.set(pts[0]);
		if (t >= 1)
			return store.set(pts[pts.length - 1]);

		store.zero();
		float valSum = 0;
		final int len = pts.length;
		for (int i = 0; i < len; i++)
		{
			final float v = coxDeBoor(t * maxKnotValue, i, degree);
			valSum += v;
			final Vec2f p = pts[i];
			store.addLocal(p.mult(v));
		}
		store.divideLocal(valSum);
		// System.out.println(store);
		return store;
	}

	public float getAngle(final float t)
	{
		final float precision = .00001f;
		float t1 = t;
		float t2 = t + precision;
		if (t2 > 1)
		{
			t1 = t - precision;
			t2 = t;
		}
		final Vec2f p1 = getPoint(t1, null);
		final Vec2f p2 = getPoint(t2, null);
		return BMath.atan2(p2.y - p1.y, p2.x - p1.x);
	}

	private float coxDeBoor(final float t, final int k, final int deg)
	{
		float b1;
		float b2;

		if (deg == 0)
		{
			if (knots[k] <= t && t <= knots[k + 1])
			{
				return 1.0f;
			}
			return 0.0f;
		}

		if (knots[k + deg] != knots[k])
			b1 = ((t - knots[k]) / (knots[k + deg] - knots[k])) * coxDeBoor(t, k, deg - 1);
		else
			b1 = 0.0f;

		if (knots[k + deg + 1] != knots[k + 1])
			b2 = ((knots[k + deg + 1] - t) / (knots[k + deg + 1] - knots[k + 1])) * coxDeBoor(t, k + 1, deg - 1);
		else
			b2 = 0.0f;

		return b1 + b2;

	}

	@Override
	public int getNumCVs()
	{
		return pts.length;
	}

}
