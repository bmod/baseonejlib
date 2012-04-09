/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.baseoneonline.jlib.ardor3d.jbullet;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyMatrix3;
import com.ardor3d.math.type.ReadOnlyQuaternion;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.MeshData;
import com.ardor3d.util.geom.BufferUtils;
import com.bulletphysics.collision.shapes.IndexedMesh;

/**
 * Nice convenience methods for conversion between javax.vecmath and
 * com.jme3.math Objects, also some jme to jbullet mesh conversion.
 * 
 * @author normenhansen
 */
public class Converter
{

	private Converter()
	{}

	public static com.ardor3d.math.Vector3 convert(javax.vecmath.Vector3f oldVec)
	{
		com.ardor3d.math.Vector3 newVec = new com.ardor3d.math.Vector3();
		convert(oldVec, newVec);
		return newVec;
	}

	public static com.ardor3d.math.Vector3 convert(
			javax.vecmath.Vector3f oldVec, com.ardor3d.math.Vector3 newVec)
	{
		newVec.set(oldVec.x, oldVec.y, oldVec.z);
		return newVec;
	}

	public static javax.vecmath.Vector3f convert(ReadOnlyVector3 oldVec)
	{
		javax.vecmath.Vector3f newVec = new javax.vecmath.Vector3f();
		convert(oldVec, newVec);
		return newVec;
	}

	public static javax.vecmath.Vector3f convert(ReadOnlyVector3 oldVec,
			javax.vecmath.Vector3f newVec)
	{

		newVec.x = oldVec.getXf();
		newVec.y = oldVec.getYf();
		newVec.z = oldVec.getZf();
		return newVec;
	}

	public static javax.vecmath.Quat4f convert(
			com.ardor3d.math.Quaternion oldQuat, javax.vecmath.Quat4f newQuat)
	{
		newQuat.w = oldQuat.getWf();
		newQuat.x = oldQuat.getXf();
		newQuat.y = oldQuat.getYf();
		newQuat.z = oldQuat.getZf();
		return newQuat;
	}

	public static javax.vecmath.Quat4f convert(
			com.ardor3d.math.Quaternion oldQuat)
	{
		javax.vecmath.Quat4f newQuat = new javax.vecmath.Quat4f();
		convert(oldQuat, newQuat);
		return newQuat;
	}

	public static com.ardor3d.math.Quaternion convert(
			javax.vecmath.Quat4f oldQuat, com.ardor3d.math.Quaternion newQuat)
	{
		newQuat.set(oldQuat.x, oldQuat.y, oldQuat.z, oldQuat.w);
		return newQuat;
	}

	public static com.ardor3d.math.Quaternion convert(
			javax.vecmath.Quat4f oldQuat)
	{
		com.ardor3d.math.Quaternion newQuat = new com.ardor3d.math.Quaternion();
		convert(oldQuat, newQuat);
		return newQuat;
	}

	public static com.ardor3d.math.Quaternion convert(
			javax.vecmath.Matrix3f oldMatrix,
			com.ardor3d.math.Quaternion newQuaternion)
	{
		// the trace is the sum of the diagonal elements; see
		// http://mathworld.wolfram.com/MatrixTrace.html
		double t = oldMatrix.m00 + oldMatrix.m11 + oldMatrix.m22;
		double w, x, y, z;
		// we protect the division by s by ensuring that s>=1
		if (t >= 0)
		{ // |w| >= .5
			double s = MathUtils.sqrt(t + 1); // |s|>=1 ...
			w = 0.5 * s;
			s = 0.5 / s; // so this division isn't bad
			x = (oldMatrix.m21 - oldMatrix.m12) * s;
			y = (oldMatrix.m02 - oldMatrix.m20) * s;
			z = (oldMatrix.m10 - oldMatrix.m01) * s;
		} else if ((oldMatrix.m00 > oldMatrix.m11)
				&& (oldMatrix.m00 > oldMatrix.m22))
		{
			double s = MathUtils.sqrt(1.0f + oldMatrix.m00 - oldMatrix.m11
					- oldMatrix.m22); // |s|>=1
			x = s * 0.5f; // |x| >= .5
			s = 0.5f / s;
			y = (oldMatrix.m10 + oldMatrix.m01) * s;
			z = (oldMatrix.m02 + oldMatrix.m20) * s;
			w = (oldMatrix.m21 - oldMatrix.m12) * s;
		} else if (oldMatrix.m11 > oldMatrix.m22)
		{
			double s = MathUtils.sqrt(1.0f + oldMatrix.m11 - oldMatrix.m00
					- oldMatrix.m22); // |s|>=1
			y = s * 0.5f; // |y| >= .5
			s = 0.5f / s;
			x = (oldMatrix.m10 + oldMatrix.m01) * s;
			z = (oldMatrix.m21 + oldMatrix.m12) * s;
			w = (oldMatrix.m02 - oldMatrix.m20) * s;
		} else
		{
			double s = MathUtils.sqrt(1.0f + oldMatrix.m22 - oldMatrix.m00
					- oldMatrix.m11); // |s|>=1
			z = s * 0.5f; // |z| >= .5
			s = 0.5f / s;
			x = (oldMatrix.m02 + oldMatrix.m20) * s;
			y = (oldMatrix.m21 + oldMatrix.m12) * s;
			w = (oldMatrix.m10 - oldMatrix.m01) * s;
		}
		return newQuaternion.set(x, y, z, w);
	}

	public static javax.vecmath.Matrix3f convert(
			ReadOnlyQuaternion oldQuaternion, javax.vecmath.Matrix3f newMatrix)
	{
		double norm = oldQuaternion.getW() * oldQuaternion.getW()
				+ oldQuaternion.getX() * oldQuaternion.getX()
				+ oldQuaternion.getY() * oldQuaternion.getY()
				+ oldQuaternion.getZ() * oldQuaternion.getZ();
		double s = (norm == 1) ? 2 : (norm > 0) ? 2 / norm : 0;

		// compute xs/ys/zs first to save 6 multiplications, since xs/ys/zs
		// will be used 2-4 times each.
		double xs = oldQuaternion.getX() * s;
		double ys = oldQuaternion.getY() * s;
		double zs = oldQuaternion.getZ() * s;
		double xx = oldQuaternion.getX() * xs;
		double xy = oldQuaternion.getX() * ys;
		double xz = oldQuaternion.getX() * zs;
		double xw = oldQuaternion.getW() * xs;
		double yy = oldQuaternion.getY() * ys;
		double yz = oldQuaternion.getY() * zs;
		double yw = oldQuaternion.getW() * ys;
		double zz = oldQuaternion.getZ() * zs;
		double zw = oldQuaternion.getW() * zs;

		// using s=2/norm (instead of 1/norm) saves 9 multiplications by 2 here
		newMatrix.m00 = (float) (1 - (yy + zz));
		newMatrix.m01 = (float) (xy - zw);
		newMatrix.m02 = (float) (xz + yw);
		newMatrix.m10 = (float) (xy + zw);
		newMatrix.m11 = (float) (1 - (xx + zz));
		newMatrix.m12 = (float) (yz - xw);
		newMatrix.m20 = (float) (xz - yw);
		newMatrix.m21 = (float) (yz + xw);
		newMatrix.m22 = (float) (1 - (xx + yy));

		return newMatrix;
	}

	public static com.ardor3d.math.Matrix3 convert(
			javax.vecmath.Matrix3f oldMatrix)
	{
		com.ardor3d.math.Matrix3 newMatrix = new com.ardor3d.math.Matrix3();
		convert(oldMatrix, newMatrix);
		return newMatrix;
	}

	public static com.ardor3d.math.Matrix3 convert(
			javax.vecmath.Matrix3f oldMatrix, com.ardor3d.math.Matrix3 newMatrix)
	{
		newMatrix.setValue(0, 0, oldMatrix.m00);
		newMatrix.setValue(0, 1, oldMatrix.m01);
		newMatrix.setValue(0, 2, oldMatrix.m02);
		newMatrix.setValue(1, 0, oldMatrix.m10);
		newMatrix.setValue(1, 1, oldMatrix.m11);
		newMatrix.setValue(1, 2, oldMatrix.m12);
		newMatrix.setValue(2, 0, oldMatrix.m20);
		newMatrix.setValue(2, 1, oldMatrix.m21);
		newMatrix.setValue(2, 2, oldMatrix.m22);
		return newMatrix;
	}

	public static javax.vecmath.Matrix3f convert(
			com.ardor3d.math.Matrix3 oldMatrix)
	{
		javax.vecmath.Matrix3f newMatrix = new javax.vecmath.Matrix3f();
		convert(oldMatrix, newMatrix);
		return newMatrix;
	}

	public static javax.vecmath.Matrix3f convert(ReadOnlyMatrix3 oldMatrix,
			javax.vecmath.Matrix3f newMatrix)
	{
		newMatrix.m00 = oldMatrix.getValuef(0, 0);
		newMatrix.m01 = oldMatrix.getValuef(0, 1);
		newMatrix.m02 = oldMatrix.getValuef(0, 2);
		newMatrix.m10 = oldMatrix.getValuef(1, 0);
		newMatrix.m11 = oldMatrix.getValuef(1, 1);
		newMatrix.m12 = oldMatrix.getValuef(1, 2);
		newMatrix.m20 = oldMatrix.getValuef(2, 0);
		newMatrix.m21 = oldMatrix.getValuef(2, 1);
		newMatrix.m22 = oldMatrix.getValuef(2, 2);
		return newMatrix;
	}

	public static com.bulletphysics.linearmath.Transform convert(
			com.ardor3d.math.Transform in,
			com.bulletphysics.linearmath.Transform out)
	{
		convert(in.getTranslation(), out.origin);
		convert(in.getMatrix(), out.basis);
		return out;
	}

	public static com.ardor3d.math.Transform convert(
			com.bulletphysics.linearmath.Transform in,
			com.ardor3d.math.Transform out)
	{
		Vector3 origin = Vector3.fetchTempInstance();
		Matrix3 basis = Matrix3.fetchTempInstance();

		convert(in.origin, origin);
		convert(in.basis, basis);

		out.setTranslation(origin);
		out.setRotation(basis);

		Vector3.releaseTempInstance(origin);
		Matrix3.releaseTempInstance(basis);
		return out;
	}

	public static IndexedMesh convert(Mesh mesh)
	{
		MeshData md = mesh.getMeshData();
		IndexedMesh jBulletIndexedMesh = new IndexedMesh();
		jBulletIndexedMesh.triangleIndexBase = ByteBuffer.allocate(md
				.getPrimitiveCount(0) * 3 * 4);
		jBulletIndexedMesh.vertexBase = ByteBuffer
				.allocate(md.getVertexCount() * 3 * 4);

		IntBuffer indices = md.getIndices().asIntBuffer();

		FloatBuffer vertices = md.getVertexBuffer();
		vertices.rewind();

		int verticesLength = md.getVertexCount() * 3;
		jBulletIndexedMesh.numVertices = md.getVertexCount();
		jBulletIndexedMesh.vertexStride = 12; // 3 verts * 4 bytes per.
		for (int i = 0; i < verticesLength; i++)
		{
			float tempFloat = vertices.get();
			jBulletIndexedMesh.vertexBase.putFloat(tempFloat);
		}

		int indicesLength = md.getPrimitiveCount(0) * 3;
		jBulletIndexedMesh.numTriangles = md.getPrimitiveCount(0);
		jBulletIndexedMesh.triangleIndexStride = 12; // 3 index entries * 4
														// bytes each.
		for (int i = 0; i < indicesLength; i++)
		{
			jBulletIndexedMesh.triangleIndexBase.putInt(indices.get(i));
		}
		vertices.rewind();
		vertices.clear();

		return jBulletIndexedMesh;
	}

	public static Mesh convert(IndexedMesh mesh)
	{
		Mesh jmeMesh = new Mesh();
		MeshData md = jmeMesh.getMeshData();

		IntBuffer indicess = BufferUtils.createIntBuffer(mesh.numTriangles * 3);
		FloatBuffer vertices = BufferUtils
				.createFloatBuffer(mesh.numVertices * 3);

		for (int i = 0; i < mesh.numTriangles * 3; i++)
		{
			indicess.put(i, mesh.triangleIndexBase.getInt(i * 4));
		}

		for (int i = 0; i < mesh.numVertices * 3; i++)
		{
			vertices.put(i, mesh.vertexBase.getFloat(i * 4));
		}

		md.setIndexBuffer(indicess);
		md.setVertexBuffer(vertices);

		md.updateVertexCount();
		jmeMesh.updateModelBound();

		return jmeMesh;
	}

}
