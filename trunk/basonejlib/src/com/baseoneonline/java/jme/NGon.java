package com.baseoneonline.java.jme;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Line.Mode;

public class NGon extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5839053139338772024L;

	public NGon(float radius, int sides) {
		createLine(radius, sides, ColorRGBA.red);
	}

	public NGon(float radius, int sides, ColorRGBA col) {
		createLine(radius, sides, col);
	}

	private void createLine(float radius, int sides, ColorRGBA color) {

		Vector3f[] pts = new Vector3f[sides];
		Vector3f[] nml = new Vector3f[sides];
		ColorRGBA[] col = new ColorRGBA[sides];
		Vector2f[] tex = new Vector2f[sides];
		for (int i = 0; i < sides; i++) {
			Vector3f p = new Vector3f();
			p.x = FastMath.sin(FastMath.TWO_PI * (float) i / (float) sides)
					* radius;
			p.z = FastMath.cos(FastMath.TWO_PI * (float) i / (float) sides)
					* radius;
			pts[i] = p;
			nml[i] = Vector3f.UNIT_Y;
			col[i] = color;
			tex[i] = new Vector2f();
		}

		Line line = new Line("Line", pts, nml, col, tex);

		line.setLightCombineMode(LightCombineMode.Off);
		line.setMode(Mode.Loop);
		line.setAntialiased(true);
		line.setLineWidth(10);
		attachChild(line);
	}
}
