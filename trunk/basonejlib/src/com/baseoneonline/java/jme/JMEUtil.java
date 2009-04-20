package com.baseoneonline.java.jme;

import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

public class JMEUtil {
	public static void letThereBeLight(Node rootNode) {
		PointLight light = new PointLight();
		light.setLocation(new Vector3f(200, 300, 100));
		light.setEnabled(true);
		light.setDiffuse(ColorRGBA.white);
		LightState ls = DisplaySystem.getDisplaySystem().getRenderer()
				.createLightState();
		ls.attach(light);
		rootNode.setRenderState(ls);
		rootNode.updateRenderState();

	}
}
