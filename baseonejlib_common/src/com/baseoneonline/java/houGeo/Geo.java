package com.baseoneonline.java.houGeo;


public class Geo {
	public Attribute<?>[] pointAttributes;
	public Point[] points;
	public Nurbs[] nurbCurves = new Nurbs[0];

	public Attribute<?> getAttribute(String name) {
		for (Attribute<?> at : pointAttributes)
			if (at.name.equals(name))
				return at;
		throw new RuntimeException("No attribute named: " + name);
	}

}