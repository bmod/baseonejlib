package test.util;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public class WireBox extends WireMesh
{

	private Vector3[] vertices;
	private Vector3[] lines;
	private final double extents = 1;

	public WireBox()
	{}

	@Override
	protected ReadOnlyVector3[] getVertices()
	{
		if (null == vertices)
		{
			vertices = new Vector3[8];
			for (int i = 0; i < 8; i++)
				vertices[i] = new Vector3();
		}

		vertices[0].set(-extents, -extents, -extents);
		vertices[1].set(extents, -extents, -extents);
		vertices[2].set(extents, -extents, extents);
		vertices[3].set(-extents, -extents, extents);
		vertices[4].set(-extents, extents, -extents);
		vertices[5].set(extents, extents, -extents);
		vertices[6].set(extents, extents, extents);
		vertices[7].set(-extents, extents, extents);

		if (null == lines)
			lines = new Vector3[12 * 2];

		int i = 0;
		lines[i++] = vertices[0];
		lines[i++] = vertices[1];
		lines[i++] = vertices[1];
		lines[i++] = vertices[2];
		lines[i++] = vertices[2];
		lines[i++] = vertices[3];
		lines[i++] = vertices[3];
		lines[i++] = vertices[0];

		lines[i++] = vertices[4];
		lines[i++] = vertices[5];
		lines[i++] = vertices[5];
		lines[i++] = vertices[6];
		lines[i++] = vertices[6];
		lines[i++] = vertices[7];
		lines[i++] = vertices[7];
		lines[i++] = vertices[4];

		lines[i++] = vertices[0];
		lines[i++] = vertices[4];
		lines[i++] = vertices[1];
		lines[i++] = vertices[5];
		lines[i++] = vertices[2];
		lines[i++] = vertices[6];
		lines[i++] = vertices[3];
		lines[i++] = vertices[7];

		return lines;
	}
}