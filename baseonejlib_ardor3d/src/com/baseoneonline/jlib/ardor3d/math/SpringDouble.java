package com.baseoneonline.jlib.ardor3d.math;

public class SpringDouble {

	public double K = 400;
	public double target = 0;
	public double vel = 0;
	public double current = 0;
	public double damp = .7;

	public SpringDouble() {

	}

	public void update(final double t) {
		vel += (target - current) * K * t * t;
		vel *= damp;
		current += vel;
	}

	public void snap(final double d) {
		vel = 0;
		target = d;
		current = d;
	}
}
