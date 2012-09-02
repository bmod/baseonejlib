package com.baseoneonline.jlib.ardor3d.controllers;

import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.functions.SimplexNoise;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.controller.SpatialController;

public class NoiseRotationController implements SpatialController<Spatial>
{

	private static final double ROT_POS_OFFSET = 1000;

	private final SimplexNoise noise = new SimplexNoise();

	private double time = 0;

	private double speed = .1;

	private final boolean applyRotation = true;

	public NoiseRotationController()
	{

	}

	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	@Override
	public void update(double t, Spatial caller)
	{
		time += t * speed;

		if (applyRotation)
			applyRotation(caller);

	}

	private void applyRotation(Spatial caller)
	{
		Matrix3 rotation = Matrix3.fetchTempInstance();

		double u = time + ROT_POS_OFFSET;

		double x = noise.noise(u, 0, 0) * MathUtils.TWO_PI;
		double y = noise.noise(0, u, 0) * MathUtils.TWO_PI;
		double z = noise.noise(0, 0, u) * MathUtils.TWO_PI;

		rotation.fromAngles(x, y, z);
		// Add the rotation to allow other controllers to do their thing.
		caller.setRotation(rotation);

		Matrix3.releaseTempInstance(rotation);
	}

}
