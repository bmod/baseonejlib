package com.baseoneonline.jlib.ardor3d.jbullet;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Matrix4;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyMatrix4;
import com.ardor3d.math.type.ReadOnlyTransform;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.scenegraph.shape.Sphere;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.SphereShape;

public class BulletConvert {

	public static Transform convert(
			com.bulletphysics.linearmath.Transform bTransform,
			Transform transform) {
		Matrix4f bMatrix = new Matrix4f();
		Matrix4 matrix = new Matrix4();
		bTransform.getMatrix(bMatrix);
		return transform.fromHomogeneousMatrix(convert(bMatrix, matrix));
	}

	public static com.bulletphysics.linearmath.Transform convert(
			ReadOnlyTransform transform,
			com.bulletphysics.linearmath.Transform bTransform) {
		Matrix4 matrix = new Matrix4();
		Matrix4f bMatrix = new Matrix4f();
		transform.getHomogeneousMatrix(matrix);
		bTransform.set(convert(matrix, bMatrix));
		return bTransform;
	}

	public static Matrix4 convert(Matrix4f bMatrix, Matrix4 matrix) {
		return matrix.set(bMatrix.m00, bMatrix.m01, bMatrix.m02, bMatrix.m03,
				bMatrix.m10, bMatrix.m11, bMatrix.m12, bMatrix.m13,
				bMatrix.m20, bMatrix.m21, bMatrix.m22, bMatrix.m23,
				bMatrix.m30, bMatrix.m31, bMatrix.m32, bMatrix.m33);
	}

	public static Matrix4f convert(ReadOnlyMatrix4 matrix, Matrix4f store) {
		store.m00 = (float) matrix.getM00();
		store.m01 = (float) matrix.getM01();
		store.m02 = (float) matrix.getM02();
		store.m03 = (float) matrix.getM03();
		store.m10 = (float) matrix.getM10();
		store.m11 = (float) matrix.getM11();
		store.m12 = (float) matrix.getM12();
		store.m13 = (float) matrix.getM13();
		store.m20 = (float) matrix.getM20();
		store.m21 = (float) matrix.getM21();
		store.m22 = (float) matrix.getM22();
		store.m23 = (float) matrix.getM23();
		store.m30 = (float) matrix.getM30();
		store.m31 = (float) matrix.getM31();
		store.m32 = (float) matrix.getM32();
		store.m33 = (float) matrix.getM33();
		return store;
	}

	public static Vector3f convert(ReadOnlyVector3 v, Vector3f store) {
		store.x = v.getXf();
		store.y = v.getYf();
		store.z = v.getZf();
		return store;
	}

	public static BoxShape createBoxShape(Box b) {
		return new BoxShape(new Vector3f((float) b.getXExtent(),
				(float) b.getYExtent(), (float) b.getZExtent()));
	}

	public static SphereShape createSphereShape(Sphere s) {
		return new SphereShape((float) s.getRadius());
	}

	public static Vector3 convert(Vector3f from, Vector3 store) {
		return store.set(from.x, from.y, from.z);
	}

	public static ColorRGBA convert(Vector3f col, ColorRGBA store) {
		return store.set(col.x, col.y, col.z, 1);
	}

}
