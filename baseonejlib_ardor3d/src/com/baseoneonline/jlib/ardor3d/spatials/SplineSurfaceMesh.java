package com.baseoneonline.jlib.ardor3d.spatials;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.ardor3d.math.Vector3;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.util.geom.BufferUtils;
import com.baseoneonline.jlib.ardor3d.math.BSplineSurface3;

public class SplineSurfaceMesh extends Mesh {

	private final BSplineSurface3 patch;

	private int uSamples;
	private int vSamples;

	private double texScaleU = 1;
	private double texScaleV = 1;
	private double texOffsetU = 0;
	private double texOffsetV = 0;

	private final boolean autoRebuild = true;

	public SplineSurfaceMesh(final BSplineSurface3 patch, final int uSamples, final int vSamples) {
		this.patch = patch;
		this.uSamples = uSamples;
		this.vSamples = vSamples;
		initBuffers();
		rebuild();
	}

	public void setSamples(final int u, final int v)
	{
		uSamples = u;
		vSamples = v;
		if (autoRebuild)
		{
			initBuffers();
			rebuild();
		}
	}

	public void setTextureScale(final double u, final double v)
	{
		texScaleU = u;
		texScaleV = v;
		if (autoRebuild)
			rebuild();
	}

	public void setTextureRepeat(final double u, final double v)
	{
		texScaleU = 1 / u;
		texScaleV = 1 / v;
		if (autoRebuild)
			rebuild();
	}

	public void setTextureOffset(final double u, final double v)
	{
		texOffsetU = u;
		texOffsetV = v;
		if (autoRebuild)
			rebuild();
	}

	private void initBuffers()
	{
		final int verts = (uSamples + 1) * (vSamples + 1);
		final int quads = uSamples * vSamples;
		_meshData.setVertexBuffer(BufferUtils.createVector3Buffer(_meshData.getVertexBuffer(), verts));

		_meshData.setNormalBuffer(BufferUtils.createVector3Buffer(_meshData.getNormalBuffer(), verts));

		_meshData.setTextureBuffer(BufferUtils.createVector2Buffer(verts), 0);

		// Index buffer
		final IntBuffer idc = BufferUtils.createIntBuffer(quads * 6);
		for (int x = 0; x < uSamples; x++)
		{
			for (int y = 0; y < vSamples; y++)
			{
				final int v = vSamples + 1;
				final int a = y + v * x;
				final int b = a + 1;
				final int d = y + v * (x + 1);
				final int c = d + 1;
				idc.put(a).put(b).put(d);
				idc.put(b).put(c).put(d);
			}
		}
		_meshData.setIndexBuffer(idc);

	}

	public void rebuild()
	{
		final FloatBuffer vtxBuf = _meshData.getVertexBuffer();
		final FloatBuffer nmlBuf = _meshData.getNormalBuffer();
		final FloatBuffer texBuf = _meshData.getTextureBuffer(0);

		vtxBuf.rewind();
		nmlBuf.rewind();
		texBuf.rewind();

		final Vector3 vtx = Vector3.fetchTempInstance();
		for (int iu = 0; iu <= uSamples; iu++)
		{
			for (int iv = 0; iv <= vSamples; iv++)
			{
				final double u = (double) iu / (double) uSamples;
				final double v = (double) iv / (double) vSamples;

				patch.getPoint(u, v, vtx);
				vtxBuf.put(vtx.getXf()).put(vtx.getYf()).put(vtx.getZf());
				patch.getNormal(u, v, vtx).normalizeLocal();
				nmlBuf.put(vtx.getXf()).put(vtx.getYf()).put(vtx.getZf());

				final double texU = texOffsetU + u / texScaleU;
				final double texV = texOffsetV + v / texScaleV;

				texBuf.put((float) texU).put((float) texV);
			}
		}
		Vector3.releaseTempInstance(vtx);
	}

}