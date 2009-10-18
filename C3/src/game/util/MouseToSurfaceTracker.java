package game.util;

import com.jme.input.MouseInput;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

public class MouseToSurfaceTracker extends SurfaceTracker {

	private final Vector2f mpos = new Vector2f();

	public MouseToSurfaceTracker(final Node surface) {
		super(surface);
	}

	@Override
	public void update() {
		mpos.x = MouseInput.get().getXAbsolute();
		mpos.y = MouseInput.get().getYAbsolute();
		ray.origin = DisplaySystem.getDisplaySystem().getWorldCoordinates(mpos,
				0);
		final Vector3f point2 = DisplaySystem.getDisplaySystem()
				.getWorldCoordinates(mpos, 1);
		ray.direction = point2.subtractLocal(ray.origin).normalizeLocal();
		super.update();
	}
}
