package com.baseoneonline.java.test.testMouse;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;

public class DragHandler extends InputHandler {

	private final TileGridNode grid;
	private final BoardCursor cursor;

	private final float liftTime = 2f;
	private final float dropTime = 2f;

	public DragHandler(final TileGridNode grid, final BoardCursor cursor) {
		this.grid = grid;
		this.cursor = cursor;
		// addAction(liftAction, InputHandler.DEVICE_MOUSE, 0,
		// InputHandler.AXIS_NONE, false);

	}

	private Entity entity;

	private final InputAction liftAction = new InputAction() {
		DragController ctrl = null;

		@Override
		public void performAction(final InputActionEvent evt) {
			final Entity ent = grid.getEntity(cursor.getGridPosition());
			if (evt.getTriggerPressed()) {
				if (null != ent) {
					ctrl = new DragController(ent, cursor
							.mouseOnPlanePosition());
					ent.addController(ctrl);
					entity = ent;
				}
			} else {
				if (null != entity) {
					final Vector3f dropPos = grid.dropEntity(entity, cursor
							.getGridPosition());
					ctrl.drop(dropPos);
					ctrl = null;
					entity = null;
				}
			}
		}
	};

}
