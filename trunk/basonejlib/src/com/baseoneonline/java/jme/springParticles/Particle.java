package com.baseoneonline.java.jme.springParticles;

public class Particle {

	public float	x;
	public float	y;
	public float	vx;
	public float	vy;
	public float	tx;
	public float	ty;

	public boolean	chaseGoal	= true;
	public float	damp		= .9f;
	public float	K			= .01f;

	public Particle(float x, float y) {
		this.x = x;
		this.y = y;
		tx = x;
		ty = y;
		vx = 0;
		vy = 0;
	}

}
