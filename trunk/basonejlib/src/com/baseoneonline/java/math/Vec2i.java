package com.baseoneonline.java.math;

public class Vec2i {

	public int x;
	public int y;

	public Vec2i() {
		this(0, 0);
	}

	public Vec2i(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public void set(final Vec2i pos) {
		x = pos.x;
		y = pos.y;
	}

	@Override
	public String toString() {
		return "[" + getClass().getName() + " x: " + x + " y:" + y + " ]";
	}

}
