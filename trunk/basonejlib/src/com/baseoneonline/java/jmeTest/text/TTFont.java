package com.baseoneonline.java.jmeTest.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.jme.image.Texture;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class TTFont {

	private final Font font;
	private final float scale = .1f;

	private final HashMap<Character, FontQuad> map = new HashMap<Character, FontQuad>();

	public TTFont(Font font) {
		this.font = font;
	}

	public FontQuad getFontQuad(char ch) {
		FontQuad q;
		q = map.get(ch);
		if (null != q)
			return q;

		// Create texture and quad
		BufferedImage im = getFontImage(ch);
		q = new FontQuad("Char_" + ch, im.getWidth() * scale, im.getHeight()
				* scale);

		// Set the texture
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		Texture t1 = TextureManager.loadTexture(im,
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear, true);
		ts.setTexture(t1);
		q.setRenderState(ts);
		

		return q;
	}

	/**
	 * Create a standard Java2D BufferedImage of the given character
	 * 
	 * @param ch
	 *            The character to create a BufferedImage for
	 * 
	 * @return A BufferedImage containing the character
	 */
	private BufferedImage getFontImage(char ch) {

		// Create a temporary image to extract the character's size
		BufferedImage tempfontImage = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) tempfontImage.getGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setFont(font);
		FontMetrics fontMetrics = g.getFontMetrics();
		int charwidth = fontMetrics.charWidth(ch);

		if (charwidth <= 0) {
			charwidth = 1;
		}
		int charheight = fontMetrics.getHeight();
		if (charheight <= 0) {
			charheight = font.getSize();
		}

		// Create another image holding the character we are creating
		BufferedImage fontImage;
		fontImage = new BufferedImage(charwidth, charheight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D gt = (Graphics2D) fontImage.getGraphics();
		gt.setColor(new Color(0,0,0,0));
		gt.fill(new Rectangle(fontImage.getWidth(), fontImage.getHeight()));
		gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		gt.setFont(font);

		gt.setColor(Color.WHITE);
		int charx = 0;
		int chary = 0;
		gt.drawString(String.valueOf(ch), (charx), (chary)
				+ fontMetrics.getAscent());

		return fontImage;

	}

}
