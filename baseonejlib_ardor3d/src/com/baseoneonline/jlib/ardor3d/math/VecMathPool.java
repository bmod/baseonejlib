package com.baseoneonline.jlib.ardor3d.math;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import com.ardor3d.util.Constants;

public class VecMathPool
{
	private VecMathPool()
	{}

	public static Pool<Vector3f> VEC3F_POOL = Pool.create(Vector3f.class,
			Constants.maxPoolSize);

	public static Pool<Matrix3f> MTX3F_POOL = Pool.create(Matrix3f.class,
			Constants.maxPoolSize);
}
