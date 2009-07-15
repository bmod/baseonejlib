package com.baseoneonline.java.test.testStandardGame.nodes;

import java.net.URL;

import com.baseoneonline.java.jme.JMEUtil;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;

public class MenuNode extends Node {

	public MenuNode() {
		createBitmapFont();
	}

	private void createDefaultText() {
		final Text t = Text.createDefaultTextLabel("MenuText", "Menu here...");
		attachChild(t);
		updateRenderState();
	}

	private void createBitmapFont() {

		final URL fontFile = JMEUtil.getResource(this,
				"assets/images/TestFont.fnt");
		final URL fontBitmap = JMEUtil.getResource(this,
				"assets/images/TestFont_00.png");

		final BitmapFont font = BitmapFontLoader.loadDefaultFont();

		// try {
		// font = BitmapFontLoader.load(fontFile, fontBitmap);
		//
		// } catch (final IOException e) {
		// java.util.logging.Logger.getLogger(MenuNode.class.getName())
		// .warning(e.getMessage());
		// }
		final BitmapText txt = new BitmapText(font, false);
		txt.setLocalTranslation(100, 100, 0);
		txt.setText("The Quick and the Dead");
		txt.setSize(30);
		txt.setDefaultColor(ColorRGBA.red.clone());
		txt.setLightCombineMode(LightCombineMode.Off);
		txt.update();

		attachChild(txt);

	}
}
