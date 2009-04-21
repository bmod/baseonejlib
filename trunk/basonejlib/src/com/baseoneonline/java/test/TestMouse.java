package com.baseoneonline.java.test;

import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.OrbitCamNode;
import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Plane;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

public class TestMouse extends SimpleGame {

	public static void main(String[] args) {
		new TestMouse().start();
	}

	OrbitCamNode orbitCam;
	Cursor cursor;
	TileGrid grid;
	Plane mousePlane;

	@Override
	protected void simpleInitGame() {
		MouseInput.get().setCursorVisible(true);

		grid = new TileGrid(16, 10, .5f);

		mousePlane = new Plane(Vector3f.UNIT_Y, 0);

		rootNode.attachChild(grid);

		cursor = new Cursor(grid.getTileSize());
		cursor.getLocalTranslation().y = .1f;
		rootNode.attachChild(cursor);

		orbitCam = new OrbitCamNode(cam);
		orbitCam.setAzimuth(FastMath.HALF_PI * .6f);
		rootNode.attachChild(orbitCam);

		JMEUtil.letThereBeLight(rootNode);

	}

	@Override
	protected void simpleUpdate() {
		updateCursor();
		//updateCamera();
	}

	private void updateCursor() {
		Vector3f mpos = JMEUtil.getMousePosition3D(mousePlane);
		mpos.y = 0.01f;
		System.out.println(mpos);
		cursor.setLocalTranslation(mpos);
	}

	private void updateCamera() {
		// Mouse position, centered and normalized
		float mx =
			(MouseInput.get().getXAbsolute() - (float) display.getWidth() / 2)
				/ display.getWidth() * 2;
		float my =
			(MouseInput.get().getYAbsolute() - (float) display.getHeight() / 2)
				/ display.getHeight() * 2;
		orbitCam.setAzimuth(FastMath.HALF_PI * (my + 1) * .5f);
		orbitCam.setHeading(mx * .3f);
	}

}

class Cursor extends Node {
	public Cursor(float size) {
		Quad q = new Quad("cursor", size, size);
		q.getLocalRotation().fromAngles(-FastMath.HALF_PI, 0, 0);
		MaterialState ms =
			DisplaySystem.getDisplaySystem().getRenderer()
					.createMaterialState();
		ms.setDiffuse(ColorRGBA.red);
		q.setRenderState(ms);
		q.updateRenderState();
		attachChild(q);
		setModelBound(new BoundingBox());
		updateModelBound();
	}
}

class TileGrid extends Node {

	private final int w;
	private final int h;
	private final float tileSize;

	private final float hw;
	private final float hh;

	public TileGrid(int w, int h, float tileSize) {
		this.w = w;
		this.h = h;
		this.tileSize = tileSize;
		hw = (tileSize * (w - 1)) / 2;
		hh = (tileSize * (h - 1)) / 2;
		createGrid();
	}

	public float getTileSize() {
		return tileSize;
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
