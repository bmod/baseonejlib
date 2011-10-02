package com.baseoneonline.java.math;

public class Vec3f {

	public float x;
	public float y;
	public float z;

	public Vec3f(final float x, final float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3f() {
		zero();
	}

	public Vec3f mult(final float n) {
		return new Vec3f(x * n, y * n, z * n);
	}

	public void zero() {
		x = 0;
		y = 0;
		z = 0;
	}

	public void addLocal(final Vec3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public Vec3f set(final Vec3f v) {
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}

	public void divideLocal(final float v) {
		x /= v;
		y /= v;
		z /= v;
	}

	@Override
	public String toString() {
		return "[Vec2f x:" + x + ", y:" + y + ", z:" + z + "]";
	}

}