package com.baseoneonline.java.blips;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Spatial;

public class FollowCam {

	private final Camera cam;
	private final Vector3f lookAt = new Vector3f();
	private final Vector3f camLoc = new Vector3f();
	private Spatial target;

	private final float cam_interpolation = .02f;
	private final float look_interpolation = .1f;

	private final Vector3f offset = new Vector3f(0,-30,30);

	public FollowCam(final Camera cam, final Spatial target) {
		this.cam = cam;
		this.target = target;
	}

	public void update() {
		final Vector3f targetPos = target.getWorldTranslation();
		final Vector3f targetCam = targetPos.add(offset);
		lookAt.x += (targetPos.x - lookAt.x)*look_interpolation;
		lookAt.y += (targetPos.y - lookAt.y)*look_interpolation;
		lookAt.z += (targetPos.z - lookAt.z)*look_interpolation;
		camLoc.x += (targetCam.x - camLoc.x)*cam_interpolation;
		camLoc.y += (targetCam.y - camLoc.y)*cam_interpolation;
		camLoc.z += (targetCam.z - camLoc.z)*cam_interpolation;
		cam.setLocation(camLoc);
		cam.lookAt(lookAt, Vector3f.UNIT_Z);
	}

	public void setTarget(final Spatial target) {
		this.target = target;
	}

	public Spatial getTarget() {
		return target;
	}
}
