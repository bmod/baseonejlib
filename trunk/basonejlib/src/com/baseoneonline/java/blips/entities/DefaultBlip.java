package com.baseoneonline.java.blips.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import com.baseoneonline.java.blips.core.ImagePaintable;
import com.baseoneonline.java.blips.core.ShapeFactory;
import com.baseoneonline.java.jlib.utils.JMEMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;

public class DefaultBlip extends Blip {

	private ColorRGBA dotColor = new ColorRGBA(1,0,0,.5f);
	private ColorRGBA pulseDotColor = new ColorRGBA(1,.5f,.5f,1);

	private final Quad dot;

	public DefaultBlip() {

		final Vector3f col = JMEMath.nextRandomVector3f();
		col.normalizeLocal().multLocal(2);
		dotColor = new ColorRGBA(col.x, col.y, col.z, .5f);
		pulseDotColor = dotColor.clone();
		pulseDotColor.a = 1;


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
		dot.getLocalTranslation().z = 0;
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
		dot.getLocalScale().interpolate(new Vector3f(1,1,1), 0.3f);
	}



}
