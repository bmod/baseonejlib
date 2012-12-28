package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public interface Curve3 {

	public enum Mode {
		Open, Clamp, Loop
	}

	public abstract int getCVCount();

	public abstract ReadOnlyVector3 getCV(int i);

	public abstract void setCVs(ReadOnlyVector3[] cvs);

	public abstract void setMode(Mode mode);

	public abstract Mode getMode();

	public abstract ReadOnlyVector3 getCVNormal(int index);

	public abstract void setNormals(ReadOnlyVector3[] normals);

	public abstract Vector3 getNormal(double t, Vector3 store);

	public abstract ReadOnlyVector3 getVelocity(double t, Vector3 store);

	public abstract ReadOnlyVector3 getPoint(double t, Vector3 store);

	public abstract void setDefaultNormal(ReadOnlyVector3 unitZ);

	public abstract double getLinearVelocity(double currentPosition);

	public abstract Matrix3 getOrientation(double currentPosition, Matrix3 rot);

}
