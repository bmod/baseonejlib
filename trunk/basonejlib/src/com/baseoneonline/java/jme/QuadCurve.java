package com.baseoneonline.java.jme;

import com.jme.curve.Curve;
import com.jme.intersection.CollisionResults;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.util.geom.BufferUtils;

/**
 * <code>QuadCurve</code> uses an ordered-list of three-dimensional points and
 * the equation: x(t) = Sum(n, i=0) Bn,i(t)Pi <br>
 * t [0,1] <br>
 * Bn,i(t) = C(n;i)t^i(1-t)^(n-i) <br>
 * The input (t) provides the current point of the curve at a interval [0,1]
 * where 0 is the first control point and 1 is the second control point.
 * 
 * @author Mark Powell
 * @version $Id: QuadCurve.java 4204 2009-03-29 15:06:10Z ian.phillips $
 */
public class QuadCurve extends Curve {

	private static final long serialVersionUID = 1L;

	private static Vector3f tempVect = new Vector3f();

	public QuadCurve() {
		super(null);
	}

	/**
	 * Constructor instantiates a new <code>QuadCurve</code> object.
	 * 
	 * @param name
	 *            the name of the scene element. This is required for
	 *            identification and comparision purposes.
	 */
	public QuadCurve(final String name) {
		super(name);
	}

	/**
	 * Constructor instantiates a new <code>QuadCurve</code> object. The control
	 * points that define the curve are supplied.
	 * 
	 * @param name
	 *            the name of the scene element. This is required for
	 *            identification and comparision purposes.
	 * @param controlPoints
	 *            the points that define the curve.
	 */
	public QuadCurve(final String name, final Vector3f[] controlPoints) {
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
	public Vector3f getPoint(final float time, final Vector3f point) {
		// first point
		if (time < 0) {
			BufferUtils.populateFromBuffer(point, getVertexBuffer(), 0);
			return point;
		}
		// last point.
		if (time > 1) {
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
	public Vector3f getPoint(final float time) {
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
	public Matrix3f getOrientation(final float time, final float precision) {
		final Matrix3f rotation = new Matrix3f();

		// calculate tangent
		final Vector3f point = getPoint(time);
		final Vector3f tangent = point.subtract(getPoint(time + precision))
				.normalizeLocal();
		// calculate normal
		final Vector3f tangent2 = getPoint(time - precision).subtract(point);
		final Vector3f normal = tangent.cross(tangent2).normalizeLocal();
		// calculate binormal
		final Vector3f binormal = tangent.cross(normal).normalizeLocal();

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
	public Matrix3f getOrientation(final float time, final float precision,
			final Vector3f up) {
		if (up == null) { return getOrientation(time, precision); }
		final Matrix3f rotation = new Matrix3f();

		// calculate tangent
		final Vector3f tangent = getPoint(time).subtract(
				getPoint(time + precision)).normalizeLocal();

		// calculate binormal
		final Vector3f binormal = tangent.cross(up).normalizeLocal();

		// calculate normal
		final Vector3f normal = binormal.cross(tangent).normalizeLocal();

		rotation.setColumn(0, tangent);
		rotation.setColumn(1, normal);
		rotation.setColumn(2, binormal);

		return rotation;
	}

	@Override
	public void findCollisions(final Spatial scene,
			final CollisionResults results, final int requiredOnBits) {
	// TODO Auto-generated method stub

	}

	@Override
	public boolean hasCollision(final Spatial scene,
			final boolean checkTriangles, final int requiredOnBits) {
		// TODO Auto-generated method stub
		return false;
	}

}
