package com.baseoneonline.java.test.testVectorcycle;

import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;

public class Transform {
	public Vector3f translation;
	public Matrix3f rotation;
	
	public Transform() {
		this(new Vector3f(), new Matrix3f());
	}
	
	public Transform(Vector3f translation, Matrix3f rotation) {
		this.translation = translation;
		this.rotation = rotation;
	}
	
}
