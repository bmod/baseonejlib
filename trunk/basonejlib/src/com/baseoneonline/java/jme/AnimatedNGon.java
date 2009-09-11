package com.baseoneonline.java.jme;

import java.nio.FloatBuffer;

import com.jme.renderer.ColorRGBA;
import com.jme.util.geom.BufferUtils;

public class AnimatedNGon extends NGon {
	protected FloatBuffer sourceBuffer;
	protected FloatBuffer lineBuffer;

	public AnimatedNGon(final int sides) {
		super(sides);
		initBuffers();
	}

	public AnimatedNGon(final int i, final ColorRGBA red) {
		super(i, red);
		initBuffers();
	}

	private void initBuffers() {
		sourceBuffer = line.getVertexBuffer();
		lineBuffer = BufferUtils.clone(sourceBuffer);
	}

	@Override
	public void updateGeometricState(final float time, final boolean initiator) {
		line.getVertexBuffer();
		final int len = line.getVertexCount();
		for (int i = 0; i < len; i += 3) {

		}
		super.updateGeometricState(time, initiator);
	}
}
