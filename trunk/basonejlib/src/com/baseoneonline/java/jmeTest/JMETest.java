package com.baseoneonline.java.jmeTest;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.baseoneonline.java.jmeMetaball.MetaBallSystem;
import com.baseoneonline.java.jmeTest.text.FontQuad;
import com.jme.app.SimpleGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;

public class JMETest extends SimpleGame implements Const {

	private final float camDistance = 50;

	public static void main(String[] args) {
		Logger.getLogger("com.jme").setLevel(Level.WARNING);
		JMETest app = new JMETest();
		app.start();
	}

	KeyBindingManager key = KeyBindingManager.getKeyBindingManager();

	private final float zoomSpeed = 30;

	private MetaBallSystem mballSys;

	private final ArrayList<TextLabel2D> labels = new ArrayList<TextLabel2D>();
	DirectoryModel model = new DirectoryModel(new File(System
			.getProperty("user.home")));

	@Override
	protected void simpleInitGame() {

		cam.setLocation(new Vector3f(0, 0, camDistance));
		cam.setFrustumFar(3000);
		// cam.setFrustumFar(4000);
		// cam.setFrustumNear(50);

		display.getRenderer().setBackgroundColor(
				new ColorRGBA(0.2f, 0.3f, 0.4f, 1));
		MouseInput.get().setCursorVisible(true);
		input.setEnabled(false);

		mballSys = new MetaBallSystem(5);
		mballSys.setLocalTranslation(0, 0, -1500);
		mballSys.setLocalScale(100);
		rootNode.attachChild(mballSys);

		float spacing = 2;
		File[] files = model.getFiles();
		Node menuNode = new Node("MenuNode");
		for (int i = 0; i < files.length; i++) {
			TextLabel2D label = new TextLabel2D(files[i].getName());
			labels.add(label);
			FontQuad q = label.getQuad(2f);
			q.setLocalTranslation(0, i * -spacing, 0);
			menuNode.attachChild(q);
		}
		menuNode.setLocalTranslation(-5, 13, 0);
		rootNode.attachChild(menuNode);
		updateMenuSelection();

		key.add(CMD_ZOOM_IN, KeyInput.KEY_A);
		key.add(CMD_ZOOM_OUT, KeyInput.KEY_Z);
		key.add(CMD_UP, KeyInput.KEY_UP);
		key.add(CMD_DOWN, KeyInput.KEY_DOWN);
		key.add(CMD_ENTER, KeyInput.KEY_RETURN);

	}

	private void updateMenuSelection() {
		for (int i = 0; i < labels.size(); i++) {
			if (model.getSelectedIndex() == i) {
				labels.get(i).getQuad().setLocalScale(1.2f);
				camTarget.y = labels.get(i).getQuad().getWorldTranslation().y;
				
			} else {
				labels.get(i).getQuad().setLocalScale(1);
			}
		}

	}

	private final Vector3f camTarget = new Vector3f(0, 0, camDistance);

	@Override
	protected void simpleUpdate() {

		Vector3f pos = cam.getLocation();
		pos.y += (camTarget.y - pos.y) * tpf * 10;
		cam.setLocation(pos);

		mballSys.update(tpf);

		if (key.isValidCommand(CMD_UP, false)) {
			int idx = model.getSelectedIndex() - 1;
			if (idx < 0) {
				idx = model.size() - 1;
			}
			model.setSelectedIndex(idx);
			updateMenuSelection();
		}
		if (key.isValidCommand(CMD_DOWN, false)) {
			int idx = model.getSelectedIndex() + 1;
			if (idx > model.size() - 1) {
				idx = 0;
			}
			model.setSelectedIndex(idx);
			updateMenuSelection();
		}
		if (key.isValidCommand(CMD_ENTER, true)) {

		}

		if (key.isValidCommand(CMD_ZOOM_IN, true)) {
			cam.setLocation(cam.getLocation().add(
					new Vector3f(0, 0, (tpf * zoomSpeed))));
		}

		if (key.isValidCommand(CMD_ZOOM_OUT, true)) {
			cam.setLocation(cam.getLocation().add(
					new Vector3f(0, 0, (-tpf * zoomSpeed))));
		}

	}

}
