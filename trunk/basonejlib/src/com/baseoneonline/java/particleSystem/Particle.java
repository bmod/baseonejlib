package com.baseoneonline.java.particleSystem;

public class Particle extends Vec2 {

	public float vx;
	public float vy;
	public boolean asleep = false;

	public Particle() {}

	public Particle(final float x, final float y) {
		super(x, y);
	}

	public void addForce(final Vec2 force) {
		vx += force.x;
		vy += force.y;
	}

}
