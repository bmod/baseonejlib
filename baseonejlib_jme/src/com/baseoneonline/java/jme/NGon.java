package com.baseoneonline.java.jme;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Line.Mode;

public class NGon extends Node {

	protected Line line;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5839053139338772024L;

	public NGon(final int sides) {
		this(sides, ColorRGBA.red);
	}

	public NGon(final int sides, final ColorRGBA color) {
		final float rad = 1;
		final Vector3f[] pts = new Vector3f[sides];
		final Vector3f[] nml = new Vector3f[sides];
		final ColorRGBA[] col = new ColorRGBA[sides];
		final Vector2f[] tex = new Vector2f[sides];
		for (int i = 0; i < sides; i++) {
			final Vector3f p = new Vector3f();
			p.x = FastMath.sin(FastMath.TWO_PI * i / sides) * rad;
			p.z = FastMath.cos(FastMath.TWO_PI * i / sides) * rad;
			pts[i] = p;
			nml[i] = Vector3f.UNIT_Y;
			col[i] = color;
			tex[i] = new Vector2f();
		}

		line = new Line("Line", pts, nml, col, tex);

		line.setLightCombineMode(LightCombineMode.Off);
		line.setMode(Mode.Loop);
		line.setAntialiased(true);
		line.setLineWidth(10);
		attachChild(line);
	}
}
