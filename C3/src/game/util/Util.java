package game.util;

import java.util.ArrayList;

import com.jme.intersection.PickData;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;

public class Util {

	private static TrianglePickResults findSurfPosResults = new TrianglePickResults();

	public static boolean findSurfPos(final Ray ray, final Spatial surface,
			final Vector3f store) {
		surface.findPick(ray, findSurfPosResults);
		final Vector3f[] tri = new Vector3f[3];

		for (int i = 0; i < findSurfPosResults.getNumber(); i++) {

			final PickData pd = findSurfPosResults.getPickData(i);
			final TriMesh geo = (TriMesh) pd.getTargetMesh();
			final ArrayList<Integer> tris = pd.getTargetTris();
			if (tris.size() > 0) {
				geo.getTriangle(tris.get(0), tri);
				if (ray.intersectWhere(tri[0], tri[1], tri[2], store)) return true;
			}
		}
		return false;
	}

}
