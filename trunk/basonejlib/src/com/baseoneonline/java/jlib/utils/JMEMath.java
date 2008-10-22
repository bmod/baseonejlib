package com.baseoneonline.java.jlib.utils;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class JMEMath {
	public static Vector3f nextRandomVector3f() {
		return new Vector3f(FastMath.nextRandomFloat(), FastMath
				.nextRandomFloat(), FastMath.nextRandomFloat());
	}

	public static Vector3f nextRandomVector3f(final float min, final float max) {
		return new Vector3f(nextRandomFloat(min, max),
				nextRandomFloat(min, max), nextRandomFloat(min, max));
	}

	public static float nextRandomFloat(final float min, final float max) {
		return min + (FastMath.nextRandomFloat() * (max - min));
	}
}
