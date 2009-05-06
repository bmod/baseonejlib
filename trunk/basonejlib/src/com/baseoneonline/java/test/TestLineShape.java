package com.baseoneonline.java.test;

import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.NGon;
import com.baseoneonline.java.jme.OrbitCamNode;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;

public class TestLineShape extends BasicFixedRateGame {

	public static void main(String[] args) {
		new TestLineShape().start();
	}

	OrbitCamNode camNode;

	@Override
	protected void initFixedRateGame() {
		MouseInput.get().setCursorVisible(true);
		Box b = new Box("b", new Vector3f(), 1, 1, 1);
		// rootNode.attachChild(b);

		camNode = new OrbitCamNode(cam);
		rootNode.attachChild(camNode);

		NGon lnX = new NGon(4,ColorRGBA.red);
		
		rootNode.attachChild(lnX);

		JMEUtil.letThereBeLight(rootNode);
	}

	@Override
	protected void updateLoop(float t) {
		float mx = MouseInput.get().getXAbsolute();
		float my = MouseInput.get().getYAbsolute();

		camNode.setHeading(mx / display.getWidth() * FastMath.TWO_PI);
		camNode.setAzimuth(my / display.getHeight() * FastMath.HALF_PI);
	}

}

