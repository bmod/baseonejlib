package com.baseoneonline.java.test.testMouse;

public class EntityEvent {

	private final Entity entity;
	private int button;

	public EntityEvent(final Entity entity) {
		this.entity = entity;
	}

	public EntityEvent(final Entity entity, final int button) {
		this(entity);
		this.button = button;
	}

	public int getButton() {
		return button;
	}

	public Entity getEntity() {
		return entity;
	}
}
