package com.baseoneonline.jlib.ardor3d.framework;

import com.ardor3d.renderer.Renderer;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;

public class SceneManager implements Manager {

	private final Node rootNode = new Node("SceneManagerRootNode");

	public void add(Spatial spatial) {
		rootNode.attachChild(spatial);
	}

	public void remove(Spatial spatial) {
		rootNode.detachChild(spatial);
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void update(double t) {
		rootNode.updateGeometricState(t, true);
	}

	public void render(Renderer r) {
		rootNode.onDraw(r);
	}

}
