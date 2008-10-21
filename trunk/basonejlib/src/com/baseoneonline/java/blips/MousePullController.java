package com.baseoneonline.java.blips;



import com.jme.input.MouseInput;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.math.FastMath;
import com.jme.math.Plane;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

class MousePullController extends Controller {

	private final Vector3f vel = new Vector3f();
	private final float damp = .9f;
	private final float acc = .005f;
	private final float slerpSpeed = .35f;

	private final Quaternion angle = new Quaternion();
	
	private final Node node;
	private final Plane plane;

	public MousePullController(final Node n, final Plane p) {
		node = n;
		plane = p;
	}

	@Override
	public void update(final float time) {
		
		if (MouseInput.get().isButtonDown(MouseButtonBinding.LEFT_BUTTON)) {
			final Vector3f loc = getMousePosition3D(plane);
			final Vector3f dir = loc.subtract(node.getWorldTranslation());
			vel.addLocal(dir.mult(acc));
		}
		

		
		
		vel.multLocal(damp);
		node.getLocalTranslation().addLocal(vel);

		angle.fromAngles(0,0, FastMath.atan2(vel.y, vel.x));
		node.getLocalRotation().slerp(angle, slerpSpeed);
	}

	/**
	 * @return The position where the mouse pointer intersects with a plane
	 */
	private static Vector3f getMousePosition3D(final Plane p) {
		final Vector2f mousePos = new Vector2f(MouseInput.get().getXAbsolute(),
				MouseInput.get().getYAbsolute());
		final Vector3f point1 = DisplaySystem.getDisplaySystem()
				.getWorldCoordinates(mousePos, 0);
		final Vector3f point2 = DisplaySystem.getDisplaySystem()
				.getWorldCoordinates(mousePos, 1);
		final Vector3f direction = point2.subtractLocal(point1)
				.normalizeLocal();
		final Ray ray = new Ray(point1, direction);
		final Vector3f loc = new Vector3f();
		ray.intersectsWherePlane(p, loc);
		return loc;
	}

}
