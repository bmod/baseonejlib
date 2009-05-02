package com.baseoneonline.java.test;

import com.jme.app.SimpleGame;
import com.jme.input.MouseInput;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;

public class TestWorldCoordsBug extends SimpleGame {

	final Sphere sphere = new Sphere("sphere", 5, 5, 1);
	Vector2f mpos = new Vector2f();
	Vector3f wpos = new Vector3f();

	public static void main(final String[] args) {
		new TestWorldCoordsBug().start();
	}

	@Override
	protected void simpleInitGame() {
		MouseInput.get().setCursorVisible(true);
		input.setEnabled(false);
		rootNode.attachChild(sphere);
	}

	@Override
	protected void simpleUpdate() {
		mpos.x = MouseInput.get().getXAbsolute();
		mpos.y = MouseInput.get().getYAbsolute();
		cam.getWorldCoordinates(mpos, 1, wpos);
		sphere.setLocalTranslation(wpos);
	}
}
