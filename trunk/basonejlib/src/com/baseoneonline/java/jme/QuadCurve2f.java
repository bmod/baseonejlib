package com.baseoneonline.java.jme;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;

public class QuadCurve2f {

	public Vector2f start;
	public Vector2f end;
	public Vector2f control;

	public final float anglePrecision = .001f;

	public QuadCurve2f(Vector2f start, Vector2f control, Vector2f end) {
		this.start = start;
		this.control = control;
		this.end = end;
	}

	public Vector2f getPoint(float t) {
		float x1 = start.x + ((control.x - start.x) * t);
		float y1 = start.y + ((control.y - start.y) * t);
		float x2 = control.x + ((end.x - control.x) * t);
		float y2 = control.y + ((end.y - control.y) * t);
		float x3 = x1 + ((x2 - x1) * t);
		float y3 = y1 + ((y2 - y1) * t);
		return new Vector2f(x3, y3);
	}
	
	public float getAngle(float t) {
		Vector2f p1 = getPoint(t);
		Vector2f p2 = getPoint(t+anglePrecision);
		float dx = p2.x - p1.x;
		float dy = p2.y - p1.y;
		return FastMath.atan2(dy, dx);
	}

	public Vector2f getPoint(float t, float offset) {
		Vector2f pt = getPoint(t);
		Vector2f pt2 = getPoint(t + anglePrecision);
		Vector2f dif = pt2.subtract(pt);
		float angle = FastMath.atan2(dif.y, dif.x) + FastMath.HALF_PI;
		float x = pt.x + FastMath.cos(angle) * offset;
		float y = pt.y + FastMath.sin(angle) * offset;
		return new Vector2f(x, y);
	}
}
