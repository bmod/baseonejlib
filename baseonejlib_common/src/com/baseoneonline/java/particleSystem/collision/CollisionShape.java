package com.baseoneonline.java.particleSystem.collision;

import com.baseoneonline.java.particleSystem.BoundingBox;
import com.baseoneonline.java.particleSystem.Particle;

public abstract class CollisionShape {

	public BoundingBox bounds = new BoundingBox();

	public abstract void resolve(Particle p);

}
