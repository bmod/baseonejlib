package com.baseoneonline.java.test;

import java.io.FileInputStream;

import com.jme.app.SimpleGame;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jmex.model.collada.ColladaImporter;

public class TestBorderCompany extends SimpleGame {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		new TestBorderCompany().start();
	}

	@Override
	protected void simpleInitGame() {
		display.getRenderer().setBackgroundColor(ColorRGBA.darkGray);

		final String fileName = "d:/temp/cube.dae";
		// rootNode.attachChild(new Box("box", new Vector3f(), 1, 1, 1));

		//
		// rootNode.attachChild(JMEUtil.loadObj(fileName, null));
		rootNode.attachChild(loadDAE(fileName));

	}

	private Spatial loadDAE(final String name) {
		try {
			ColladaImporter.load(new FileInputStream(name), "Name");
			return ColladaImporter.getModel();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
