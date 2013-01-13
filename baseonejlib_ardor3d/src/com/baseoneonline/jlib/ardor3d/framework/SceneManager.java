package com.baseoneonline.jlib.ardor3d.framework;

import com.ardor3d.renderer.Renderer;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;

public class SceneManager {

	private static SceneManager instance;
	private Node root = new Node();

	private SceneManager() {
	}

	public void setRoot(final Node root) {
		this.root = root;
	}

	public Node getRoot() {
		return root;
	}

	public void update(final double t) {
		root.updateGeometricState(t, true);
	}

	public void render(final Renderer r) {
		root.onDraw(r);
	}

	public static SceneManager get() {
		if (instance == null)
			instance = new SceneManager();
		return instance;
	}

	public void add(final Spatial spatial) {
		root.attachChild(spatial);
	}

	public void remove(final Spatial spatial) {
		root.detachChild(spatial);
	}

}
