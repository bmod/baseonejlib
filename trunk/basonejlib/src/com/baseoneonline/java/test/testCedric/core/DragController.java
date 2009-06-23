package com.baseoneonline.java.test.testCedric.core;

import com.jme.curve.BezierCurve;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Spatial;

public class DragController extends Controller {

	private final Spatial target;
	private Vector3f follow;
	private final Vector3f attachPoint = new Vector3f();

	private final Vector3f v = new Vector3f();
	private final Vector3f k = new Vector3f(.3f, .07f, .3f);
	private final Vector3f damp = new Vector3f(.6f, .8f, .6f);
	private final Vector3f off = new Vector3f(0, 1, 0);
	private final Vector3f cursorOff = off.add(0, .7f, 0);
	private boolean dragging = true;
	private BezierCurve dropBezier;
	private final float dropTime = 1f;
	private float elapsed;
	private static final Quaternion ZERO_ROTATION = new Quaternion();

	public DragController(final Spatial target, final Vector3f follow) {
		this.target = target;
		this.follow = follow;
	}

	public void drop(final Vector3f pos) {
		dragging = false;
		follow = pos;
		final Vector3f[] pts = { target.getLocalTranslation().clone(),
				target.getLocalTranslation().add(v), follow.add(0, 1, 0),
				follow.clone() };
		dropBezier = new BezierCurve("bezier", pts);
		elapsed = 0;
	}

	public Vector3f getAttachPoint() {
		return attachPoint;
	}

	@Override
	public void update(final float time) {
		if (dragging) {
			final Vector3f p = target.getLocalTranslation();
			v.addLocal(follow.add(off).subtract(p).multLocal(k));
			target.getLocalRotation().fromAngles(v.z, 0, -v.x);
			p.addLocal(v.multLocal(damp));
			attachPoint.set(follow.add(cursorOff));
		} else {
			elapsed += .1f;
			final float t = elapsed / dropTime;
			target.setLocalTranslation(dropBezier.getPoint(t));
			target.getLocalRotation().slerp(ZERO_ROTATION, t);
			if (t >= 1) {
				target.setLocalTranslation(follow);
				destroy();
			}
		}
	}

	public void destroy() {
		target.removeController(this);
	}

}
