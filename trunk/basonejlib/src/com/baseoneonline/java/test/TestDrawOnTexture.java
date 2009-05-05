package com.baseoneonline.java.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.OrbitCamNode;
import com.baseoneonline.java.jme.TexturedQuad;
import com.jme.image.Texture;
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
		// display.getRenderer().setBackgroundColor(ColorRGBA.gray);

		q = new TexturedQuad("q", 5, 512);
		rootNode.attachChild(q);
		im = q.getTextureImage();
		camNode = new OrbitCamNode(cam);
		rootNode.attachChild(camNode);

		JMEUtil.letThereBeLight(rootNode);

	}

	@Override
	protected void updateLoop(final float t) {

		final Graphics2D g = (Graphics2D) im.getGraphics();
		g.setColor(Color.RED);
		g.fillRect(0, 0, im.getWidth(), im.getHeight());
		g.setColor(Color.green);
		g.drawLine(FastMath.nextRandomInt(0, 254), FastMath.nextRandomInt(0,
				254), FastMath.nextRandomInt(0, 254), FastMath.nextRandomInt(0,
				254));
		q.updateTexture(im);

		// final Image img = new Image();
		//
		// final float mx = MouseInput.get().getXAbsolute();
		// final float my = MouseInput.get().getYAbsolute();
		//
		// camNode.setHeading(mx / display.getWidth() * FastMath.TWO_PI);
		// camNode.setAzimuth(my / display.getHeight() * FastMath.HALF_PI);
	}

}
