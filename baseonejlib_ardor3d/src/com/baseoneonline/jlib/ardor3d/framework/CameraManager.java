package com.baseoneonline.jlib.ardor3d.framework;

import com.ardor3d.renderer.Camera;

public class CameraManager {
	private static CameraManager instance;

	public static CameraManager get() {
		if (instance == null)
			instance = new CameraManager();
		return instance;
	}

	private Camera camera;
	private CameraController controller;

	private CameraManager() {

	}

	public void setController(CameraController controller) {
		this.controller = controller;
	}

	public CameraController getController() {
		return controller;
	}

	public void update(double t) {
		if (null != controller)
			controller.update(t, camera);
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Camera getCamera() {
		if (camera == null)
			throw new RuntimeException("Camera not yet initialized!");
		return camera;
	}
}
