package com.baseoneonline.java.test.testVectorcycle;

import java.util.ArrayList;

import com.baseoneonline.java.jme.JMEUtil;
import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class Road {

	private final ArrayList<RoadSegment> segments = new ArrayList<RoadSegment>();
	private RoadSegment lastSegment;
	private final int numTracks = 3;
	private final float width;

	private ITerrain terrain;
	private float roadOffset;

	private final Vector2f anglePrecision = new Vector2f(0, 0.001f);

	public Road(float width) {
		this.width = width;
	}



	public Road(int width, ITerrain terrain, float roadOffset) {
		this.terrain = terrain;
		this.width = width;
		this.roadOffset = roadOffset;
	}

	public void addSegment(float dist, float angle) {
		RoadSegment seg;
		if (null != lastSegment) {
			seg = new RoadSegment(lastSegment.curve.end, dist,
					lastSegment.endAngle, lastSegment.endAngle + angle);
		} else {
			seg = new RoadSegment(new Vector2f(), dist, 0, angle);
		}
		lastSegment = seg;
		segments.add(seg);
	}

	public Node getRibbons() {
		Node n = new Node();
		float w = width + (width / numTracks) * 2;
		for (RoadSegment seg : segments) {
			RoadRibbon ribbon = new RoadRibbon(seg, w, terrain, roadOffset);
			ribbon.setModelBound(new BoundingBox());
			ribbon.updateModelBound();
			n.attachChild(ribbon);
		}
		return n;
	}

	/**
	 * @return A set of displayable lines for debugging
	 */
	public Spatial getLines() {
		Node n = new Node();
		int precision = 8;

		float ts = -width / 2;
		float tw = width / (numTracks - 1);

		ColorRGBA col = ColorRGBA.red;
		for (RoadSegment seg : segments) {
			for (int trk = 0; trk < numTracks; trk++) {
				Vector3f[] vtc = new Vector3f[precision + 1];
				for (int i = 0; i <= precision; i++) {
					float t = (float) i / (float) precision;
					Vector2f p = seg.curve.getPoint(t, ts + (trk * tw));
					if (null != terrain) {
						vtc[i] = new Vector3f(p.x, terrain.getHeight(p), p.y);
					} else {
						vtc[i] = new Vector3f(p.x, 0, p.y);
					}
				}
				Line ln = JMEUtil.createLine(vtc, col);
				n.attachChild(ln);
			}
		}

		return n;
	}

	public Matrix3f getOrientation(Vector2f p) {
		Matrix3f rotation = new Matrix3f();
		// calculate tangent
		Vector3f point = getPoint(p);
		Vector3f tangent = point.subtract(getPoint(p.add(anglePrecision)))
				.normalizeLocal();
		// calculate normal
		Vector3f tangent2 = getPoint(p.subtract(anglePrecision))
				.subtract(point);
		Vector3f normal = tangent.cross(tangent2).normalizeLocal();
		// calculate binormal
		Vector3f binormal = tangent.cross(normal).normalizeLocal();

		rotation.setColumn(0, tangent);
		rotation.setColumn(1, normal);
		rotation.setColumn(2, binormal);
		return rotation;
	}

	public Matrix3f getOrientation(Vector2f p, Vector3f up) {
		Matrix3f rotation = new Matrix3f();
		// calculate tangent
		Vector3f point = getPoint(p);
		Vector3f tangent = point.subtract(getPoint(p.add(anglePrecision)))
				.normalizeLocal();
		// calculate normal
		Vector3f normal = tangent.cross(tangent).normalizeLocal();
		// calculate binormal
		Vector3f binormal = tangent.cross(up).normalizeLocal();

		rotation.setColumn(0, tangent);
		rotation.setColumn(1, normal);
		rotation.setColumn(2, binormal);
		return rotation;
	}

	public Vector3f getPoint(Vector2f pos) {
		int seg = (int) FastMath.floor(pos.y);
		float t = pos.y - seg;
		if (seg >= 0 && seg < segments.size()) {
			RoadSegment segment = segments.get(seg);
			Vector2f p = segment.curve.getPoint(t, pos.x);
			return new Vector3f(p.x, terrain.getHeight(p) + roadOffset, p.y);
		} else {
			return new Vector3f();
		}
	}

	public int numSegments() {
		return segments.size();
	}

	public float getWidth() {
		return width;
	}

}
