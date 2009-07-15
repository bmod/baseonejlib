package com.baseoneonline.java.test.testVectorcycle;

import com.jme.curve.Curve;
import com.jme.intersection.CollisionResults;
import com.jme.intersection.PickResults;
import com.jme.math.Matrix3f;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.util.geom.BufferUtils;

public class PathBezier extends Curve {

	private static final long serialVersionUID = 1L;

	private static Vector3f tempVect = new Vector3f();

	public PathBezier() {
		super(null);
	}

	/**
	 * Constructor instantiates a new <code>PathBezier</code> object.
	 * 
	 * @param name
	 *            the name of the scene element. This is required for
	 *            identification and comparision purposes.
	 */
	public PathBezier(String name) {
		super(name);
	}

	/**
	 * Constructor instantiates a new <code>PathBezier</code> object. The
	 * control points that define the curve are supplied.
	 * 
	 * @param name
	 *            the name of the scene element. This is required for
	 *            identification and comparision purposes.
	 * @param controlPoints
	 *            the points that define the curve.
	 */
	public PathBezier(String name, Vector3f[] controlPoints) {
		super(name, controlPoints);
	}

	/**
	 * <code>getPoint</code> calculates a point on a Bezier curve from a given
	 * time value within the interval [0, 1]. If the value is zero or less, the
	 * first control point is returned. If the value is one or more, the last
	 * control point is returned. Using the equation of a Bezier Curve, the
	 * point at the interval is calculated and returned.
	 * 
	 * @see com.jme.curve.Curve#getPoint(float)
	 */
	@Override
	public Vector3f getPoint(float time, Vector3f point) {
		// first point
		if (time < 0) {
			BufferUtils.populateFromBuffer(point, getVertexBuffer(), 0);
			return point;
		}
		// last point.
		if (time >= 1) {
			BufferUtils.populateFromBuffer(point, getVertexBuffer(),
				getVertexCount() - 1);
			return point;
		}

		float muk = 1;
		float munk = (float) Math.pow(1 - time, getVertexCount() - 1);

		point.zero();

		for (int i = 0; i < getVertexCount(); i++) {
			int count = getVertexCount() - 1;
			int iCount = i;
			int diff = count - iCount;
			float blend = muk * munk;
			muk *= time;
			munk /= (1 - time);
			while (count >= 1) {
				blend *= count;
				count--;
				if (iCount > 1) {
					blend /= iCount;
					iCount--;
				}

				if (diff > 1) {
					blend /= diff;
					diff--;
				}
			}
			BufferUtils.populateFromBuffer(tempVect, getVertexBuffer(), i);
			point.x += tempVect.x * blend;
			point.y += tempVect.y * blend;
			point.z += tempVect.z * blend;
		}

		return point;
	}

	@Override
	public Vector3f getPoint(float time) {
		return getPoint(time, new Vector3f());
	}
	
	

	/**
	 * <code>getOrientation</code> calculates the rotation matrix for any given
	 * point along to the line to still be facing in the direction of the line.
	 * 
	 * @param time
	 *            the current time (between 0 and 1)
	 * @param precision
	 *            how accurate to (i.e. the next time) to check against.
	 * @return the rotation matrix.
	 * @see com.jme.curve.Curve#getOrientation(float, float)
	 */
	@Override
	public Matrix3f getOrientation(float time, float precision) {
		Matrix3f rotation = new Matrix3f();

		// calculate tangent
		Vector3f point = getPoint(time);
		Vector3f tangent =
			point.subtract(getPoint(time + precision)).normalizeLocal();
		// calculate normal
		Vector3f tangent2 = getPoint(time - precision).subtract(point);
		Vector3f normal = tangent.cross(tangent2).normalizeLocal();
		// calculate binormal
		Vector3f binormal = tangent.cross(normal).normalizeLocal();

		rotation.setColumn(0, tangent);
		rotation.setColumn(1, normal);
		rotation.setColumn(2, binormal);
		return rotation;
	}

	/**
	 * <code>getOrientation</code> calculates the rotation matrix for any given
	 * point along to the line to still be facing in the direction of the line.
	 * A up vector is supplied, this keep the rotation matrix following the
	 * line, but insures the object's up vector is not drastically changed.
	 * 
	 * @param time
	 *            the current time (between 0 and 1)
	 * @param precision
	 *            how accurate to (i.e. the next time) to check against.
	 * @return the rotation matrix.
	 * @see com.jme.curve.Curve#getOrientation(float, float)
	 */
	@Override
	public Matrix3f getOrientation(float time, float precision, Vector3f up) {
		if (up == null) {
			return getOrientation(time, precision);
		}
		Matrix3f rotation = new Matrix3f();

		// calculate tangent
		Vector3f tangent =
			getPoint(time).subtract(getPoint(time + precision))
					.normalizeLocal();

		// calculate binormal
		Vector3f binormal = tangent.cross(up).normalizeLocal();

		// calculate normal
		Vector3f normal = binormal.cross(tangent).normalizeLocal();

		rotation.setColumn(0, tangent);
		rotation.setColumn(1, normal);
		rotation.setColumn(2, binormal);

		return rotation;
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme.scene.Spatial#hasCollision(com.jme.scene.Spatial,
	 * com.jme.intersection.CollisionResults)
	 */
	@Override
	public void findCollisions(Spatial scene, CollisionResults results) {
	// TODO Auto-generated method stub

	}

	@Override
	public boolean hasCollision(Spatial scene, boolean checkTriangles) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme.scene.Spatial#doPick(com.jme.math.Ray,
	 * com.jme.intersection.PickResults)
	 */
	@Override
	public void findPick(Ray toTest, PickResults results) {
	// TODO Auto-generated method stub

	}

}