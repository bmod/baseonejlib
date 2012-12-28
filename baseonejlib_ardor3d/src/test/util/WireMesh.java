package test.util;

import java.nio.FloatBuffer;

import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.util.geom.BufferUtils;

public abstract class WireMesh extends Mesh {

	private FloatBuffer vertBuffer;

	public WireMesh() {
		_meshData.setIndexMode(IndexMode.Lines);

		rebuild();
	}

	private void rebuild() {
		ReadOnlyVector3[] vertices = getVertices();
		vertBuffer = BufferUtils.createFloatBuffer(vertices);
		_meshData.setVertexBuffer(vertBuffer);

	}

	protected abstract ReadOnlyVector3[] getVertices();

}
