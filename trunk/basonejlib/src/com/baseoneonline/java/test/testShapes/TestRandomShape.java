package com.baseoneonline.java.test.testShapes;

import com.jme.app.SimpleGame;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial.LightCombineMode;

public class TestRandomShape extends SimpleGame {

	public static void main(String[] args) {
		new TestRandomShape().start();
	}

	@Override
	protected void simpleInitGame() {
		display.getRenderer().setBackgroundColor(
			new ColorRGBA(1f, .96f, .9f, 1));
		rootNode.setLightCombineMode(LightCombineMode.Off);
		input.setEnabled(false);
		createShape();
	}

	private void createShape() {
		Circle circle = new Circle(3);
		DynamicLine ln = new DynamicLine(16, circle, ColorRGBA.red);
		rootNode.attachChild(ln);
	}
	


}
