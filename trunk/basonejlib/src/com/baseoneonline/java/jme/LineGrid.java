package com.baseoneonline.java.jme;

import java.util.ArrayList;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;

public class LineGrid extends Node {

	private final int extents;
	private final float spacing;
	
	private Line line;
	
	public LineGrid() {
		this(10, 1);
	}
	
	public LineGrid(int extents, float spacing) {
		this.extents = extents;
		this.spacing = spacing;
		construct();
	}
	
	private void construct() {
		ArrayList<Vector3f> vtc = new ArrayList<Vector3f>();
		
		for (int x = -extents; x <= extents; x++) {
			vtc.add(new Vector3f(x*spacing,0,-extents));
			vtc.add(new Vector3f(x*spacing,0,extents));
		}
		for (int z = -extents; z <= extents; z++) {
			vtc.add(new Vector3f(-extents,0,z*spacing));
			vtc.add(new Vector3f(extents,0,z*spacing));
		}
		
		line = JMEUtil.createLine(vtc.toArray(new Vector3f[vtc.size()]), ColorRGBA.darkGray);
		attachChild(line);
		setModelBound(new BoundingBox());
		updateModelBound();
	}
	
	
}
