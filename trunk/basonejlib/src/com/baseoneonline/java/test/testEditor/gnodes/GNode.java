package com.baseoneonline.java.test.testEditor.gnodes;

import java.awt.geom.AffineTransform;

public class GNode {

	private boolean editable = true;
	private AffineTransform transform = new AffineTransform();

	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public AffineTransform getTransform() {
		return transform;
	}

	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}
}
