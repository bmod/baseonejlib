package com.baseoneonline.jlib.ardor3d;

import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.functions.SimplexNoise;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.controller.SpatialController;

public class RandomRotationController implements SpatialController<Spatial>
{

	private final Matrix3 rotation = new Matrix3();

	private final SimplexNoise noise = new SimplexNoise();

	private double time = 0;

	private final double speed = .1;

	@Override
	public void update(double t, Spatial caller)
	{
		time += t * speed;

		double u = time;

		double x = noise.noise(u, 0, 0) * MathUtils.TWO_PI;
		double y = noise.noise(0, u, 0) * MathUtils.TWO_PI;
		double z = noise.noise(0, 0, u) * MathUtils.TWO_PI;

		rotation.fromAngles(x, y, z);
		caller.setRotation(rotation);

	}

}
