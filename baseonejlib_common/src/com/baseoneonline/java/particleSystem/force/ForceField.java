package com.baseoneonline.java.particleSystem.force;

import com.baseoneonline.java.particleSystem.BoundingBox;
import com.baseoneonline.java.particleSystem.Vec2;

public abstract class ForceField {

	public BoundingBox bounds = new BoundingBox();

	public abstract Vec2 getForce(float x, float y);

}
