package game.util;

import java.util.ArrayList;

import com.jme.intersection.PickData;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;

public abstract class SurfaceTracker {

	protected Ray ray = new Ray();
	protected TrianglePickResults results = new TrianglePickResults();
	protected Spatial surface;
	protected Vector3f intersection = new Vector3f();

	public SurfaceTracker(final Spatial surface) {
		this.surface = surface;
	}

	public boolean isIntersecting() {
		return intersection != null;
	}

	public Vector3f getIntersection() {
		return intersection;
	}

	public void update() {
		surface.findPick(ray, results);
		final Vector3f[] tri = new Vector3f[3];

		for (int i = 0; i < results.getNumber(); i++) {

			final PickData pd = results.getPickData(i);
			final TriMesh geo = (TriMesh) pd.getTargetMesh();
			final ArrayList<Integer> tris = pd.getTargetTris();
			if (tris.size() > 0) {
				geo.getTriangle(tris.get(0), tri);
				if (ray.intersectWhere(tri[0], tri[1], tri[2], intersection)) break;
			}
		}
	}
}
