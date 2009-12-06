package com.baseoneonline.java.math;

public class Mandelbrot {

	public static boolean isInSet(final float a, final float b,
			final int maxIterations) {
		float x = 0.0f;
		float y = 0.0f;
		int iterations = 0;
		do {
			final float xnew = x * x - y * y + a;
			final float ynew = 2 * x * y + b;
			x = xnew;
			y = ynew;
			iterations++;
			if (iterations == maxIterations) return false;
		} while (x <= 2 && y <= 2);
		return true;
	}
}
