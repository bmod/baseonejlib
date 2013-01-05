package com.baseoneonline.jlib.ardor3d.spatials;

import java.nio.FloatBuffer;

import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.scenegraph.Line;
import com.ardor3d.util.geom.BufferUtils;

public class WireSphere extends Line {

	private final Vector3 extents = new Vector3();
	private double radius = 1;

	public WireSphere() {
		rebuild();
	}

	private void rebuild() {
		int samples = 16;
		FloatBuffer buf = BufferUtils.createFloatBuffer((samples + 1) * 6 * 3);
		float x1;
		float y1;
		float x2 = (float) (MathUtils.cos(0) * radius);
		float y2 = (float) (MathUtils.sin(0) * radius);
		float t;
		for (int i = 0; i <= samples; i++) {
			t = (float) i / (float) samples * (float) MathUtils.TWO_PI;

			x1 = (float) (MathUtils.cos(t) * radius);
			y1 = (float) (MathUtils.sin(t) * radius);
			buf.put(x1).put(y1).put(0);
			buf.put(x2).put(y2).put(0);
			buf.put(x1).put(0).put(y1);
			buf.put(x2).put(0).put(y2);
			buf.put(0).put(x1).put(y1);
			buf.put(0).put(x2).put(y2);
			x2 = x1;
			y2 = y1;
		}

		_meshData.setVertexBuffer(buf);
		_meshData.setIndexMode(IndexMode.Lines);
	}

	public void setColor(ReadOnlyColorRGBA color) {
		setDefaultColor(color);
	}

	public void setExtents(ReadOnlyVector3 extents) {
		this.extents.set(extents);
		rebuild();
	}

	public void setRadius(double radius) {
		this.radius = radius;
		rebuild();
	}

	public double getRadius() {
		return radius;
	}
}
