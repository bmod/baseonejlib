package com.baseoneonline.jlib.ardor3d.controllers;

import com.ardor3d.annotation.MainThread;
import com.ardor3d.framework.Canvas;
import com.ardor3d.input.Key;
import com.ardor3d.input.MouseState;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyHeldCondition;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.logical.MouseMovedCondition;
import com.ardor3d.input.logical.MouseWheelMovedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TriggerConditions;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.Camera;
import com.google.common.base.Predicates;

/**
 * Mouse/Keyboard camera orbit controller mimicking Maya's navigation controls.
 * 
 */
public class EditorCameraController {

	private final Camera cam;
	private double distance = 10;
	private final Vector3 orbitCenter = new Vector3();
	private double heading = 0;
	private double elevation = -MathUtils.HALF_PI / 2;

	private final double dollyMultiplier = .005;
	private final double dollyWheelMultiplier = .002;
	private final double orbitMultipler = .007;
	private final double panMultiplier = .002;

	private boolean enabled = false;

	private final LogicalLayer logicalLayer;

	private final InputTrigger[] triggers;

	public EditorCameraController(final LogicalLayer logicalLayer,
			final Camera cam) {
		this.cam = cam;

		this.logicalLayer = logicalLayer;

		triggers = getTriggers();

		setEnabled(true);
	}

	@SuppressWarnings("unchecked")
	private InputTrigger[] getTriggers() {
		KeyHeldCondition modifierKey = new KeyHeldCondition(Key.LMENU);
		return new InputTrigger[] {
				new InputTrigger(Predicates.and(modifierKey,
						new MouseMovedCondition(),
						TriggerConditions.leftButtonDown()), orbitAction),
				new InputTrigger(Predicates.and(modifierKey,
						new MouseMovedCondition(),
						TriggerConditions.middleButtonDown()), panAction),
				new InputTrigger(Predicates.and(modifierKey,
						new MouseMovedCondition(),
						TriggerConditions.rightButtonDown()), dollyAction),
				new InputTrigger(new MouseWheelMovedCondition(), dollyAction),
				new InputTrigger(new KeyPressedCondition(Key.HOME), homeAction) };
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled)
			return;
		this.enabled = enabled;

		if (enabled)
			for (InputTrigger t : triggers)
				logicalLayer.registerTrigger(t);
		else
			for (InputTrigger t : triggers)
				logicalLayer.deregisterTrigger(t);
	}

	public boolean isEnabled() {
		return enabled;
	}

	private final TriggerAction dollyAction = new TriggerAction() {

		@Override
		public void perform(final Canvas source,
				final TwoInputStates inputStates, final double tpf) {
			final MouseState ms = inputStates.getCurrent().getMouseState();

			distance -= ms.getDx() * distance * dollyMultiplier;
			distance -= ms.getDwheel() * distance * dollyWheelMultiplier;
		}
	};

	private final TriggerAction panAction = new TriggerAction() {

		@Override
		public void perform(final Canvas source,
				final TwoInputStates inputStates, final double tpf) {
			final MouseState ms = inputStates.getCurrent().getMouseState();
			final Vector3 tmp = Vector3.fetchTempInstance();

			tmp.set(cam.getLeft());
			tmp.multiplyLocal(ms.getDx() * panMultiplier * distance);
			orbitCenter.addLocal(tmp);

			tmp.set(cam.getUp());
			tmp.multiplyLocal(-ms.getDy() * panMultiplier * distance);
			orbitCenter.addLocal(tmp);

			Vector3.releaseTempInstance(tmp);
		}
	};

	private final TriggerAction orbitAction = new TriggerAction() {

		@Override
		public void perform(final Canvas source,
				final TwoInputStates inputStates, final double tpf) {
			final MouseState ms = inputStates.getCurrent().getMouseState();
			elevation += ms.getDy() * orbitMultipler;

			heading -= ms.getDx() * orbitMultipler;
		}
	};

	private final TriggerAction homeAction = new TriggerAction() {

		@Override
		@MainThread
		public void perform(Canvas source, TwoInputStates inputStates,
				double tpf) {
			orbitCenter.set(Vector3.ZERO);
		}
	};

	public void update() {
		if (!enabled)
			return;

		heading %= MathUtils.TWO_PI;
		// elevation = MathUtils.clamp(elevation, minElevation, maxElevation);

		final Vector3 pos = Vector3.fetchTempInstance();
		final Matrix3 orient = Matrix3.fetchTempInstance();

		pos.set(0, 0, distance);
		orient.fromAngles(elevation, heading, 0);
		orient.applyPost(pos, pos);

		pos.addLocal(orbitCenter);
		cam.setLocation(pos);
		cam.lookAt(orbitCenter, Vector3.UNIT_Y);

		Vector3.releaseTempInstance(pos);
		Matrix3.releaseTempInstance(orient);

	}
}
