package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public class BSpline
{

	private BSpline()
	{
	}

	public static Vector3 getPoint(final ReadOnlyVector3 a,
			final ReadOnlyVector3 b, final ReadOnlyVector3 c,
			final ReadOnlyVector3 d, final double t, final Vector3 store)
	{

		final double it = 1 - t;
		final double b0 = it * it * it / 6;
		final double b1 = (3 * t * t * t - 6 * t * t + 4) / 6;
		final double b2 = (-3 * t * t * t + 3 * t * t + 3 * t + 1) / 6;
		final double b3 = t * t * t / 6;

		store.setX(b0 * a.getX() + b1 * b.getX() + b2 * c.getX() + b3
				* d.getX());
		store.setY(b0 * a.getY() + b1 * b.getY() + b2 * c.getY() + b3
				* d.getY());
		store.setZ(b0 * a.getZ() + b1 * b.getZ() + b2 * c.getZ() + b3
				* d.getZ());

		return store;
	}

	public static Vector3 getVelocity(final ReadOnlyVector3 a,
			final ReadOnlyVector3 b, final ReadOnlyVector3 c,
			final ReadOnlyVector3 d, final double t, final Vector3 store)
	{

		final double it = 1 - t;
		final double b0 = it * it;
		final double b1 = 1 + 2 * t - 3 * t * t;

		store.setX((-(a.getX() * b0) + t
				* (-4 * b.getX() + 3 * b.getX() * t + d.getX() * t) + c.getX()
				* b1) * .5);
		store.setY((-(a.getY() * b0) + t
				* (-4 * b.getY() + 3 * b.getY() * t + d.getY() * t) + c.getY()
				* b1) * .5);
		store.setZ((-(a.getZ() * b0) + t
				* (-4 * b.getZ() + 3 * b.getZ() * t + d.getZ() * t) + c.getZ()
				* b1) * .5);

		return store;
	}
}
