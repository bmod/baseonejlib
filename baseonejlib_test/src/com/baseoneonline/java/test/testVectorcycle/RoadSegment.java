package com.baseoneonline.java.test.testVectorcycle;

import com.baseoneonline.java.jme.QuadCurve2f;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;

public class RoadSegment {

	public float startAngle;
	public float endAngle;
	public float distance;
	public QuadCurve2f curve;

	public RoadSegment(Vector2f startPoint, float distance, float startAngle,
			float endAngle) {
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		this.distance = distance;

		Vector2f control = new Vector2f();
		control.x = startPoint.x + FastMath.cos(startAngle) * distance / 2;
		control.y = startPoint.y + FastMath.sin(startAngle) * distance / 2;

		Vector2f endPoint = new Vector2f();
		endPoint.x = control.x + FastMath.cos(endAngle) * distance / 2;
		endPoint.y = control.y + FastMath.sin(endAngle) * distance / 2;

		curve = new QuadCurve2f(startPoint, control, endPoint);
	}

}