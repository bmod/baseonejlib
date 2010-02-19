package com.baseoneonline.java.test.testMouse;

public class MouseEvent {

	private final Entity entity;
	private int button;

	public MouseEvent(final Entity entity) {
		this.entity = entity;
	}

	public MouseEvent(final Entity entity, final int button) {
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
