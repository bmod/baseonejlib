package com.baseoneonline.java.test.testMouse;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

public class Entity extends Node {

	public static enum EntityState {
		Idle, Hover
	}

	private static int entityCount = 0;

	Box box;
	private final Vector2f gridPos = new Vector2f();

	private final MaterialState ms_off;
	private final MaterialState ms_hover;

	public Entity(final float size) {
		super("Entity" + (entityCount++));
		box = new Box("Boks", new Vector3f(0, size / 2, 0), size * .5f,
				size * .5f, size * .5f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();

		ms_off = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms_off.setDiffuse(ColorRGBA.blue);
		ms_hover = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms_hover.setDiffuse(ColorRGBA.green);
		attachChild(box);

		setState(EntityState.Idle);
	}

	public Spatial getSpatial() {
		return box;
	}

	public void setState(final EntityState s) {
		switch (s) {
		case Hover:
			box.setRenderState(ms_hover);
			box.updateRenderState();
			break;

		default:
			box.setRenderState(ms_off);
			box.updateRenderState();
			break;
		}
	}

	public void setGridPos(final Vector2f gridPos) {
		this.gridPos.set(gridPos);
	}

	public Vector2f getGridPos() {
		return gridPos;
	}

	public void setGridPos(final int x, final int y) {
		gridPos.set(x, y);
	}

}
