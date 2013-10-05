package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.baseoneonline.jlib.ardor3d.ArdorUtil;

public class SmoothVector {

	public Vector3 current = new Vector3();
	public Vector3 target = new Vector3();
	public Vector3 velocity = new Vector3();
	public double smoothTime = 1;
	public double maxSpeed = 10000;

	public SmoothVector() {
	}

	public SmoothVector(ReadOnlyVector3 v) {
		snap(v);
	}

	public void update(double t) {
		ArdorUtil
				.smoothDamp(current, target, velocity, smoothTime, maxSpeed, t);
	}

	public void snap(ReadOnlyVector3 val) {
		current.set(val);
		target.set(val);
		velocity.zero();
	}

}
