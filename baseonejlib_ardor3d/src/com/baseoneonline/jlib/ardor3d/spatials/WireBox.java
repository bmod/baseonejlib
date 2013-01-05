package com.baseoneonline.jlib.ardor3d.spatials;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.scenegraph.Line;
import com.ardor3d.util.geom.BufferUtils;

public class WireBox extends Line {

	private final Vector3 extents = new Vector3();

	public WireBox() {
		rebuild();
	}

	private void rebuild() {
		Vector3 tlf = new Vector3(-extents.getX(), extents.getY(),
				extents.getZ());
		Vector3 trf = new Vector3(extents.getX(), extents.getY(),
				extents.getZ());
		Vector3 tlb = new Vector3(-extents.getX(), extents.getY(),
				-extents.getZ());
		Vector3 trb = new Vector3(extents.getX(), extents.getY(),
				-extents.getZ());
		Vector3 blf = new Vector3(-extents.getX(), -extents.getY(),
				extents.getZ());
		Vector3 brf = new Vector3(extents.getX(), -extents.getY(),
				extents.getZ());
		Vector3 blb = new Vector3(-extents.getX(), -extents.getY(),
				-extents.getZ());
		Vector3 brb = new Vector3(extents.getX(), -extents.getY(),
				-extents.getZ());
		Vector3[] verts = { tlf, trf, trf, trb, trb, tlb, tlb, tlf, tlf, blf,
				trf, brf, tlb, blb, trb, brb, blf, brf, brf, brb, brb, blb,
				blb, blf, };
		_meshData.setVertexBuffer(BufferUtils.createFloatBuffer(verts));

	}

	public void setColor(ReadOnlyColorRGBA color) {
		setDefaultColor(color);
	}

	public void setExtents(ReadOnlyVector3 extents) {
		this.extents.set(extents);
		rebuild();
	}
}
