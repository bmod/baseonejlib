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

	public void divideLocal(final float valSum) {
		x /= valSum;
		y /= valSum;
	}

	@Override
	public String toString() {
		return "[Vec2f x:" + x + ", y:" + y + "]";
	}

}