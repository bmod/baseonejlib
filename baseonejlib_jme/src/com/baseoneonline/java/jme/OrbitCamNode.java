package com.baseoneonline.java.jme;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;

public class OrbitCamNode extends Node {

	
	private final Node aziNode;
	private final Node camNode;
	
	private final Camera cam;
	
	public OrbitCamNode(Camera cam) {
		this.cam = cam;
		aziNode = new Node();
		camNode = new Node();
		aziNode.attachChild(camNode);
		attachChild(aziNode);
		setDistance(15);
		setAzimuth(FastMath.DEG_TO_RAD*30);
		setHeading(FastMath.DEG_TO_RAD*30);
	}
	
	public void setDistance(float d) {
		camNode.getLocalTranslation().z = d;
	}
	
	public float getDistance() {
		return camNode.getLocalTranslation().z;
	}
	
	public void setAzimuth(float n) {
		aziNode.getLocalRotation().fromAngles(-n, 0, 0);
	}
	
	public float getAzimuth() {
		return -aziNode.getLocalRotation().toAngles(null)[0];
	}
	
	public void setHeading(float n) {
		getLocalRotation().fromAngles(0, n, 0);
	}
	
	public float getHeading() {
		return getLocalRotation().toAngles(null)[1];
	}
	
	
	
	
	@Override
	public void updateGeometricState(float time, boolean initiator) {
		super.updateGeometricState(time, initiator);
		cam.setLocation(camNode.getWorldTranslation());
		cam.lookAt(getWorldTranslation(), Vector3f.UNIT_Y);
	}
	
}
