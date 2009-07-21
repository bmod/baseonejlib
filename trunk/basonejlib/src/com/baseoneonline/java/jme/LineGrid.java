package com.baseoneonline.java.jme;

import java.nio.FloatBuffer;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.TexCoords;
import com.jme.scene.Line.Mode;
import com.jme.scene.shape.Box;

public class LineGrid extends Node {

	private final float spacing;

	private float xMin = -1;
	private float xMax = 1;
	private float zMin = -1;
	private float zMax = 1;

	private Line line;

	private ColorRGBA lineColor = new ColorRGBA(1, 1, 0, 1);

	public LineGrid(float spacing) {
		this.spacing = spacing;
		reconstruct();

	}

	private void reconstruct() {
		detachAllChildren();
		

		
		float xs = FastMath.ceil(xMin / spacing);
		float xe = FastMath.floor(xMax / spacing);

		float zs = FastMath.ceil(zMin / spacing);
		float ze = FastMath.floor(zMax / spacing);

		float xd = xe - xs + 1;
		float zd = ze - zs + 1;

		int len = (int) (xd * zd);
		Vector3f[] vtc = new Vector3f[len*4]; 
		int i =0;
		
		for (float ix = xs; ix <= xe; ix++) {
			float x = ix * spacing;
			vtc[i++] = new Vector3f(x, 0, zMin);
			vtc[i++] = new Vector3f(x, 0, zMax);
			addBox(x, zMin);
			addBox(x, zMax);
		}

		for (float iz = zs; iz <= ze; iz++) {
			float z = iz * spacing;
			vtc[i++] = new Vector3f(xMin, 0, z);
			vtc[i++] = new Vector3f(xMax, 0, z);
		}
		line = JMEUtil.createLine(vtc, lineColor);
		line.setMode(Mode.Segments);
		attachChild(line);
		// line.updateModelBound();

	}

	private void addBox(float x, float z) {
		Box b = new Box("", new Vector3f(x, 0, z), .1f, .1f, .1f);
		attachChild(b);
	}

	private TexCoords createTexCoors(int size) {
		Vector2f coord = new Vector2f();
		Vector2f[] coords = new Vector2f[size];
		for (int i = 0; i < size; i++) {
			coords[i] = coord;
		}
		return TexCoords.makeNew(coords);
	}

	private FloatBuffer createNormalBuffer(Vector3f vec, int size) {
		FloatBuffer buf = FloatBuffer.allocate(size * 3);
		for (int i = 0; i < size; i++) {
			buf.put(vec.x).put(vec.y).put(vec.z);
		}
		return buf;
	}

	private FloatBuffer createColorBuffer(ColorRGBA col, int size) {
		FloatBuffer buf = FloatBuffer.allocate(size * 4);
		for (int i = 0; i < size; i++) {
			buf.put(col.r).put(col.g).put(col.b).put(col.a);
		}
		return buf;
	}

	public void setBounds(float xMin, float xMax, float zMin, float zMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.zMin = zMin;
		this.zMax = zMax;
		reconstruct();
	}

	public Geometry getLine() {
		return line;
	}

}
