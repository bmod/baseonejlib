package test.util;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public class ParticleTail extends WireMesh
{
	private final int samples = 10;
	private final Vector3 point = new Vector3();
	private final Vector3[] vtc = new Vector3[] { point };

	@Override
	protected ReadOnlyVector3[] getVertices()
	{
		return vtc;
	}
}
