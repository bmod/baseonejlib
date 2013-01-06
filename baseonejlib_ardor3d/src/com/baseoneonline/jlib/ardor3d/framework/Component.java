package com.baseoneonline.jlib.ardor3d.framework;

import com.ardor3d.util.export.Savable;

public abstract class Component implements Savable {

	private Entity owner;

	public void setOwner(Entity owner) {
		this.owner = owner;
	}

	public Entity getOwner() {
		return owner;
	}

	public abstract void onAdded();

	public abstract void onRemoved();

	public abstract void update(double t);

	@Override
	public Class<?> getClassTag() {
		return getClass();
	}

}
