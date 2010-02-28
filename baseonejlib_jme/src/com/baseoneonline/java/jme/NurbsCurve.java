package com.baseoneonline.java.jme;

import java.util.logging.Logger;

import com.jme.curve.Curve;
import com.jme.intersection.CollisionResults;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.util.geom.BufferUtils;

/**
 * 
 * Shamelessly ripped from <a href="http://nccastaff.bournemouth.ac.uk/jmacey/RobTheBloke/www/opengl_programming.html"
 * >http://nccastaff.bournemouth.ac.uk/jmacey/RobTheBloke/www/opengl_programming
 * .html</a>
 * 
 * @author bask
 * 
 */
public class NurbsCurve extends Curve {

	private final int degree;
	private final int order;
	private final int num_knots;
	private float maxKnotValue;
	private float[] knots;
	
	private Vector3f tempVect = new Vector3f();

	public NurbsCurve(String name, final Vector3f[] pts) {
		this(name, pts, 3);
	}

	public NurbsCurve(String name, final Vector3f[] pts, final int degree) {
		super(name, pts);
		this.degree = degree;
		order = degree + 1;
		num_knots = pts.length + order;
		createKnots();
	}

	private void createKnots() {
		knots = new float[num_knots];
		float v = 0;
		for (int i = 0; i < num_knots; i++) {
			knots[i] = v;
			if (i >= degree && i < num_knots - degree - 1) {
				v += 1;
			}
		}
		maxKnotValue = v;
	}


	public Vector3f getPoint(final float t, Vector3f store) {
		if (null == store) store = new Vector3f();
		final int len = getNumCVs();

		if (t <= 0) {
			BufferUtils.populateFromBuffer(store, getVertexBuffer(), 0);
			return store;
		}
		if (t >= 1) {
			BufferUtils.populateFromBuffer(store, getVertexBuffer(), getVertexCount()-1);
			return store;
		}

		store.zero();
		float valSum = 0;
		for (int i = 0; i < len; i++) {
			final float v = coxDeBoor(t * maxKnotValue, i, degree);
			valSum += v;
			BufferUtils.populateFromBuffer(tempVect, getVertexBuffer(), i);
			store.addLocal(tempVect.mult(v));
		}
		store.divideLocal(valSum);
		// System.out.println(store);
		return store;
	}


	private float coxDeBoor(final float t, final int k, final int deg) {
		float b1;
		float b2;

		if (deg == 0) {
			if (knots[k] <= t && t <= knots[k + 1]) { return 1.0f; }
			return 0.0f;
		}

		if (knots[k + deg] != knots[k]) b1 = ((t - knots[k]) / (knots[k + deg] - knots[k]))
				* coxDeBoor(t, k, deg - 1);
		else
			b1 = 0.0f;

		if (knots[k + deg + 1] != knots[k + 1]) b2 = ((knots[k + deg + 1] - t) / (knots[k
				+ deg + 1] - knots[k + 1]))
				* coxDeBoor(t, k + 1, deg - 1);
		else
			b2 = 0.0f;

		return b1 + b2;

	}


	public int getNumCVs() {
		return (int)getVertexBuffer().capacity()/3;
	}

	@Override
	public Matrix3f getOrientation(float time, float precision) {
		Matrix3f rotation = new Matrix3f();

		//calculate tangent
		Vector3f point = getPoint(time);
		Vector3f tangent = point.subtract(getPoint(time + precision)).normalizeLocal();
		//calculate normal
		Vector3f tangent2 = getPoint(time - precision).subtract(point);
		Vector3f normal = tangent.cross(tangent2).normalizeLocal();
		//calculate binormal
		Vector3f binormal = tangent.cross(normal).normalizeLocal();

		rotation.setColumn(0, tangent);
		rotation.setColumn(1, normal);
		rotation.setColumn(2, binormal);
		return rotation;

	}

	@Override
	public Matrix3f getOrientation(float time, float precision, Vector3f up) {
		if (up == null) {
			return getOrientation(time, precision);
		}
		Matrix3f rotation = new Matrix3f();

		//calculate tangent
		Vector3f tangent = getPoint(time).subtract(getPoint(time + precision)).normalizeLocal();

		//calculate binormal
		Vector3f binormal = tangent.cross(up).normalizeLocal();

		//calculate normal
		Vector3f normal = binormal.cross(tangent).normalizeLocal();

		rotation.setColumn(0, tangent);
		rotation.setColumn(1, normal);
		rotation.setColumn(2, binormal);

		return rotation;	}

	@Override
	public Vector3f getPoint(float time) {
		return getPoint(time, new Vector3f());
	}



	@Override
	public void findCollisions(Spatial scene, CollisionResults results,
			int requiredOnBits) {
		Logger.getLogger(getClass().getName()).warning("NOT IMPLEMENTED!!!");
	}

	@Override
	public boolean hasCollision(Spatial scene, boolean checkTriangles,
			int requiredOnBits) {
		Logger.getLogger(getClass().getName()).warning("NOT IMPLEMENTED!!!");
		return false;
	}

}
