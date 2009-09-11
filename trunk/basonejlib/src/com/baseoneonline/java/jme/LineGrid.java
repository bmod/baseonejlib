package com.baseoneonline.java.jme;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Line.Mode;

public class LineGrid extends Node {

	private final float spacing;

	private float xMin = -1;
	private float xMax = 1;
	private float zMin = -1;
	private float zMax = 1;

	private Line line;

	private final ColorRGBA lineColor = new ColorRGBA(.6f, .6f, .6f, 1);

	public LineGrid(final float spacing) {
		this.spacing = spacing;
		reconstruct();
		getLocalRotation().fromAngles(-FastMath.HALF_PI, 0, 0);
	}

	private void reconstruct() {
		detachAllChildren();

		final float xs = FastMath.ceil(xMin / spacing);
		final float xe = FastMath.floor(xMax / spacing);

		final float zs = FastMath.ceil(zMin / spacing);
		final float ze = FastMath.floor(zMax / spacing);

		final float xd = xe - xs + 1;
		final float zd = ze - zs + 1;

		final int len = (int) (xd * zd);
		final Vector3f[] vtc = new Vector3f[len * 4];
		int i = 0;

		for (float ix = xs; ix <= xe; ix++) {
			final float x = ix * spacing;
			vtc[i++] = new Vector3f(x, 0, zMin);
			vtc[i++] = new Vector3f(x, 0, zMax);

		}

		for (float iz = zs; iz <= ze; iz++) {
			final float z = iz * spacing;
			vtc[i++] = new Vector3f(xMin, 0, z);
			vtc[i++] = new Vector3f(xMax, 0, z);
		}
		line = JMEUtil.createLine(vtc, lineColor);
		line.setMode(Mode.Segments);
		attachChild(line);
		// line.updateModelBound();

	}

	public void setBounds(final float xMin, final float xMax, final float zMin,
			final float zMax) {
		this.xMin = Math.min(xMin, xMax);
		this.xMax = Math.max(xMin, xMax);
		this.zMin = Math.min(zMin, zMax);
		this.zMax = Math.max(zMin, zMax);
		reconstruct();
	}

	public Geometry getLine() {
		return line;
	}

}
