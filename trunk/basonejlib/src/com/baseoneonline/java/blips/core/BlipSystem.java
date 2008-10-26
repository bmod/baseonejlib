package com.baseoneonline.java.blips.core;

import java.util.ArrayList;
import java.util.List;

import com.baseoneonline.java.blips.entities.Blip;
import com.jme.scene.Node;

public class BlipSystem extends Node {

	private final List<Blip> blips = new ArrayList<Blip>();

	private boolean ticking = false;

	public void addBlip(final Blip b, final Blip parent) {
		blips.add(b);
		attachChild(b);

		if (null != parent) {
			parent.addChild(b);
		}
	}

	public void tick() {
		ticking = true;
	}

	public void update() {
		for (final Blip b : blips) {
			if (!b.hasParent()) {
				b.updateBlip();
			}
			if (ticking) {
				b.tick();
			}
		}
		if (ticking) {
			ticking = false;
		}

	}


}
