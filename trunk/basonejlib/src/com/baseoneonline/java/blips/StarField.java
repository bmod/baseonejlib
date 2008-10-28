package com.baseoneonline.java.blips;

import java.util.ArrayList;
import java.util.List;

import com.baseoneonline.java.jlib.utils.JMEMath;
import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

public class StarField extends Node {

	private final Camera cam = DisplaySystem.getDisplaySystem().getRenderer()
			.getCamera();

	private final List<Star> stars = new ArrayList<Star>();

	private final float extent = 100;

	private BoundingBox bounds = new BoundingBox(new Vector3f(), extent,
			extent, extent);

	public StarField() {


		for (int i = 0; i < 500; i++) {
			final Star s = new Star();
			s.setLocalTranslation(JMEMath.nextRandomVector3f(-extent, extent));
			s.setModelBound(new BoundingBox());
			s.updateModelBound();
			attachChild(s);
			stars.add(s);
		}

	}

	public void setBounds(final BoundingBox bounds) {
		this.bounds = bounds;
	}

	public BoundingBox getBounds() {
		return bounds;
	}

	public void update() {

		bounds.setCenter(cam.getLocation());

		for (final Star s : stars) {

			if (!s.getWorldBound().intersects(bounds)) {
				s.setLocalTranslation(JMEMath.nextRandomVector3f(-extent, extent).addLocal(bounds.getCenter()));
			}
		}
	}


	private class Star extends Node {

		public Star() {
			final float size = .3f;
			final Quad q = new Quad("star", size, size);
			attachChild(q);
		}

	}

}
