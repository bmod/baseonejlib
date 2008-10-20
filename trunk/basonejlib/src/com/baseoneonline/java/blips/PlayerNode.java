package com.baseoneonline.java.blips;

import com.jme.scene.shape.Quad;

public class PlayerNode extends ParticleNode {
	
	
	public PlayerNode() {
		Quad q = Shapes.getImageQuad(Shapes.getPlayerArc(), 3);
		attachChild(q);
	}
	
	@Override
	public void update() {
		super.update();
		
	}
	
}
