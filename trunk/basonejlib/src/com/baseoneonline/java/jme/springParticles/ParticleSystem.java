package com.baseoneonline.java.jme.springParticles;

import java.util.ArrayList;

public class ParticleSystem {

	private final ArrayList<Particle>		particles	=
															new ArrayList<Particle>();

	private final ArrayList<AbstractField>	fields		=
															new ArrayList<AbstractField>();

	public ParticleSystem() {

		
	}

	public Particle createParticle(float x, float y) {
		Particle p = new Particle(x, y);
		addParticle(p);
		
		
		
		return p;
	}

	public void addField(AbstractField field) {
		fields.add(field);
	}

	public void addParticle(Particle p) {
		particles.add(p);
	}

	public void update(float t) {
		for (Particle p : particles) {
			if (p.chaseGoal) {
				p.vx += (p.tx - p.x) * p.K;
				p.vy += (p.ty - p.y) * p.K;
			}
			for (AbstractField field : fields) {
				
			}
			p.vx *= p.damp;
			p.vy *= p.damp;
			p.x += p.vx;
			p.y += p.vy;
		}
	}

}
