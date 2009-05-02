package com.baseoneonline.java.tweening;

public interface Equation {
	/**
	 * @param time
	 *            Value from 0 to 1
	 * @param start
	 *            Start value
	 * @param end
	 *            End value
	 * @return The interpolated value
	 */
	public float tween(float time, float start, float end);
}
