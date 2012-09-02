package test.util;

import java.nio.FloatBuffer;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.scenegraph.Point;
import com.ardor3d.util.geom.BufferUtils;

public class Dot extends Point
{

	private final FloatBuffer vertBuffer;

	public Dot()
	{
		_meshData.setIndexMode(IndexMode.Points);

		vertBuffer = BufferUtils.createFloatBuffer(new Vector3());
		_meshData.setVertexBuffer(vertBuffer);
		_meshData
				.setColorBuffer(BufferUtils.createFloatBuffer(ColorRGBA.WHITE));
		setPointSize(3);

	}
}
