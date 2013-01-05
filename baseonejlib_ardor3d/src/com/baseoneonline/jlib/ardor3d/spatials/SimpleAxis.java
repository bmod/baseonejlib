package com.baseoneonline.jlib.ardor3d.spatials;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.scenegraph.Line;
import com.ardor3d.scenegraph.hint.LightCombineMode;
import com.ardor3d.util.geom.BufferUtils;

public class SimpleAxis extends Line {

	public SimpleAxis() {
		rebuild();
		getSceneHints().setLightCombineMode(LightCombineMode.Off);

	}

	private void rebuild() {
		ReadOnlyVector3[] verts = { Vector3.ZERO, Vector3.UNIT_X, Vector3.ZERO,
				Vector3.UNIT_Y, Vector3.ZERO, Vector3.UNIT_Z, };
		ReadOnlyColorRGBA[] cols = { ColorRGBA.RED, ColorRGBA.RED,
				ColorRGBA.GREEN, ColorRGBA.GREEN, ColorRGBA.BLUE,
				ColorRGBA.BLUE };
		_meshData.setVertexBuffer(BufferUtils.createFloatBuffer(verts));
		_meshData.setColorBuffer(BufferUtils.createFloatBuffer(cols));
		_meshData.setIndexMode(IndexMode.Lines);
	}

}
