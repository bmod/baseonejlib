package com.baseoneonline.java.jme;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.TexCoords;
import com.jme.util.geom.BufferUtils;

public class SimpleLine extends Line {
	
	public SimpleLine(Vector3f[] vtc, ColorRGBA col) {
		reconstruct(vtc, col);
	}
	
	public SimpleLine() {
		super();
	}
	
	protected void reconstruct(Vector3f[] vtc, ColorRGBA col) {
		Vector2f[] tex = new Vector2f[vtc.length];
		ColorRGBA[] cols = new ColorRGBA[vtc.length];
		Vector3f[] nmls = new Vector3f[vtc.length];
		for (int i = 0; i < vtc.length; i++) {
			nmls[i] = Vector3f.UNIT_Y.clone();
			cols[i] = col;
			tex[i] = new Vector2f();
		}
		reconstruct(BufferUtils.createFloatBuffer(vtc), BufferUtils
				.createFloatBuffer(nmls), BufferUtils.createFloatBuffer(cols),
			TexCoords.makeNew(tex));	
	}
}
