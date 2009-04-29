package com.baseoneonline.java.test.testMouse;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

public class Entity extends Node {

	public static enum State {
		Idle, Hover
	}

	Box box;

	private final MaterialState ms_off;
	private final MaterialState ms_hover;

	public Entity(float size) {
		box =
			new Box("Boks", new Vector3f(0, size / 2, size), size * .5f,
				size * .5f, size * .5f);
		ms_off =
			DisplaySystem.getDisplaySystem().getRenderer()
					.createMaterialState();
		ms_off.setDiffuse(ColorRGBA.blue);
		ms_hover =
			DisplaySystem.getDisplaySystem().getRenderer()
					.createMaterialState();
		ms_hover.setDiffuse(ColorRGBA.green);
		attachChild(box);
		
		setState(State.Idle);
	}
	
	

	public void setState(State s) {
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

}
