package com.baseoneonline.java.j3d;

import java.applet.Applet;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class J3DTest extends Applet {




	public J3DTest() {
		final GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		final Canvas3D canvas = new Canvas3D(config);
		add(canvas);

		final BranchGroup scene = createSceneGraph();

		final SimpleUniverse universe = new SimpleUniverse(canvas);
		universe.getViewingPlatform().setNominalViewingTransform();
		universe.addBranchGraph(scene);


	}

	private BranchGroup createSceneGraph() {
		final BranchGroup root = new BranchGroup();
		root.addChild(new ColorCube(.2));
		return root;
	}
}
