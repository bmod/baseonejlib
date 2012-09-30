package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.baseoneonline.java.math.Curve;

public abstract class Curve3 extends Curve<ReadOnlyVector3, Vector3>
{

	protected ReadOnlyVector3[] normals;
	private ReadOnlyVector3 defaultNormal = Vector3.UNIT_Y;

	public Curve3(ReadOnlyVector3[] cvs)
	{
		super(cvs);
	}

	public void setNormals(ReadOnlyVector3[] normals)
	{
		this.normals = normals;
	}

	public void setDefaultNormal(ReadOnlyVector3 defaultNormal)
	{
		if (null == defaultNormal)
			throw new IllegalArgumentException("Cannot provide a null normal");
		this.defaultNormal = defaultNormal;
	}

	public ReadOnlyVector3 getDefaultNormal()
	{
		return defaultNormal;
	}

	public ReadOnlyVector3 getCVNormal(int index)
	{
		if (null == normals)
			return defaultNormal;
		return normals[index];
	}

	/**
	 * Calculates the orientation at the provided time on the curve. Z-axis will
	 * point towards the curve end. Y-axis will be aligned with the normal.
	 * 
	 * @param t
	 * @param store
	 * @return
	 */
	public Matrix3 getOrientation(double t, Matrix3 store)
	{
		Vector3 tangent = Vector3.fetchTempInstance();
		Vector3 normal = Vector3.fetchTempInstance();
		Quaternion q = Quaternion.fetchTempInstance();

		getVelocity(t, tangent);
		getNormal(t, normal);
		q.lookAt(tangent, normal);
		store.set(q);

		Quaternion.releaseTempInstance(q);
		Vector3.releaseTempInstance(tangent);
		Vector3.releaseTempInstance(normal);
		return store;
	}

	public Quaternion getCVOrientation(int index, Quaternion store)
	{
		Vector3 tangent = Vector3.fetchTempInstance();
		Vector3 normal = Vector3.fetchTempInstance();

		double t = (double) index / (double) cvs.length;

		getVelocity(t, tangent);
		// tangent.normalizeLocal();

		getNormal(t, normal);
		store.lookAt(tangent, normal);

		Vector3.releaseTempInstance(tangent);
		Vector3.releaseTempInstance(normal);
		return store;
	}

	public abstract Vector3 getNormal(double t, Vector3 store);

	public double getLinearVelocity(double t)
	{
		Vector3 tmp = Vector3.fetchTempInstance();
		getVelocity(t, tmp);
		double velocity = tmp.length();
		Vector3.releaseTempInstance(tmp);
		return velocity;
	}

}
