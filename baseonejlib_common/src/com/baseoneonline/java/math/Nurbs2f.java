package com.baseoneonline.java.math;

/**
 * 
 */
public class Nurbs2f {
	public enum Mode {
		Open, Loop, Clamp
	}

	private Vec2f[] cvs;
	private double[] knots;
	private final int degree = 3;
	private int order;
	private double maxKnotValue;
	protected Mode mode = Mode.Open;

	public Nurbs2f(final Vec2f[] cvs) {
		setCVs(cvs);
	}

	public void setCVs(final Vec2f[] cvs) {
		this.cvs = cvs;
		rebuild();
	}

	private void rebuild() {
		order = degree + 1;
		final int numKnots = cvs.length + order;

		knots = new double[numKnots];
		float v = 0;
		for (int i = 0; i < numKnots; i++) {
			knots[i] = v;
			if (i >= degree && i < numKnots - degree - 1)
				v += 1;
		}
		maxKnotValue = v;
	}

	public Vec2f getPoint(final double t, Vec2f store) {
		if (null == store)
			store = new Vec2f();

		if (t <= 0)
			return store.set(cvs[0]);
		if (t >= 1)
			return store.set(cvs[cvs.length - 1]);

		store.zero();

		float valSum = 0;
		float v = 0;
		final Vec2f tmp = new Vec2f();
		for (int i = 0; i < cvs.length; i++) {
			v = (float) CurveAlgorithms.coxDeBoor(knots, t * maxKnotValue, i,
					degree);
			valSum += v;
			tmp.set(cvs[i]);
			tmp.multiplyLocal(v);
			store.addLocal(tmp);
		}
		store.divideLocal(valSum);

		return store;
	}

	public Vec2f getVelocity(double t, Vec2f store) {
		throw new RuntimeException("Not implemented yet.");
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	/**
	 * @param i
	 *            Index of the control vertex to retrieve.
	 * @return Instance of a control vertex used by this curve.
	 */
	public Vec2f getCV(int i) {
		return cvs[i];
	}

	/**
	 * @return The number of control vertices in this curve.
	 */
	public int getCVCount() {
		return cvs.length;
	}
}
