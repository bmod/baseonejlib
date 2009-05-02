package com.baseoneonline.java.tweening;

public class Equations {

	private static float PI = (float) Math.PI;

	public static final Equation Linear = new Equation() {
		@Override
		public float tween(final float t, final float b, final float c) {
			return b + ((c - b) * t);
		}
	};

	/**
	 * @author Administrator
	 * 
	 */
	public static class Sine {
		public static final Equation EaseIn = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				return -c * cos(t * (PI / 2)) + c + b;
			}
		};

		public static final Equation EaseOut = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				return c * sin(t * (PI / 2)) + b;
			}
		};

		public static final Equation EaseInOut = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				return -c / 2 * (cos(PI * t) - 1) + b;
			}
		};
	}

	/**
	 * @author Administrator
	 * 
	 */
	public static class Quad {
		public static final Equation EaseIn = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				return c * t * t + b;
			}
		};

		public static final Equation EaseOut = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				return -c * t * (t - 2) + b;
			}
		};

		public static final Equation EaseInOut = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				if ((t / 2) < 1) {
					return c / 2 * t * t + b;
				} else {
					return -c / 2 * ((t - 1) * (t - 2) - 1) + b;
				}
			}
		};
	}

	/**
	 * @author Administrator
	 * 
	 */
	public static class Bounce {
		public static final Equation EaseIn = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				return c - EaseOut.tween(1f - t, 0f, c) + b;
			}
		};

		public static final Equation EaseOut = new Equation() {
			@Override
			public float tween(final float tm, final float b, final float c) {
				float t = tm;
				if ((t) < (1 / 2.75f)) {
					return c * (7.5625f * t * t) + b;
				} else if (t < (2 / 2.75)) {
					return c * (7.5625f * (t -= (1.5 / 2.75)) * t + .75f) + b;
				} else if (t < (2.5 / 2.75)) {
					return c * (7.5625f * (t -= (2.25 / 2.75)) * t + .9375f)
							+ b;
				} else {
					return c * (7.5625f * (t -= (2.625 / 2.75)) * t + .984375f)
							+ b;
				}
			}
		};

		public static final Equation EaseInOut = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				if (t < .5f)
					return EaseIn.tween(t * 2, 0, c) * .5f + b;
				else
					return EaseOut.tween(t * 2 - 1, 0, c) * .5f + c * .5f + b;
			}
		};
	}

	public static class Elastic {
		public static final Equation EaseIn = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				return -c * cos(t * (PI / 2)) + c + b;
			}
		};

		public static final Equation EaseOut = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				return c * sin(t * (PI / 2)) + b;
			}
		};

		public static final Equation EaseInOut = new Equation() {
			@Override
			public float tween(final float t, final float b, final float c) {
				return -c / 2 * (cos(PI * t) - 1) + b;
			}
		};
	}

	private static float cos(final float n) {
		return (float) Math.cos(n);
	}

	private static float sin(final float n) {
		return (float) Math.sin(n);
	}

}
