package com.baseoneonline.java.test.testEditor.gnodes;

public class Block extends GNode {
	public float width = 0;
	public float height = 0;
	
	public Block(float x, float y, float w, float h) {
		getTransform().setToTranslation(x, y);
		this.width = w;
		this.height = h;
	}
	
	public Block() {
	}
	
}
