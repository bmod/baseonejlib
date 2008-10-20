package com.baseoneonline.java.blips;

import com.jme.math.Vector3f;
import com.jme.scene.Node;

public class ParticleNode extends Node {
	
	private Vector3f velocity = new Vector3f();
	private float damp = .9f;
	
	public float getDamp() {
		return damp;
	}


	public void setDamp(float damp) {
		this.damp = damp;
	}


	public Vector3f getVelocity() {
		return velocity;
	}
	
	
	public void setVelocity(Vector3f v) {
		velocity = v;
	}
	
	public void update() {
		velocity.multLocal(damp);
		Vector3f loc = getLocalTranslation();
		
		setLocalTranslation(loc.add(velocity));
	}
	
}
