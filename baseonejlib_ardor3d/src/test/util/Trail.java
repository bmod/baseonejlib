package test.util;

import java.nio.FloatBuffer;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.renderer.state.BlendState;
import com.ardor3d.renderer.state.BlendState.BlendEquation;
import com.ardor3d.renderer.state.BlendState.DestinationFunction;
import com.ardor3d.renderer.state.BlendState.SourceFunction;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.hint.LightCombineMode;
import com.ardor3d.util.geom.BufferUtils;
import com.baseoneonline.java.tools.ArrayUtils;

public class Trail extends Mesh
{
	private final FloatBuffer vertBuffer;
	private final Vector3[] points;

	private final ColorRGBA baseColor;
	private final ColorRGBA[] colors;

	private final Spatial spatial;

	public Trail(final Spatial spatial)
	{
		this(spatial, ColorRGBA.ORANGE, 50, 3);

	}

	public Trail(final Spatial spatial, final ReadOnlyColorRGBA col,
			final int samples, final int fadeOutExponent)
	{
		this.spatial = spatial;
		points = new Vector3[samples];
		colors = new ColorRGBA[samples];
		baseColor = new ColorRGBA(col);

		for (int i = 0; i < points.length; i++)
		{
			points[i] = new Vector3();
			final float t = (float) Math.pow(
					1f - ((float) i / (float) points.length), fadeOutExponent);

			colors[i] = new ColorRGBA(1, 1, 1, t).multiplyLocal(baseColor);
		}

		_meshData.setIndexMode(IndexMode.LineStrip);

		vertBuffer = BufferUtils.createFloatBuffer(points);

		_meshData.setVertexBuffer(vertBuffer);
		_meshData.setColorBuffer(BufferUtils.createFloatBuffer(colors));

		getSceneHints().setLightCombineMode(LightCombineMode.Off);

		final BlendState bs = new BlendState();
		bs.setBlendEnabled(true);
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.One);
		bs.setBlendEquation(BlendEquation.Add);

		setRenderState(bs);

	}

	@Override
	public void updateGeometricState(final double time, final boolean initiator)
	{

		ArrayUtils.shiftRight(points, 1);
		points[0].set(spatial.getTranslation());
		updateBuffer(vertBuffer, points);
		_meshData.setVertexBuffer(vertBuffer);

		super.updateGeometricState(time, initiator);
	}

	private static void updateBuffer(final FloatBuffer buf,
			final ReadOnlyVector3[] pts)
	{
		buf.rewind();
		ReadOnlyVector3 pt;
		for (int i = 0; i < pts.length; i++)
		{
			pt = pts[i];
			buf.put(pt.getXf()).put(pt.getYf()).put(pt.getZf());
		}
	}
}
