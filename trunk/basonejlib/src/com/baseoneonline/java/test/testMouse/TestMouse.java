package com.baseoneonline.java.test.testMouse;

import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.OrbitCamNode;
import com.jme.app.SimpleGame;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;

public class TestMouse extends SimpleGame {

	public static void main(String[] args) {
		new TestMouse().start();
	}

	OrbitCamNode orbitCam;
	Cursor cursor;
	TileGridNode tileGridNode;
	Grid<Entity> grid;
	
	Vector2f mouse = new Vector2f();

	@Override
	protected void simpleInitGame() {
		MouseInput.get().setCursorVisible(true);
		input.setEnabled(false);

		int w = 16;
		int h = 10;
		float tsize = .5f;
		
		grid = new Grid<Entity>(w,h);
		tileGridNode = new TileGridNode(w, h, tsize);

		rootNode.attachChild(tileGridNode);

		cursor = new Cursor(tileGridNode);
		cursor.getLocalTranslation().y = .1f;
		rootNode.attachChild(cursor);

		orbitCam = new OrbitCamNode(cam);
		orbitCam.setAzimuth(FastMath.HALF_PI * .6f);
		orbitCam.setDistance(6);
		rootNode.attachChild(orbitCam);

		JMEUtil.letThereBeLight(rootNode);
		
		Entity entity = new Entity(.4f);

	}

	@Override
	protected void simpleUpdate() {
		mouse.x = MouseInput.get().getXAbsolute();
		mouse.y = MouseInput.get().getYAbsolute();
		
		updateCamera();
		cursor.update(mouse);
		pick();
	}
	
	private void pick() {
		
	}
	
	private void updateCamera() {
		float mx = (mouse.x / display.getWidth()*2)-1;
		float my = (mouse.y / display.getHeight()*2)-1;
		
		orbitCam.getLocalTranslation().x = mx*2;
		orbitCam.getLocalTranslation().z = -my;
		orbitCam.getLocalRotation().y = mx*.03f;
	}

}



