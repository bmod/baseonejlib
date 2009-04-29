package com.baseoneonline.java.test.testMouse;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Plane;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

public class TileGridNode extends Node {

	private final int w;
	private final int h;
	private final float tileSize;

	private final float hw;
	private final float hh;

	private final Plane plane;

	private final Entity[][] entities;

	public TileGridNode(int w, int h, float tileSize) {
		this.w = w;
		this.h = h;
		this.tileSize = tileSize;
		hw = (tileSize * (w - 1)) / 2;
		hh = (tileSize * (h - 1)) / 2;
		createGrid();
		entities = new Entity[w][h];
		plane = new Plane(Vector3f.UNIT_Y, 0);

	}

	public int getHeight() {
		return h;
	}

	public int getWidth() {
		return w;
	}

	public Vector2f gridPosition(Vector3f vec) {
		return new Vector2f(Math.round((vec.x + hw) / tileSize), Math
				.round((vec.z + hh) / tileSize));
	}

	public Vector3f roundToGrid(Vector3f intersec) {
		return realPosition(gridPosition(intersec));
	}

	public Plane getPlane() {
		return plane;
	}

	public float getTileSize() {
		return tileSize;
	}

	public Vector3f realPosition(Vector2f gridPos) {
		return new Vector3f(gridPos.x * tileSize - hw, 0, gridPos.y * tileSize
			- hh);
	}

	public Vector3f realPosition(Vector3f gridPos) {
		Vector3f vec = gridPos.mult(tileSize);
		vec.x -= hw;
		vec.z -= hh;
		return vec;
	}

	public Vector3f realPosition(int x, int y) {
		return new Vector3f(x * tileSize - hw, 0, y * tileSize - hh);
	}

	private void createGrid() {
		MaterialState mat1 =
			DisplaySystem.getDisplaySystem().getRenderer()
					.createMaterialState();
		MaterialState mat2 =
			DisplaySystem.getDisplaySystem().getRenderer()
					.createMaterialState();

		mat1.setDiffuse(new ColorRGBA(.6f, .65f, .7f, 1f));
		mat2.setDiffuse(new ColorRGBA(.7f, .75f, .8f, 1f));

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < h; z++) {
				Quad q = new Quad("q" + x + "-" + z, tileSize, tileSize);
				q.getLocalRotation().fromAngles(-FastMath.HALF_PI, 0, 0);
				q.setLocalTranslation(realPosition(x, z));

				if ((x + z) % 2 == 0) {
					q.setRenderState(mat1);
				} else {
					q.setRenderState(mat2);
				}
				q.updateRenderState();
				q.setModelBound(new BoundingBox());
				q.updateModelBound();
				attachChild(q);
			}
		}
	}


	
	
}
