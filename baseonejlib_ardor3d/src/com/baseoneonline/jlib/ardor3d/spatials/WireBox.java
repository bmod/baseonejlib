package com.baseoneonline.jlib.ardor3d.spatials;

import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.scenegraph.Line;

public class WireBox extends Line {

	public WireBox() {

	}

	public void setColor(ReadOnlyColorRGBA color) {
		setDefaultColor(color);
	}

	public void setData(ReadOnlyVector3 center, ReadOnlyVector3 extents) {
	}
}
