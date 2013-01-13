package com.baseoneonline.jlib.ardor3d.framework.entities;

import com.ardor3d.util.export.Savable;

public abstract class Component implements Savable {

	private Entity owner;

	public abstract void update(double t);

	public abstract void resume();

	public abstract void suspend();

	protected <T extends Component> T getComponent(final Class<T> type) {
		return getOwner().getComponent(type);
	}

	public void setOwner(final Entity owner) {
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
