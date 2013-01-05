package com.baseoneonline.jlib.ardor3d.jbullet;

import com.ardor3d.scenegraph.Spatial;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class BulletMotionState extends MotionState {

	private final Spatial spatial;
	private final Transform bTransform = new Transform();
	private final com.ardor3d.math.Transform transform = new com.ardor3d.math.Transform();

	public BulletMotionState(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public Transform getWorldTransform(Transform out) {
		return BulletConvert.convert(spatial.getTransform(), bTransform);
	}

	@Override
	public void setWorldTransform(Transform worldTrans) {
		spatial.setTransform(BulletConvert.convert(worldTrans, transform));
	}
}
