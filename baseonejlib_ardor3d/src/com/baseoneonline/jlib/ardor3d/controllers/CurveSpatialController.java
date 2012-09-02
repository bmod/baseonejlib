package com.baseoneonline.jlib.ardor3d.controllers;

import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Vector3;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.controller.SpatialController;
import com.baseoneonline.jlib.ardor3d.math.Curve3;

public class CurveSpatialController implements SpatialController<Spatial>
{

	private final Curve3 curve;
	private double speed;
	private double currentPosition = 0;
	private boolean constantVelocity = true;

	public CurveSpatialController(Curve3 curve, double speed)
	{
		this.curve = curve;
		this.speed = speed;
	}

	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	public double getSpeed()
	{
		return speed;
	}

	public void setConstantVelocity(boolean constantVelocity)
	{
		this.constantVelocity = constantVelocity;
	}

	public boolean isConstantVelocity()
	{
		return constantVelocity;
	}

	@Override
	public void update(double time, Spatial caller)
	{
		Vector3 pos = Vector3.fetchTempInstance();
		Quaternion rot = Quaternion.fetchTempInstance();

		curve.getPoint(currentPosition, pos);
		caller.setTranslation(pos);

		curve.getOrientation(currentPosition, rot);
		caller.setRotation(rot);

		Vector3.releaseTempInstance(pos);

		if (constantVelocity)
			currentPosition += speed * time
					/ curve.getLinearVelocity(currentPosition);
		else
			currentPosition += speed * time;
		currentPosition %= 1;
	}

}