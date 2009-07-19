package com.baseoneonline.java.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.OrbitCamNode;
import com.baseoneonline.java.jme.TexturedQuad;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;

public class TestDrawOnTexture extends BasicFixedRateGame {

	public static void main(final String[] args) {
		new TestDrawOnTexture().start();
	}

	OrbitCamNode camNode;
	Texture tex;
	BufferedImage im;
	TexturedQuad q;

	@Override
	protected void initFixedRateGame() {
		MouseInput.get().setCursorVisible(true);
		// display.getRenderer().setBackgroundColor(ColorRGBA.gray);

		q = new TexturedQuad("q", 5, 256);
		rootNode.attachChild(q);

		camNode = new OrbitCamNode(cam);
		rootNode.attachChild(camNode);

		updateTexture();
		JMEUtil.letThereBeLight(rootNode);

		KeyBindingManager.getKeyBindingManager().add("updateLine",
				KeyInput.KEY_SPACE);

	}

	private void updateTexture() {
		im = q.getTextureImage();
		final int w = im.getWidth();
		final int h = im.getHeight();

		final Graphics2D g = (Graphics2D) im.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.white);
		g.setStroke(new BasicStroke(2));
		final Line2D line = new Line2D.Float(FastMath.nextRandomFloat() * w,
				FastMath.nextRandomFloat() * h, FastMath.nextRandomFloat() * w,
				FastMath.nextRandomFloat() * h);

		g.draw(line);

		q.updateTexture(im);
		q.updateRenderState();
	}

	@Override
	protected void updateLoop(final float t) {
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"updateLine", false)) {
			updateTexture();
			System.out.println("UpdateTexture");
		}
	}
}
