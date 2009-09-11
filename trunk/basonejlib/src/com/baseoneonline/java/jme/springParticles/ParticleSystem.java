package com.baseoneonline.java.jme.springParticles;

import java.util.ArrayList;

public class ParticleSystem {

	private final ArrayList<Particle> particles = new ArrayList<Particle>();

	private final ArrayList<AbstractField> fields = new ArrayList<AbstractField>();

	public ParticleSystem() {

	}

	public Particle createParticle(final float x, final float y) {
		final Particle p = new Particle(x, y);
		addParticle(p);

		return p;
	}

	public void addField(final AbstractField field) {
		fields.add(field);
	}

	public void addParticle(final Particle p) {
		particles.add(p);
	}

	public void update(final float t) {
		for (final Particle p : particles) {
			if (p.chaseGoal) {
				p.vx += (p.tx - p.x) * p.K;
				p.vy += (p.ty - p.y) * p.K;
			}

			p.vx *= p.damp;
			p.vy *= p.damp;
			p.x += p.vx;
			p.y += p.vy;
		}
	}

	public void removeParticle(final Particle particle) {
		particles.remove(particle);
	}

}
