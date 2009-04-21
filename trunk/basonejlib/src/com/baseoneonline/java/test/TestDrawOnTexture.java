package com.baseoneonline.java.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.OrbitCamNode;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

public class TestDrawOnTexture extends BasicFixedRateGame {

	public static void main(String[] args) {
		new TestDrawOnTexture().start();
	}

	OrbitCamNode camNode;
	Texture tex;
	BufferedImage im;

	@Override
	protected void simpleInitGame() {
		display.getRenderer().setBackgroundColor(ColorRGBA.gray);

		Box b = new Box("b", new Vector3f(), 1, 1, 1);

		// Create texture
		im = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		tex = TextureManager.loadTexture(im, MinificationFilter.Trilinear,
				MagnificationFilter.Bilinear, false);
		im.createGraphics();
		TextureState ts = display.getRenderer().createTextureState();
		ts.setTexture(tex);
		b.setRenderState(ts);
		b.updateRenderState();

		rootNode.attachChild(b);

		camNode = new OrbitCamNode(cam);
		rootNode.attachChild(camNode);

		JMEUtil.letThereBeLight(rootNode);

	}

	@Override
	protected void updateLoop(float t) {

		Graphics2D g = (Graphics2D) im.getGraphics();
		g.setColor(Color.RED);
		g.drawLine(FastMath.nextRandomInt(0, 254), FastMath.nextRandomInt(0,
				254), FastMath.nextRandomInt(0, 254), FastMath.nextRandomInt(0,
				254));
		Image img = new Image();

		float mx = MouseInput.get().getXAbsolute();
		float my = MouseInput.get().getYAbsolute();

		camNode.setHeading(mx / display.getWidth() * FastMath.TWO_PI);
		camNode.setAzimuth(my / display.getHeight() * FastMath.HALF_PI);
	}

}
