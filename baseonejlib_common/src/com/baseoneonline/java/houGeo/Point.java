package com.baseoneonline.java.houGeo;


public class Point {
	public double x;
	public double y;
	public double z;
	public double w;

	@Override
	public String toString() {
		return String.format("[%s,  %s, %s, %s]", x, y, z, w);
	}
}