package com.baseoneonline.java.math;

public interface Curve
{

	public Vec2f getCV(int i);

	public int getNumCVs();

	public Vec2f getPoint(float t, Vec2f store);

}
