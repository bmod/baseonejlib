package com.baseoneonline.java.test.testVectorcycle;

import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.AxisRods;

public class TestRods extends Node{
	
	private static TestRods instance;
	
	private TestRods() {
		
	}
	
	public void addRod(Spatial sp) {
		addRod(sp.getWorldTranslation(), sp.getWorldRotation().toRotationMatrix());
	}
	
	
	public void addRod(Vector3f pos, Matrix3f angle) {
		AxisRods rods = new AxisRods("rods", true, .1f);
		
		rods.setLocalTranslation(pos);
		rods.setLocalRotation(angle);
		attachChild(rods);
	}
	
	
	public static TestRods get() {
		if (null == instance) {
			instance = new TestRods();
		}
		return instance;
	}
}
