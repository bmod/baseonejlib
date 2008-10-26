package com.baseoneonline.java.blips.core;

import com.baseoneonline.java.blips.entities.Blip;
import com.jme.scene.Controller;

public class ParentController extends Controller {

	private final Blip parent;

	public ParentController(final Blip parent) {
		this.parent = parent;
	}

	@Override
	public void update(final float time) {

	}

}
