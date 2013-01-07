package com.baseoneonline.jlib.ardor3d.framework;

import com.ardor3d.util.export.Savable;

public abstract class Component implements Savable {

	private Entity owner;

	public abstract void update(double t);

	public abstract void onAdded();

	public abstract void onRemoved();

	protected <T extends Component> T getComponent(Class<T> type) {
		return getOwner().getComponent(type);
	}

	protected void setOwner(Entity owner) {
		this.owner = owner;
	}

	public Entity getOwner() {
		return owner;
	}

	@Override
	public Class<?> getClassTag() {
		return getClass();
	}

}
