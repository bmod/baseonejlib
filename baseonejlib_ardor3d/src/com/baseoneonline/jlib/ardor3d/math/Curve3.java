package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public interface Curve3 {

	public int getCVCount();

	public ReadOnlyVector3 getCV(int i);

	public ReadOnlyVector3[] getCVS();

	public void setCVs(ReadOnlyVector3[] cvs);

	public ReadOnlyVector3 getTangent(double t, Vector3 store);

	public ReadOnlyVector3 getPoint(double t, Vector3 store);

	public double getLinearVelocity(double currentPosition);

	public Matrix3 getOrientation(double currentPosition,
			ReadOnlyVector3 normal, Matrix3 rot);

	public void getTransform(double t, ReadOnlyVector3 normal, Transform store);

}
