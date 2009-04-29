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

public class Cursor extends Node {

	private final TileGridNode grid;
	private final Vector3f intersec = new Vector3f();
	private Camera cam;

	public Cursor(TileGridNode grid) {
		this.grid = grid;
		createQuad();
	}

	public void update(Vector2f screenPos) {
		cam = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
		Vector3f p1 = cam.getWorldCoordinates(screenPos, 0);
		Vector3f p2 = cam.getWorldCoordinates(screenPos, 1);
		Ray ray = new Ray(p1, p2.subtractLocal(p1).normalizeLocal());
		ray.intersectsWherePlane(grid.getPlane(), intersec);
		// Yes
		Vector2f pos = grid.gridPosition(intersec);
		if (pos.x >= 0 && pos.x < grid.getWidth() && pos.y >= 0 && pos.y < grid.getHeight()) {
			setLocalTranslation(grid.roundToGrid(intersec));
			setCullHint(CullHint.Inherit);
		} else {
			setCullHint(CullHint.Always);
		}
		
	}

	private void createQuad() {
		Quad q = new Quad("cursor", grid.getTileSize(), grid.getTileSize());
		q.getLocalRotation().fromAngles(-FastMath.HALF_PI, 0, 0);
		MaterialState ms =
			DisplaySystem.getDisplaySystem().getRenderer()
					.createMaterialState();
		ms.setDiffuse(new ColorRGBA(1f,0f,0f,.3f));
		q.setDefaultColor(new ColorRGBA(1f,0f,0f,.3f));
		JMEUtil.applyBlendState(q);
		
		q.setRenderState(ms);
		q.updateRenderState();
		q.setLocalTranslation(0, .01f, 0);
		attachChild(q);
		setModelBound(new BoundingBox());
		updateModelBound();
		setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		
	}

}
