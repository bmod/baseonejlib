package com.baseoneonline.java.math;

public class Vec2f {

	public float x;
	public float y;

	public Vec2f(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2f() {
		x = 0;
		y = 0;
	}

	public Vec2f mult(final float n) {
		return new Vec2f(x * n, y * n);
	}

	public void zero() {
		x = 0;
		y = 0;
	}

	public void addLocal(final Vec2f v) {
		x += v.x;
		y += v.y;
	}

	public Vec2f set(final Vec2f v) {
		x = v.x;
		y = v.y;
		return this;
	}

	public Vec2f minus(final Vec2f v) {
		return new Vec2f(x - v.x, y - v.y);
	}

	public void divideLocal(final float value) {
		x /= value;
		y /= value;
	}

	public void divideLocal(final Vec2f value) {
		x /= value.x;
		y /= value.y;
	}

	public float dot(final Vec2f v) {
		return x * v.x + y * v.y;
	}

	public float distanceSquared(final Vec2f v) {
		final float dx = v.x - x;
		final float dy = v.y - y;

		return dx * dx + dy * dy;
	}

	@Override
	public String toString() {
		return "[Vec2f x:" + x + ", y:" + y + "]";
	}

	public void multiplyLocal(final float v) {
		x *= v;
		y *= v;
	}

	public float lengthSquared() {
		return x * x + y * y;
	}

	public void normalizeLocal() {
		final float lengthSq = lengthSquared();
		if (Math.abs(lengthSq) > BMath.EPSILON) {
			multiplyLocal(BMath.inverseSqrt(lengthSq));
		}

	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

}