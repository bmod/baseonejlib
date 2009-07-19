package com.baseoneonline.java.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.OrbitCamNode;
import com.baseoneonline.java.jme.TexturedQuad;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.util.TextureManager;

public class TestDrawOnTexture extends BasicFixedRateGame {

	public static void main(final String[] args) {
		new TestDrawOnTexture().start();
	}

	OrbitCamNode camNode;

	BufferedImage im;
	TexturedQuad q;

	TextureState ts;
	Texture tex;

	@Override
	protected void initFixedRateGame() {
		MouseInput.get().setCursorVisible(true);
		display.getRenderer().setBackgroundColor(ColorRGBA.gray);

		// q = new TexturedQuad("q", 5, 256);
		// rootNode.attachChild(q);

		im = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);

		drawShape(im, .5f);
		// drawShape(im, 1);

		tex = TextureManager.loadTexture(im, MinificationFilter.Trilinear,
				MagnificationFilter.NearestNeighbor, true);

		ts = display.getRenderer().createTextureState();
		ts.setTexture(tex);

		final BlendState bs = display.getRenderer().createBlendState();
		bs.setBlendEnabled(true);
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		bs.setEnabled(true);

		final Quad q = new Quad("KWAD", 5, 5);
		q.setRenderState(ts);
		q.setRenderState(bs);
		q.updateRenderState();
		rootNode.attachChild(q);

		camNode = new OrbitCamNode(cam);
		rootNode.attachChild(camNode);

		JMEUtil.letThereBeLight(rootNode);

		KeyBindingManager.getKeyBindingManager().add("updateLine",
				KeyInput.KEY_SPACE);

	}

	private void drawShape(final BufferedImage im, final float radius) {
		final float strokeWidth = 5;
		final float sh = strokeWidth / 2;
		final float w = (im.getWidth() - strokeWidth);
		final float h = im.getHeight() - strokeWidth;

		final float hw = w / 2;
		final float hh = h / 2;

		final float r = radius * hw;
		final Graphics2D g = (Graphics2D) im.getGraphics();

		g.setBackground(new Color(0, true));
		g.clearRect(0, 0, (int) w, (int) h);
		final Stroke stroke = new BasicStroke(strokeWidth);

		final Rectangle2D rect = new Rectangle2D.Float(sh + hw - r,
				sh + hw - r, r, r);

		final Arc2D arc = new Arc2D.Float(rect, 0, 270, Arc2D.OPEN);

		// final Ellipse2D shape = new Ellipse2D.Float(sh, sh, w - strokeWidth,
		// h
		// - strokeWidth);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g.setStroke(stroke);
		g.draw(arc);
	}

	private void updateTexture() {
		drawShape(im, FastMath.nextRandomFloat());
		tex.updateMemoryReq();

	}

	@Override
	protected void updateLoop(final float t) {
		updateTexture();
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"updateLine", false)) {
			System.out.println("UpdateTexture");
		}
	}
}
