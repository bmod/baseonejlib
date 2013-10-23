package com.baseoneonline.jlib.ardor3d.spatials;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.util.geom.BufferUtils;

public class QuadMesh extends Mesh {

	private double texScaleU = 1;
	private double texScaleV = 1;
	private double texOffsetU = 0;
	private double texOffsetV = 0;

	private final boolean autoRebuild = false;

	private ReadOnlyVector3[][] vertices;
	private ReadOnlyVector3[][] normals;

	public QuadMesh() {
	}

	public QuadMesh(final ReadOnlyVector3[][] vtx, final ReadOnlyVector3[][] nml) {
		vertices = vtx;
		normals = nml;
		rebuild();
	}

	public void setTextureScale(final double u, final double v) {
		texScaleU = u;
		texScaleV = v;
		if (autoRebuild)
			rebuild();
	}

	public void setTextureRepeat(final double u, final double v) {
		texScaleU = 1 / u;
		texScaleV = 1 / v;
		if (autoRebuild)
			rebuild();
	}

	public void setTextureOffset(final double u, final double v) {
		texOffsetU = u;
		texOffsetV = v;
		if (autoRebuild)
			rebuild();
	}

	private void initBuffers() {
		final int uSamples = vertices.length - 1;
		final int vSamples = vertices[0].length - 1;
		final int verts = (uSamples + 1) * (vSamples + 1);
		final int quads = uSamples * vSamples;
		_meshData.setVertexBuffer(BufferUtils.createVector3Buffer(
				_meshData.getVertexBuffer(), verts));

		_meshData.setNormalBuffer(BufferUtils.createVector3Buffer(
				_meshData.getNormalBuffer(), verts));

		_meshData.setTextureBuffer(BufferUtils.createVector2Buffer(verts), 0);

		// Index buffer
		final IntBuffer idc = BufferUtils.createIntBuffer(quads * 6);
		for (int x = 0; x < uSamples; x++) {
			for (int y = 0; y < vSamples; y++) {
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

	public void setVertices(final ReadOnlyVector3[][] vertices) {
		this.vertices = vertices;
	}

	public void setNormals(final ReadOnlyVector3[][] normals) {
		this.normals = normals;
	}

	public void rebuild() {
		initBuffers();
		final FloatBuffer vtxBuf = _meshData.getVertexBuffer();
		final FloatBuffer nmlBuf = _meshData.getNormalBuffer();
		final FloatBuffer texBuf = _meshData.getTextureBuffer(0);

		vtxBuf.rewind();
		nmlBuf.rewind();
		texBuf.rewind();

		final int uSamples = vertices.length;
		final int vSamples = vertices[0].length;
		for (int iu = 0; iu < uSamples; iu++) {
			for (int iv = 0; iv < vSamples; iv++) {
				final double u = (double) iu / (double) (uSamples - 1);
				final double v = (double) iv / (double) (vSamples - 1);

				ReadOnlyVector3 vtx = vertices[iu][iv];
				vtxBuf.put(vtx.getXf()).put(vtx.getYf()).put(vtx.getZf());

				vtx = normals[iu][iv];
				nmlBuf.put(vtx.getXf()).put(vtx.getYf()).put(vtx.getZf());

				final double texU = texOffsetU + u / texScaleU;
				final double texV = texOffsetV + v / texScaleV;
				texBuf.put((float) texU).put((float) texV);
			}
		}

	}

}