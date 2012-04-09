package com.baseoneonline.jlib.ardor3d;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Matrix4;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyMatrix3;
import com.ardor3d.math.type.ReadOnlyTransform;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.baseoneonline.jlib.ardor3d.math.VecMathPool;

public class Convert
{
	private Convert()
	{}

	public static Vector3 toVector3(Vector3f v, Vector3 store)
	{
		return store.set(v.x, v.y, v.z);
	}

	public static Vector3f toVector3f(ReadOnlyVector3 v, Vector3f store)
	{
		store.set(v.getXf(), v.getYf(), v.getZf());
		return store;
	}

	public static Matrix3 toMatrix3(Matrix3f bMatrix, Matrix3 store)
	{
		store.set(bMatrix.m00, bMatrix.m01, bMatrix.m02, bMatrix.m10,
				bMatrix.m11, bMatrix.m12, bMatrix.m20, bMatrix.m21, bMatrix.m22);
		return store;
	}

	public static Matrix3f toMatrix3f(ReadOnlyMatrix3 mtx, Matrix3f store)
	{
		Vector3f v3f = VecMathPool.VEC3F_POOL.fetch();
		Vector3 v3 = Vector3.fetchTempInstance();

		mtx.getColumn(0, v3);
		toVector3f(v3, v3f);
		store.setColumn(0, v3f);

		mtx.getColumn(1, v3);
		toVector3f(v3, v3f);
		store.setColumn(1, v3f);

		mtx.getColumn(2, v3);
		toVector3f(v3, v3f);
		store.setColumn(2, v3f);

		VecMathPool.VEC3F_POOL.release(v3f);
		Vector3.releaseTempInstance(v3);

		return store;
	}

	public static Matrix4 toMatrix4(Matrix4f bMatrix, Matrix4 store)
	{
		store.set(bMatrix.m00, bMatrix.m01, bMatrix.m02, bMatrix.m03,
				bMatrix.m10, bMatrix.m11, bMatrix.m12, bMatrix.m13,
				bMatrix.m20, bMatrix.m21, bMatrix.m22, bMatrix.m23,
				bMatrix.m30, bMatrix.m31, bMatrix.m32, bMatrix.m33);
		return store;
	}

	public static Transform toTransform(
			com.bulletphysics.linearmath.Transform bTransform, Transform store)
	{
		Vector3 position = Vector3.fetchTempInstance().zero();
		Matrix3 rotation = Matrix3.fetchTempInstance();

		store.setTranslation(toVector3(bTransform.origin, position));
		store.setRotation(toMatrix3(bTransform.basis, rotation));

		Vector3.releaseTempInstance(position);
		Matrix3.releaseTempInstance(rotation);

		return store;
	}

	public static com.bulletphysics.linearmath.Transform toTransform(
			ReadOnlyTransform aTransform,
			com.bulletphysics.linearmath.Transform store)
	{
		toMatrix3f(aTransform.getMatrix(), store.basis);
		toVector3f(aTransform.getTranslation(), store.origin);
		return store;
	}
}
