package com.baseoneonline.jlib.ardor3d.math;

import com.baseoneonline.jlib.ardor3d.ArdorUtil;

public class SmoothDouble {

	public double current = 0;
	public double target = 0;
	public double velocity = 0;
	public double smoothTime = 1;
	public double maxSpeed = 10000;

	public SmoothDouble() {
	}

	public SmoothDouble(double v) {
		snap(v);
	}

	public void update(double t) {
		double[] values = ArdorUtil.smoothDamp(current, target, velocity,
				smoothTime, maxSpeed, t);
		current = values[0];
		velocity = values[1];
	}

	public void snap(double val) {
		current = val;
		target = val;
		velocity = 0;
	}

}
