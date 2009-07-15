package com.baseoneonline.java.test.testCedric;

import com.baseoneonline.java.tweening.Equation;
import com.baseoneonline.java.tweening.Equations;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Spatial;

public class MoveToController extends Controller {

	private final Vector3f start = new Vector3f();
	private final Vector3f end = new Vector3f();
	private Equation eq = Equations.Linear;

	private float time = 0;
	private float duration = 1;

	private Spatial spatial;

	private boolean moving = false;

	public MoveToController() {
		setEquation(Equations.Linear);
	}

	public MoveToController(final Spatial s) {
		this(s, Equations.Linear);
	}

	public MoveToController(final Spatial s, final Equation eq) {
		setTarget(s);
		setEquation(eq);
	}

	@Override
	public void update(final float t) {
		if (moving) {
			time += t;
			final Vector3f translate = spatial.getLocalTranslation();
			System.out.println(translate.y);
			if (time < duration) {
				spatial.setLocalTranslation(eq.tween(time / duration, start.x,
						end.x), eq.tween(time / duration, start.y, end.y), eq
						.tween(time / duration, start.z, end.z));
			} else {
				spatial.setLocalTranslation(end);
				moving = false;
			}
		}
	}

	public void setTarget(final Spatial spatial) {
		if (null != spatial)
			spatial.removeController(this);
		this.spatial = spatial;
		spatial.addController(this);
	}

	public void moveTo(final Vector3f pos, final float duration) {
		this.duration = duration;
		moveTo(pos);
	}

	public void moveTo(final Vector3f pos) {
		start.set(spatial.getLocalTranslation());
		end.set(pos);
		time = 0;
		moving = true;
	}

	public void setEquation(final Equation eq) {
		this.eq = eq;
	}

}
