package com.baseoneonline.java.blips;

import com.baseoneonline.java.jlib.utils.JMEMath;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

public class StarField extends Node {

	private final Camera cam = DisplaySystem.getDisplaySystem().getRenderer().getCamera();

	private final Star[] stars = new Star[100];

	private final float extent = 3;

	public StarField() {

		for (int i=0; i<stars.length; i++) {
			final Star s = new Star();
			s.setLocalTranslation(JMEMath.nextRandomVector3f(-extent, extent));
			attachChild(s);
			stars[i] = s;
		}

	}

	private class Star extends Node {



		public Star() {
			final Quad q = new Quad("star", .1f, .1f);
			attachChild(q);
		}

	}

}

