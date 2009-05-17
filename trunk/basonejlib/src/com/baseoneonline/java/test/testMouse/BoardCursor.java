package com.baseoneonline.java.test.testMouse;

import com.baseoneonline.java.jme.JMEUtil;
import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

public class BoardCursor extends Node {

	private final TileGridNode grid;
	private final Vector3f intersec = new Vector3f();
	private Camera cam;
	private final Vector2f gridPosition = new Vector2f();

	public BoardCursor(final TileGridNode grid) {
		this.grid = grid;
		createQuad();
	}

	public Vector3f mouseOnPlanePosition() {
		return intersec;
	}

	public void setVisible(final boolean b) {
		if (b) {
			setCullHint(CullHint.Inherit);
		} else {
			setCullHint(CullHint.Always);
		}
	}

	public void update(final Vector2f screenPos) {
		cam = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
		final Vector3f p1 = cam.getWorldCoordinates(screenPos, 0);
		final Vector3f p2 = cam.getWorldCoordinates(screenPos, 1);
		final Ray ray = new Ray(p1, p2.subtractLocal(p1).normalizeLocal());
		ray.intersectsWherePlane(grid.getPlane(), intersec);
		// Yes
		grid.gridPosition(intersec, gridPosition);
		if (gridPosition.x >= 0 && gridPosition.x < grid.getWidth()
				&& gridPosition.y >= 0 && gridPosition.y < grid.getHeight()) {
			grid.realPosition(gridPosition, getLocalTranslation());
			// setCullHint(CullHint.Inherit);
		} else {
			// setCullHint(CullHint.Always);
		}

	}

	private void createQuad() {
		final Quad q = new Quad("cursor", grid.getTileSize(), grid
				.getTileSize());
		q.getLocalRotation().fromAngles(-FastMath.HALF_PI, 0, 0);
		final MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms.setDiffuse(new ColorRGBA(1f, 0f, 0f, .3f));
		q.setDefaultColor(new ColorRGBA(1f, 0f, 0f, .3f));
		JMEUtil.applyBlendState(q);

		q.setRenderState(ms);
		q.updateRenderState();
		q.setLocalTranslation(0, .01f, 0);
		attachChild(q);
		setModelBound(new BoundingBox());
		updateModelBound();
		setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);

	}

	public Vector2f getGridPosition() {
		return gridPosition;
	}

}
