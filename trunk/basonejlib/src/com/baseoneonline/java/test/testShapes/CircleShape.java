package com.baseoneonline.java.test.testShapes;

import com.jme.renderer.ColorRGBA;


public class CircleShape extends DynamicLine {
public CircleShape() {
	super(10, new Circle(10), ColorRGBA.red);
}
//	Shape shape;
//	int segs;
//	int len;
//	float rot = FastMath.nextRandomFloat() * FastMath.TWO_PI;
//	float scale = FastMath.nextRandomFloat() * .5f;
//	float speed = .5f + FastMath.nextRandomFloat() * 5;
//	float dev = FastMath.nextRandomFloat() * 5;
//
//	Vector3f seed =
//		new Vector3f(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(),
//			FastMath.nextRandomFloat()).mult(1000);
//
//	float tm = 0;
//
//	public CircleShape(int segs, Shape shape, ColorRGBA col) {
//		this.segs = segs;
//		this.shape = shape;
//		this.color = col;
//		rebuild();
//	}
//
//	@Override
//	protected Vector3f[] initShape() {
//		float a = rot;
//		float x = FastMath.cos(a) * radius;
//		float y = FastMath.sin(a) * radius;
//		Vector3f[] vtc = new Vector3f[segs];
//		for (int i = 0; i < segs; i++) {
//			a += FastMath.TWO_PI / segs;
//			x = FastMath.cos(a) * radius;
//			y = FastMath.sin(a) * radius;
//			vtc[i] = new Vector3f(x, y, 0);
//		}
//		return vtc;
//	}
//
//	@Override
//	public void updateGeometricState(float time, boolean initiator) {
//		tm += time * speed;
//		float x, y;
//		for (int i = 0; i < len; i += 3) {
//			float sx = source.get(i);
//			float sy = source.get(i + 1);
//			float sz = source.get(i + 2);
//			// x = source.get(i) + FastMath.nextRandomFloat()*dev;
//			// y = source.get(i+1) + FastMath.nextRandomFloat()*dev;
//
//			x =
//				(float) (sx + ImprovedNoise.noise(sx * scale + seed.x, sy
//					* scale + tm, sz * scale)
//					* dev);
//			y =
//				(float) (sy + ImprovedNoise.noise(sx * scale + seed.y, sy
//					* scale + tm, sz * scale)
//					* dev);
//
//			verts.put(i, x);
//			verts.put(i + 1, y);
//		}
//		super.updateGeometricState(time, initiator);
//	}

}
