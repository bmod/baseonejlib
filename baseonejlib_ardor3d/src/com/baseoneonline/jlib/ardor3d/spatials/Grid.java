package com.baseoneonline.jlib.ardor3d.spatials;

import java.nio.FloatBuffer;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.renderer.state.BlendState;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.scenegraph.FloatBufferData;
import com.ardor3d.scenegraph.FloatBufferDataUtil;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.hint.LightCombineMode;
import com.ardor3d.util.geom.BufferUtils;
import com.baseoneonline.jlib.ardor3d.ArdorUtil;

public class Grid extends Mesh {
	private final int subdivs;
	private final ColorRGBA color = new ColorRGBA(1, 1, 1, .2f);

	public Grid() {
		this(10);
	}

	public Grid(final int samples) {
		subdivs = samples;
		rebuild();

		final WireframeState ws = new WireframeState();
		ws.setEnabled(true);
		setRenderState(ws);

		final BlendState bs = new BlendState();
		bs.setBlendEnabled(true);
		bs.setEnabled(true);
		setRenderState(bs);

		getSceneHints().setLightCombineMode(LightCombineMode.Off);

		updateWorldRenderStates(true);
	}

	private void rebuild() {
		final Vector3[] vertices = new Vector3[(subdivs * 2 + 1) * 4];
		int i = 0;
		for (int y = -subdivs; y <= subdivs; y++) {
			vertices[i++] = new Vector3(-subdivs, 0, y);
			vertices[i++] = new Vector3(subdivs, 0, y);
			vertices[i++] = new Vector3(y, 0, -subdivs);
			vertices[i++] = new Vector3(y, 0, subdivs);
		}

		setupData(BufferUtils.createFloatBuffer(vertices),
				BufferUtils.createFloatBuffer(ArdorUtil.createArray(
						Vector3.NEG_UNIT_Y, vertices.length)),
				BufferUtils.createFloatBuffer(ArdorUtil.createArray(color,
						vertices.length)),
				FloatBufferDataUtil.makeNew(ArdorUtil.createArray(Vector3.ZERO,
						vertices.length)));
		_meshData.setIndexMode(IndexMode.Lines);

	}

	private void setupData(final FloatBuffer vertices,
			final FloatBuffer normals, final FloatBuffer colors,
			final FloatBufferData coords) {
		_meshData.setVertexBuffer(vertices);
		_meshData.setNormalBuffer(normals);
		_meshData.setColorBuffer(colors);
		_meshData.setTextureCoords(coords, 0);
		_meshData.setIndices(null);
	}
}
