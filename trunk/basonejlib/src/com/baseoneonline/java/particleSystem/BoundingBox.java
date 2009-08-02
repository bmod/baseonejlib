package com.baseoneonline.java.particleSystem;

public class BoundingBox {

	public float minX = 0;
	public float maxX = 0;
	public float minY = 0;
	public float maxY = 0;

	public boolean contains(final Vec2 p) {
		return (p.x > minX && p.x < maxX && p.y > minY && p.y < maxY);
	}

}
