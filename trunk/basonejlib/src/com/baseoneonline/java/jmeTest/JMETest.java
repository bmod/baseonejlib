package com.baseoneonline.java.jmeTest;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.baseoneonline.java.jmeMetaball.MetaBallSystem;
import com.jme.app.SimpleGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;

public class JMETest extends SimpleGame implements Const {

	private final float camDistance = 50;

	public static void main(final String[] args) {
		Logger.getLogger("com.jme").setLevel(Level.WARNING);
		final JMETest app = new JMETest();
		app.start();
	}

	KeyBindingManager key = KeyBindingManager.getKeyBindingManager();

	private final float zoomSpeed = 30;

	private MetaBallSystem mballSys;
	private MenuNode<File> menuNode;

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
		mballSys.setLocalTranslation(0, 0, -2000);
		mballSys.setLocalScale(100);
		rootNode.attachChild(mballSys);

		menuNode = new MenuNode<File>(model);
		rootNode.attachChild(menuNode);

		key.add(CMD_ZOOM_IN, KeyInput.KEY_A);
		key.add(CMD_ZOOM_OUT, KeyInput.KEY_Z);
		key.add(CMD_UP, KeyInput.KEY_UP);
		key.add(CMD_DOWN, KeyInput.KEY_DOWN);
		key.add(CMD_ENTER, KeyInput.KEY_RETURN);

	}

	private final Vector3f camTarget = new Vector3f(0, 0, camDistance);

	@Override
	protected void simpleUpdate() {

		final Vector3f pos = cam.getLocation();
		pos.y += (camTarget.y - pos.y) * tpf * 10;
		cam.setLocation(pos);

		mballSys.update(tpf);

		if (key.isValidCommand(CMD_UP, false)) {
			menuNode.prev();
			camTarget.y = menuNode.getSelectedLabel().getQuad()
					.getWorldTranslation().y;
		}
		if (key.isValidCommand(CMD_DOWN, false)) {
			menuNode.next();
			camTarget.y = menuNode.getSelectedLabel().getQuad()
					.getWorldTranslation().y;
		}
		if (key.isValidCommand(CMD_ENTER, true)) {
			File dir = model.getSelectedItem();
			if (null != dir && dir.isDirectory()) {
				model.setDirectory(dir);
			}
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
