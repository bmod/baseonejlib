package com.baseoneonline.java.blips.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import com.baseoneonline.java.blips.core.ImagePaintable;
import com.baseoneonline.java.blips.core.ShapeFactory;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;

public class PlayerBlip extends Blip {

	private final ColorRGBA dotColor = new ColorRGBA(1,0,0,.5f);
	private final ColorRGBA pulseDotColor = new ColorRGBA(1,.5f,.5f,1);

	private final Quad dot;

	public PlayerBlip() {
		setPulseDelay(2);

		final Quad arc = ShapeFactory.getImageQuad(new ImagePaintable() {
			public void paint(final Graphics2D g, final int width,
					final int height) {
				g.setColor(Color.white);
				final float margin = 2;
				final float sw = 27;
				final float arcLen = 200;
				final float swh = sw / 2;

				g.setStroke(new BasicStroke(sw,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				final Shape s = new Arc2D.Float(swh + margin, swh + margin, width - sw
						- (margin*2), height - sw -(margin*2) , -arcLen / 2, arcLen, Arc2D.OPEN);
				g.draw(s);

			}
		}, 256, 2.5f);
		attachChild(arc);

		dot = ShapeFactory.getImageQuad(new ImagePaintable() {
			public void paint(final Graphics2D g, final int width,
					final int height) {
				g.setColor(Color.white);
				final float margin = 2;

				final Shape s = new Ellipse2D.Float(margin, margin, width - margin*2, height - margin*2);
				g.fill(s);

			}
		}, 256, 1);
		attachChild(dot);

		dot.setDefaultColor(dotColor);

		updateRenderState();

	}

	@Override
	public void pulse() {

		dot.setDefaultColor(pulseDotColor.clone());
		dot.setLocalScale(1.5f);

	}



	@Override
	public void update() {

		dot.getDefaultColor().interpolate(dotColor, .1f);
		dot.getLocalScale().interpolate(new Vector3f(1,1,1), 0.1f);
	}



}
