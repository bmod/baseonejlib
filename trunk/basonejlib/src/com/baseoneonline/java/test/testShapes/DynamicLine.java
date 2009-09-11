package com.baseoneonline.java.test.testShapes;

import java.nio.FloatBuffer;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.TexCoords;
import com.jme.util.geom.BufferUtils;

public class DynamicLine extends Line {
	
	protected ColorRGBA color;
	protected FloatBuffer verts;
	protected FloatBuffer source;
	private final Vector3f[] vtc;
	
	public DynamicLine(int segs, Shape shape, ColorRGBA col) {
		super("DynamicLine");
		this.color = col;
		
		setClosed(true);
		vtc = new Vector3f[segs];
		for (int i=0; i<segs; i++) {
			vtc[i] = new Vector3f();
		}
		
		setShape(shape);
		rebuild();
	}
	
	public void setClosed(boolean closed) {
		setMode(closed ? Mode.Loop : Mode.Connected);
	}
	
	public void rebuild() {
		reconstruct(vtc);
		verts = getVertexBuffer();
		source = BufferUtils.clone(verts);
	}
	
	public void setShape(Shape s) {
		s.build(vtc);
	}
	

	protected void reconstruct(Vector3f[] vtc) {
		Vector2f[] tex = new Vector2f[vtc.length];
		ColorRGBA[] cols = new ColorRGBA[vtc.length];
		Vector3f[] nmls = new Vector3f[vtc.length];
		for (int i = 0; i < vtc.length; i++) {
			nmls[i] = Vector3f.UNIT_Y.clone();
			cols[i] = color;
			tex[i] = new Vector2f();
		}
		reconstruct(BufferUtils.createFloatBuffer(vtc), BufferUtils
				.createFloatBuffer(nmls), BufferUtils.createFloatBuffer(cols),
			TexCoords.makeNew(tex));
	}

}
