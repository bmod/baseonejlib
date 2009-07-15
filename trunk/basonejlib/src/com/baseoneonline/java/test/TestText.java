package com.baseoneonline.java.test;

import com.jme.app.SimpleGame;
import com.jme.scene.Text;

public class TestText extends SimpleGame {

	public static void main(final String[] args) {
		new TestText().start();
	}

	@Override
	protected void simpleInitGame() {
		final Text t = Text.createDefaultTextLabel("bla", "Hello world!");
		rootNode.attachChild(t);
	}
}
