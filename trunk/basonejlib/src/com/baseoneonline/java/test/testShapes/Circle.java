package com.baseoneonline.java.test.testShapes;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Circle implements Shape {
	
	public float radius;
	
	public Circle(float radius) {
		this.radius = radius;
	}
	
	@Override
	public void build(Vector3f[] vtc) {
		float a = 0;
		for (int i=0; i<vtc.length; i++) {
			vtc[i].x = FastMath.cos(a)*radius;
			vtc[i].y = FastMath.sin(a)*radius;
		}
	}
}
