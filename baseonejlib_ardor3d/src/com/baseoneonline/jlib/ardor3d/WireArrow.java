package com.baseoneonline.jlib.ardor3d;

import java.nio.FloatBuffer;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.hint.LightCombineMode;
import com.ardor3d.util.geom.BufferUtils;

public class WireArrow extends Mesh
{
	private float length = 1;
	private float arrowSize = .2f;

	private final ColorRGBA color = new ColorRGBA(ColorRGBA.YELLOW);

	private final FloatBuffer vertexBuffer = BufferUtils
			.createFloatBuffer(7 * 2 * 3);

	private static float N = (float) MathUtils.sin(120 * MathUtils.DEG_TO_RAD);

	private boolean geomDirty = true;

	public WireArrow()
	{
		_meshData.setIndexMode(IndexMode.Lines);
		_meshData.setColorBuffer(null);
		setDefaultColor(color);

		getSceneHints().setLightCombineMode(LightCombineMode.Off);

	}

	public void setLength(float length)
	{
		this.length = length;
	}

	public void setArrowSize(float arrowSize)
	{
		this.arrowSize = arrowSize;
	}

	private void rebuild()
	{
		float s = arrowSize;
		if (length < arrowSize)
			s = length;
		float r = s / 2;
		float a = length - s; // base until arrow

		float hw = -r / 2;
		float nr = N * r;

		vertexBuffer.rewind();
		// Stick
		vertexBuffer.put(0).put(a).put(0);
		vertexBuffer.put(0).put(0).put(0);

		// Arrow ribs
		vertexBuffer.put(0).put(length).put(0);
		vertexBuffer.put(r).put(a).put(0);

		vertexBuffer.put(0).put(length).put(0);
		vertexBuffer.put(hw).put(a).put(nr);

		vertexBuffer.put(0).put(length).put(0);
		vertexBuffer.put(hw).put(a).put(-nr);

		// Arrow base
		vertexBuffer.put(r).put(a).put(0);
		vertexBuffer.put(hw).put(a).put(nr);
		vertexBuffer.put(hw).put(a).put(nr);
		vertexBuffer.put(hw).put(a).put(-nr);
		vertexBuffer.put(hw).put(a).put(-nr);
		vertexBuffer.put(r).put(a).put(0);

		_meshData.setVertexBuffer(vertexBuffer);
		geomDirty = false;
	}

	public void setColor(ReadOnlyColorRGBA col)
	{
		setDefaultColor(col);
	}

	@Override
	public void draw(Renderer r)
	{
		if (geomDirty)
		{
			rebuild();
		}
		super.draw(r);
	}
}