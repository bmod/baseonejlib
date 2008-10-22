package com.baseoneonline.java.blips.core;

import com.jme.scene.Spatial;

public class IKContraint {

	public Spatial child;
	public Spatial parent;
	public float distance;

	public IKContraint(final Spatial child, final Spatial parent, final float distance) {
		this.child = child;
		this.parent = parent;
		this.distance = distance;
	}

}
