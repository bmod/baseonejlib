package com.baseoneonline.jlib.ardor3d.framework.entities;

import java.io.IOException;

import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;
import com.ardor3d.util.export.Savable;

/**
 * Use {@link #resume()} to initialize the component, {@link #suspend()} to
 * clean it up.
 * 
 */
public abstract class Component implements Savable {

	private Entity entity;

	public void update(double t) {

	}

	/**
	 * Inititalizes the component when it is awakened. Do not re-initialize
	 * objects when not needed.
	 */
	public void resume() {

	}

	public void suspend() {

	}

	protected <T extends Component> T getComponent(final Class<T> type) {
		return getEntity().getComponent(type);
	}

	protected void onCollide(Entity other) {

	}

	public void setOwner(final Entity owner) {
		this.entity = owner;
	}

	public Entity getEntity() {
		return entity;
	}

	@Override
	public Class<?> getClassTag() {
		return getClass();
	}

	@Override
	public void write(OutputCapsule capsule) throws IOException {
	}

	@Override
	public void read(InputCapsule capsule) throws IOException {
	}

}
