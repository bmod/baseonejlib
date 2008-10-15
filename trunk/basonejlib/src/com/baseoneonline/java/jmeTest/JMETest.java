package com.baseoneonline.java.jmeTest;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.baseoneonline.java.jmeMetaball.MetaBallSystem;
import com.baseoneonline.java.jmeTest.text.TTFont;
import com.baseoneonline.java.jmeTest.text.TextNode;
import com.jme.app.SimpleGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;

public class JMETest extends SimpleGame implements Const {

	public static void main(String[] args) {
		Logger.getLogger("com.jme").setLevel(Level.WARNING);
		JMETest app = new JMETest();
		app.start();
	}

	KeyBindingManager key = KeyBindingManager.getKeyBindingManager();
	
	private final float zoomSpeed = 30;
	
	private final Quaternion mballRotation
	
	private MetaBallSystem mbsys;

	@Override
	protected void simpleInitGame() {
		
		cam.setLocation(new Vector3f(0, 0, 70));
		cam.setFrustumFar(4000);
		//cam.setFrustumFar(4000);
		//cam.setFrustumNear(50);
		
		display.getRenderer().setBackgroundColor(
				new ColorRGBA(0.2f, 0.3f, 0.4f, 1));
		MouseInput.get().setCursorVisible(true);
		input.setEnabled(false);

		Font font = new Font("sans", Font.PLAIN, 30);
		TextNode tn = new TextNode(new TTFont(font));
		tn.setText("Hello AA AA A Java Monkey Engine");
		rootNode.attachChild(tn);
		
		mbsys = new MetaBallSystem();
		mbsys.setLocalScale(100);
		mbsys.setLocalTranslation(0,0,-1000);
		rootNode.attachChild(mbsys);
		
		key.add(CMD_ZOOM_IN, KeyInput.KEY_A);
		key.add(CMD_ZOOM_OUT, KeyInput.KEY_Z);

	}
	
	@Override
	protected void simpleUpdate() {
		
		mbsys.update(tpf);
		
		if (key.isValidCommand(CMD_ZOOM_IN,true)) {
			
			cam.setLocation(new Vector3f(0,0,cam.getLocation().z + (tpf*zoomSpeed)));
		}
		
		if (key.isValidCommand(CMD_ZOOM_OUT,true)) {
			cam.setLocation(new Vector3f(0,0,cam.getLocation().z - (tpf*zoomSpeed)));
		}
		
		
	}

}
