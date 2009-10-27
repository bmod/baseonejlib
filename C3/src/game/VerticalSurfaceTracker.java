package game;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

public class VerticalSurfaceTracker extends SurfaceTracker {

	public VerticalSurfaceTracker(final Spatial surface) {
		super(surface);
		ray.direction = new Vector3f(0, -1, 0);
		ray.origin.y = 10;
	}

	public void setRayPos(final Vector2f pos) {
		ray.origin.x = pos.x;
		ray.origin.z = pos.y;
	}

}