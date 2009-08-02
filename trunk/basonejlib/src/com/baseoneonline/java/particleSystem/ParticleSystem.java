package com.baseoneonline.java.particleSystem;

import java.util.ArrayList;

import com.baseoneonline.java.particleSystem.collision.CollisionShape;
import com.baseoneonline.java.particleSystem.force.ForceField;

public class ParticleSystem {

	private final ArrayList<Particle> particles = new ArrayList<Particle>();
	private final ArrayList<ForceField> forceFields = new ArrayList<ForceField>();
	private final ArrayList<CollisionShape> collisions = new ArrayList<CollisionShape>();

	private float damp;
	private final float sleepTreshold = .01f;

	public ParticleSystem() {}

	public void addParticle(final Particle p) {
		particles.add(p);
	}

	public void removeParticle(final Particle p) {
		particles.remove(p);
	}

	public void addCollision(final CollisionShape c) {
		collisions.add(c);
	}

	public void removeCollision(final CollisionShape c) {
		collisions.remove(c);
	}

	public void step() {
		for (final Particle p : particles) {
			// Resolve force fields
			for (final ForceField field : forceFields) {
				if (field.bounds.contains(p)) {
					p.addForce(field.getForce(p.x, p.y));
				}
			}

			if (p.vx < sleepTreshold && p.vy < sleepTreshold) p.asleep = true;

			if (!p.asleep) {

				// Resolve collisions

				p.vx *= damp;
				p.vy *= damp;

				p.x += p.vx;
				p.y += p.vy;
			}
		}
	}
}
