package com.baseoneonline.java.math;

/**
 * 
 * Shamelessly ripped from <a href="http://nccastaff.bournemouth.ac.uk/jmacey/RobTheBloke/www/opengl_programming.html"
 * >http://nccastaff.bournemouth.ac.uk/jmacey/RobTheBloke/www/opengl_programming
 * .html</a>
 * 
 * @author bask
 * 
 */
public class NurbsCurve implements Curve {

	private final Vec2f[] pts;
	private final int degree;
	private final int order;
	private final int num_knots;
	private float[] knots;
	private float largestKnot;

	public NurbsCurve(Vec2f[] pts) {
		this(pts, 3);
	}

	public NurbsCurve(final Vec2f[] pts, int degree) {
		this.pts = pts;
		this.degree = degree;
		order = degree + 1;
		num_knots = pts.length + order;
		createKnots();
	}

	private void createKnots() {
		knots = new float[num_knots];
		float v = 0;
		for (int i = 0; i < num_knots; i++) {
			knots[i] = v;
			if (i >= degree && i < num_knots - degree - 1) {
				v += 1;
			}

		}
		largestKnot = v;
	}

	public Vec2f getCV(final int i) {
		return pts[i];
	}

	public Vec2f getPoint(float t, Vec2f store) {
		if (null == store)
			store = new Vec2f();
		if (t >= 1) {
			store.set(pts[pts.length - 1]);
			return store;
		}
		// if (t == .5f)
		// t = .5000001f;
		if (t <= 0) {
			store.set(pts[0]);
			return store;
		}
		store.zero();
		float tot = 0;
		for (int i = 0; i < pts.length; i++) {
			final float v = coxDeBoor(t * largestKnot, i, order);
			tot += v;

			if (v > 0.001f)
				store.addLocal(pts[i].mult(v));
		}
		System.out.println(tot);
		return store;
	}

	private float coxDeBoor(final float t, final int i, final int k) {
		if (k == 1) {
			if (knots[i] <= t && t <= knots[i + 1]) {
				return 1.0f;
			}
			return 0.0f;
		}
		final float Den1 = knots[i + k - 1] - knots[i];
		final float Den2 = knots[i + k] - knots[i + 1];
		float Eq1 = 0, Eq2 = 0;
		if (Den1 > 0) {
			Eq1 = ((t - knots[i]) / Den1) * coxDeBoor(t, i, k - 1);
		}
		if (Den2 > 0) {
			Eq2 = (knots[i + k] - t) / Den2 * coxDeBoor(t, i + 1, k - 1);
		}
		return Eq1 + Eq2;
	}

	@Override
	public int getNumCVs() {
		// TODO Auto-generated method stub
		return 0;
	}

}
