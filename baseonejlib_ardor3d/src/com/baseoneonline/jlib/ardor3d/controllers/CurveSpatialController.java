package com.baseoneonline.jlib.ardor3d.controllers;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector3;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.controller.SpatialController;
import com.baseoneonline.jlib.ardor3d.math.Curve3;

public class CurveSpatialController implements SpatialController<Spatial> {

	private final Curve3 curve;
	private double speed;
	private double currentPosition = 0;
	private boolean constantVelocity = true;

	public CurveSpatialController(final Curve3 curve, final double speed) {
		this.curve = curve;
		this.speed = speed;
	}

	public void setSpeed(final double speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}

	public void setConstantVelocity(final boolean constantVelocity) {
		this.constantVelocity = constantVelocity;
	}

	public boolean isConstantVelocity() {
		return constantVelocity;
	}

	@Override
	public void update(final double time, final Spatial caller) {
		final Vector3 pos = Vector3.fetchTempInstance();
		final Matrix3 rot = Matrix3.fetchTempInstance();

		curve.getPoint(currentPosition, pos);
		caller.setTranslation(pos);

		curve.getOrientation(currentPosition, Vector3.UNIT_Y, rot);
		caller.setRotation(rot);

		Vector3.releaseTempInstance(pos);
		Matrix3.releaseTempInstance(rot);

		if (constantVelocity)
			currentPosition += speed * time
					/ curve.getLinearVelocity(currentPosition);
		else
			currentPosition += speed * time;
		currentPosition %= 1;
	}

}