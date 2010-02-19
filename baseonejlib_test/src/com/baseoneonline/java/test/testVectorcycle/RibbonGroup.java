package com.baseoneonline.java.test.testVectorcycle;

import com.jme.bounding.BoundingBox;
import com.jme.curve.BezierCurve;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.util.geom.BufferUtils;

public class RibbonGroup extends Node {

	private final Vector3f start = new Vector3f();
	Node pnode = new Node();
	Node cnode = new Node();
	Vector3f angle = new Vector3f();

	public RibbonGroup() {
		super("Ribbon");
		pnode.attachChild(cnode);
	}

	public void addSegment(float dist, Vector3f a, float width) {
		addSegment(dist, a, width, .7f);
	}

	public void addSegment(float dist, Vector3f a, float width, float tension) {

		// Create probes and step (like logo turtle)
		angle.addLocal(a);
		cnode.setLocalTranslation(0, 0, dist / 2);
		pnode.setLocalTranslation(0, 0, 0);
		pnode.updateGeometricState(1, true);
		Vector3f mid = start.add(cnode.getWorldTranslation());

		pnode.setLocalTranslation(0, 0, 0);
		pnode.getLocalRotation().fromAngles(angle.x, angle.y, angle.z);
		pnode.updateGeometricState(1, true);
		Vector3f end = mid.add(cnode.getWorldTranslation());

		Vector3f m1 = start.clone();
		Vector3f m2 = end.clone();
		m1.interpolate(mid, tension);
		m2.interpolate(mid, tension);

		Vector3f[] seg = new Vector3f[4];
		seg[0] = start;
		seg[1] = m1;
		seg[2] = m2;
		seg[3] = end;

		Vector3f[] nml = new Vector3f[4];
		nml[0] = Vector3f.UNIT_Y;
		nml[1] = Vector3f.UNIT_Y;
		nml[2] = Vector3f.UNIT_Y;
		nml[3] = Vector3f.UNIT_Y;

		ColorRGBA[] col = new ColorRGBA[4];
		col[0] = ColorRGBA.white;
		col[1] = ColorRGBA.white;
		col[2] = ColorRGBA.white;
		col[3] = ColorRGBA.white;

		BezierCurve bz = new BezierCurve("bz", seg);
		bz.setLightCombineMode(LightCombineMode.Off);
		
		bz.setColorBuffer(BufferUtils.createFloatBuffer(col));
		bz.setNormalBuffer(BufferUtils.createFloatBuffer(nml));
		attachChild(bz);

		PolyRibbon ribbon = new PolyRibbon(bz, width);
		ribbon.setModelBound(new BoundingBox());
		ribbon.updateModelBound();
		attachChild(ribbon);

		start.set(end);
		setModelBound(new BoundingBox());
		updateModelBound();
	}

}