package com.baseoneonline.java.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.OrbitCamNode;
import com.baseoneonline.java.jme.TexturedQuad;
import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.renderer.lwjgl.LWJGLPbufferTextureRenderer;

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

		q = new TexturedQuad("q", 5, 512);
		rootNode.attachChild(q);
		im = q.getTextureImage();
		camNode = new OrbitCamNode(cam);
		rootNode.attachChild(camNode);

		JMEUtil.letThereBeLight(rootNode);
		
		
		

	}

	@Override
	protected void updateLoop(final float t) {

		int w = im.getWidth();
		int h = im.getHeight();
		final Graphics2D g = (Graphics2D) im.getGraphics();
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.white);
		g.drawLine(FastMath.nextRandomInt(0, w), FastMath.nextRandomInt(0, h),
				FastMath.nextRandomInt(0, w), FastMath.nextRandomInt(0, h));
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
