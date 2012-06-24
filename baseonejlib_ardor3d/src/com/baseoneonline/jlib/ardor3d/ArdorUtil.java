package com.baseoneonline.jlib.ardor3d;

import java.nio.FloatBuffer;
import java.util.Random;

import com.ardor3d.math.Vector2;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.math.type.ReadOnlyVector2;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.util.geom.BufferUtils;

public class ArdorUtil
{
	private static Random random = new Random();

	private ArdorUtil()
	{}

	public static Vector3 randomize(final Vector3 v, final double extents)
	{
		return v.addLocal(randRange(0, extents), randRange(0, extents),
				randRange(0, extents));
	}

	public static double randRange(final double center, final double extents)
	{
		return center + ((random.nextDouble() * 2 - 1) * extents);
	}

	public static Line createLine(final String name,
			final ReadOnlyVector3[] points, final ReadOnlyColorRGBA color)
	{
		final ReadOnlyVector3[] normals = createArray(Vector3.UNIT_Y,
				points.length);
		final ReadOnlyColorRGBA[] cols = createArray(color, points.length);
		final ReadOnlyVector2[] tex = createArray(Vector2.ZERO, points.length);

		final Line line = new Line(name, points, normals, cols, tex,
				IndexMode.LineStrip);
		return line;
	}

	public static ReadOnlyVector3[] createArray(final ReadOnlyVector3 element,
			final int size)
	{
		final ReadOnlyVector3[] arr = new ReadOnlyVector3[size];
		for (int i = 0; i < size; i++)
			arr[i] = element;
		return arr;
	}

	public static ReadOnlyVector2[] createArray(final ReadOnlyVector2 element,
			final int size)
	{
		final ReadOnlyVector2[] arr = new ReadOnlyVector2[size];
		for (int i = 0; i < size; i++)
			arr[i] = element;
		return arr;
	}

	public static ReadOnlyColorRGBA[] createArray(
			final ReadOnlyColorRGBA element, final int size)
	{
		final ReadOnlyColorRGBA[] arr = new ReadOnlyColorRGBA[size];
		for (int i = 0; i < size; i++)
			arr[i] = element;
		return arr;
	}

	public static FloatBuffer createFloatBuffer(ReadOnlyColorRGBA color,
			int size)
	{
		return BufferUtils.createFloatBuffer(createArray(color, size));
	}
}
