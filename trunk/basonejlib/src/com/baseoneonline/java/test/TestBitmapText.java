package com.baseoneonline.java.test;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;

public class TestBitmapText extends SimpleGame {

	public static void main(final String[] args) {
		new TestBitmapText().start();
	}

	@Override
	protected void simpleInitGame() {
		display.getRenderer().setBackgroundColor(ColorRGBA.blue);
		final BitmapFont font = BitmapFontLoader.loadDefaultFont();
		final BitmapText text = new BitmapText(font, false);
		text.setText("Blaarp");
		// text.setLocalTranslation(100, 100, 0);
		text.update();
		final Node node = new Node();
		// node.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		node.attachChild(text);

		text.setModelBound(new BoundingBox());
		text.updateModelBound();

		final Vector3f c = text.getModelBound().getCenter();
		cam.setLocation(c.add(new Vector3f(0, 0, 800)));
		cam.lookAt(c, Vector3f.UNIT_Y);

		final Text t2 = Text.createDefaultTextLabel("Blaarp",
				"The Quick Brown Fox");
		t2.setLocalTranslation(0, 20, 0);
		node.attachChild(t2);

		rootNode.attachChild(node);
	}
}
