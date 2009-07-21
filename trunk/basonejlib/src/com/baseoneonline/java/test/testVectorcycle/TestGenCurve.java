package com.baseoneonline.java.test.testVectorcycle;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.baseoneonline.java.jme.LineGrid;
import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.nanoxml.XMLParseException;
import com.jme.app.SimpleGame;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

public class TestGenCurve extends SimpleGame {

	public static void main(String[] args) {
		Logger.getLogger("com.jme").setLevel(Level.WARNING);
		new TestGenCurve().start();
	}

	@Override
	protected void simpleInitGame() {
		cam.setLocation(new Vector3f(3, 3, 3));
		cam.lookAt(new Vector3f(), Vector3f.UNIT_Y);

		rootNode.attachChild(new LineGrid(1));
		rootNode.attachChild(TestRods.get());

		Spatial path = loadData("path.xml");
		if (null != path) {
			rootNode.attachChild(path);
		}

	}

	private Spatial loadData(String fname) {
		try {
			XMLElement xml = new XMLElement();
			xml.parseFromReader(new FileReader(fname));
			return XMLToRibbon(xml);
		} catch (XMLParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private RibbonGroup XMLToRibbon(XMLElement xml) {
		RibbonGroup path = new RibbonGroup();
		for (XMLElement x : xml.getChildren("segment")) {
			float dist = x.getFloatAttribute("distance");
			Vector3f angle = new Vector3f();
			angle.x = -x.getFloatAttribute("climb", 0) * FastMath.DEG_TO_RAD;
			angle.y = x.getFloatAttribute("corner", 0) * FastMath.DEG_TO_RAD;
			angle.z = x.getFloatAttribute("bank", 0) * FastMath.DEG_TO_RAD;
			float width = x.getFloatAttribute("width", .5f);
			float tension = x.getFloatAttribute("tension", .5f);
			path.addSegment(dist, angle, width, tension);
		}
		return path;
	}

}



