package com.baseoneonline.java.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class TestGraphicsQuad extends SimpleGame {

	public static void main(final String[] args) {
		new TestGraphicsQuad().start();
	}

	CircleQuad q;

	@Override
	protected void simpleInitGame() {
		display.getRenderer().setBackgroundColor(ColorRGBA.darkGray);
		q = new CircleQuad(.1f, .1f);
		rootNode.attachChild(q);
	}

	@Override
	protected void simpleUpdate() {

	}

}

class CircleQuad extends GraphicsQuad {

	private float radius = 1;
	private float strokeWidth = .1f;
	private Ellipse2D ellipse;

	public CircleQuad() {

	}

	public CircleQuad(final float radius, final float strokeWidth) {
		this.strokeWidth = strokeWidth;
		this.radius = radius;
		redraw();
	}

	@Override
	protected void draw(final Graphics2D g) {
		drawRect(g);
		g.setStroke(new BasicStroke(strokeWidth));
		g.setColor(Color.red);

		radius = 1;

		final float range = 1 - strokeWidth;
		final float c1 = (1 - radius * range) / 2;
		final float c2 = radius * range;

		ellipse = new Ellipse2D.Float(c1, c1, c2, c2);
		// ellipse = new Ellipse2D.Float(0, 0, 1, 1);
		g.draw(ellipse);
	}

	public void setRadius(final float radius) {
		this.radius = radius;
		redraw();
	}

	private void drawRect(final Graphics2D g) {
		g.setStroke(new BasicStroke(.01f));
		g.setColor(Color.black);
		g.drawLine(0, 0, 1, 0);
		g.drawLine(0, 1, 1, 1);
		g.drawLine(1, 0, 1, 1);
		g.drawLine(0, 0, 0, 1);
	}
}

abstract class GraphicsQuad extends com.jme.scene.shape.Quad {

	BufferedImage im;
	Texture tex;
	AffineTransform xform = new AffineTransform();
	int textureSize = 128;

	public GraphicsQuad() {
		super("GraphicsQuad", 10, 10);
		setRenderState(getBlendState());
		setRenderState(getTextureState());
	}

	private TextureState getTextureState() {
		final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		im = new BufferedImage(textureSize, textureSize,
				BufferedImage.TYPE_INT_ARGB);
		return ts;

	}

	private BlendState getBlendState() {
		final BlendState bs = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		bs.setBlendEnabled(true);
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		bs.setEnabled(true);
		return bs;
	}

	protected void redraw() {
		final Graphics2D g = (Graphics2D) im.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.scale(textureSize, textureSize);
		draw(g);

		tex = TextureManager.loadTexture(im, MinificationFilter.Trilinear,
				MagnificationFilter.NearestNeighbor, true);

		// final Image image = new Image();
		// final DataBufferInt buf = (DataBufferInt) im.getRaster()
		// .getDataBuffer();
		// final ByteBuffer bb = ByteBuffer.allocate(buf.getSize());
		// for (int i = 0; i < buf.getSize(); i++) {
		// bb.put((byte) buf.getElem(i));
		// }
		// image.setData(bb);
		// image.setFormat(Format.RGBA8);
		//
		// tex.setImage(image);
		// tex.updateMemoryReq();

		final TextureState ts = (TextureState) getRenderState(StateType.Texture);
		ts.setTexture(tex);

		ts.setNeedsRefresh(true);
		updateRenderState();
	}

	protected abstract void draw(final Graphics2D g);

}