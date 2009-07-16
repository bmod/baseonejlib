package com.baseoneonline.java.test.testStandardGame.menu;

import java.io.IOException;
import java.net.URL;

import com.baseoneonline.java.jme.JMEUtil;
import com.jme.image.Texture.ApplyMode;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.RenderState.StateType;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;

public class BitmapTextFactory {

	private final String fntExtension = ".fnt";
	private final String pngSuffix = "_00.png";
	private BitmapFont font;
	private final float fontSize;
	private final String fontFile;

	public BitmapTextFactory(final String fontFile, final float fontSize) {
		this.fontFile = fontFile;
		this.fontSize = fontSize;
	}

	public BitmapText createText(final String text) {

		if (null == font) {
			final URL fontDescFile = JMEUtil.getResource(this, fontFile
					+ fntExtension);
			final URL fontBitmap = JMEUtil.getResource(this, fontFile
					+ pngSuffix);
			font = BitmapFontLoader.loadDefaultFont();

			if (null == fontDescFile) throw new NullPointerException(
					"Font could not be loaded: " + fontFile + fntExtension);
			try {
				font = BitmapFontLoader.load(fontDescFile, fontBitmap);
			} catch (final IOException e) {

			}
		}
		final BitmapText txt = new BitmapText(font, false);

		final BlendState bs = (BlendState) txt.getRenderState(StateType.Blend);
		bs.setTestEnabled(false);

		final TextureState ts = (TextureState) txt
				.getRenderState(StateType.Texture);
		ts.getTexture().setApply(ApplyMode.Modulate);
		txt.updateRenderState();

		if (null != text) txt.setText(text);
		txt.setSize(fontSize);
		txt.setDefaultColor(ColorRGBA.white.clone());
		txt.setLightCombineMode(LightCombineMode.Off);
		txt.update();

		return txt;
	}
}
