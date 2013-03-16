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
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.Camera;
import com.google.common.base.Predicates;

/**
 * Mouse/Keyboard camera orbit controller mimicking Maya's navigation controls.
 * 
 */
public class EditorCameraController {

	private double distance = 10;
	private final Vector3 orbitCenter = new Vector3();
	private double heading = MathUtils.HALF_PI / 3;
	private double elevation = -MathUtils.HALF_PI / 2;

	private final double dollyMultiplier = .005;
	private final double dollyWheelMultiplier = .002;
	private final double orbitMultipler = .007;
	private final double panMultiplier = .002;

	private boolean enabled = false;

	private final LogicalLayer logicalLayer;

	private final InputTrigger[] triggers;

	private double fov = 60;

	private final Vector3 pos = new Vector3();
	private final Matrix3 orient = new Matrix3();

	public EditorCameraController(final LogicalLayer logicalLayer) {

		this.logicalLayer = logicalLayer;

		triggers = getTriggers();

		setEnabled(true);
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setFov(double fov) {
		this.fov = fov;
	}

	public double getFov() {
		return fov;
	}

	@SuppressWarnings("unchecked")
	private InputTrigger[] getTriggers() {
		final KeyHeldCondition modifierKey = new KeyHeldCondition(Key.LMENU);
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

	public void setEnabled(final boolean enabled) {
		if (this.enabled == enabled)
			return;
		this.enabled = enabled;

		if (enabled)
			for (final InputTrigger t : triggers)
				logicalLayer.registerTrigger(t);
		else
			for (final InputTrigger t : triggers)
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

			update(source.getCanvasRenderer().getCamera());
		}
	};

	private final TriggerAction panAction = new TriggerAction() {

		@Override
		public void perform(final Canvas source,
				final TwoInputStates inputStates, final double tpf) {
			final MouseState ms = inputStates.getCurrent().getMouseState();
			final Vector3 tmp = Vector3.fetchTempInstance();
			Camera cam = source.getCanvasRenderer().getCamera();

			tmp.set(cam.getLeft());
			tmp.multiplyLocal(ms.getDx() * panMultiplier * distance);
			orbitCenter.addLocal(tmp);

			tmp.set(cam.getUp());
			tmp.multiplyLocal(-ms.getDy() * panMultiplier * distance);
			orbitCenter.addLocal(tmp);

			Vector3.releaseTempInstance(tmp);

			update(cam);
		}
	};

	private final TriggerAction orbitAction = new TriggerAction() {

		@Override
		public void perform(final Canvas source,
				final TwoInputStates inputStates, final double tpf) {
			final MouseState ms = inputStates.getCurrent().getMouseState();
			elevation += ms.getDy() * orbitMultipler;
			heading -= ms.getDx() * orbitMultipler;
			update(source.getCanvasRenderer().getCamera());
		}
	};

	private final TriggerAction homeAction = new TriggerAction() {

		@Override
		@MainThread
		public void perform(final Canvas source,
				final TwoInputStates inputStates, final double tpf) {
			orbitCenter.set(Vector3.ZERO);
			update(source.getCanvasRenderer().getCamera());
		}
	};

	private void update(Camera cam) {
		if (!enabled)
			return;

		heading %= MathUtils.TWO_PI;
		// elevation = MathUtils.clamp(elevation, minElevation, maxElevation);

		pos.set(0, 0, distance);
		orient.fromAngles(elevation, heading, 0);
		orient.applyPost(pos, pos);

		pos.addLocal(orbitCenter);
		cam.setLocation(pos);
		cam.lookAt(orbitCenter, Vector3.UNIT_Y);

		double aspect = (double) cam.getWidth() / (double) cam.getHeight();
		cam.setFrustumPerspective(fov, aspect, cam.getFrustumNear(),
				cam.getFrustumFar());
	}

	public void setCenter(ReadOnlyVector3 center) {
		orbitCenter.set(center);
	}

	public ReadOnlyVector3 getCenter() {
		return orbitCenter;
	}
}
