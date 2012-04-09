package com.baseoneonline.jlib.ardor3d.jbullet;

import com.ardor3d.scenegraph.Spatial;
import com.baseoneonline.jlib.ardor3d.Convert;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

/**
 * Does the actual transformation of the Ardor3d {@link Spatial}
 * 
 */
class PhysicsMotionState extends MotionState
{

	private final Spatial spatial;

	public PhysicsMotionState(Spatial spatial)
	{
		this.spatial = spatial;
	}

	@Override
	public Transform getWorldTransform(Transform out)
	{
		com.ardor3d.math.Transform tmp = com.ardor3d.math.Transform
				.fetchTempInstance();

		tmp.set(spatial.getTransform());
		Convert.toTransform(tmp, out);

		com.ardor3d.math.Transform.releaseTempInstance(tmp);
		return out;
	}

	@Override
	public void setWorldTransform(Transform worldTrans)
	{
		com.ardor3d.math.Transform tmp = com.ardor3d.math.Transform
				.fetchTempInstance();

		Convert.toTransform(worldTrans, tmp);
		spatial.setTransform(tmp);

		com.ardor3d.math.Transform.releaseTempInstance(tmp);
	}
}
