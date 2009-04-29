package com.baseoneonline.java.jme;

import com.jme.input.MouseInput;
import com.jme.light.PointLight;
import com.jme.math.Plane;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;

public class JMEUtil {
	public static void letThereBeLight(Node rootNode) {
		PointLight light = new PointLight();
		light.setLocation(new Vector3f(200, 300, 100));
		light.setEnabled(true);
		light.setDiffuse(ColorRGBA.white);
		LightState ls =
			DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		ls.attach(light);
		rootNode.setRenderState(ls);
		rootNode.updateRenderState();

	}

	/**
	 * @return The position where the mouse pointer intersects with a plane
	 */
	public static Vector3f getMousePosition3D(final Plane p) {
		final Vector2f mousePos =
			new Vector2f(MouseInput.get().getXAbsolute(), MouseInput.get()
					.getYAbsolute());
		final Vector3f point1 =
			DisplaySystem.getDisplaySystem().getWorldCoordinates(mousePos, 0);
		final Vector3f point2 =
			DisplaySystem.getDisplaySystem().getWorldCoordinates(mousePos, 1);
		final Vector3f direction =
			point2.subtractLocal(point1).normalizeLocal();
		final Ray ray = new Ray(point1, direction);
		final Vector3f loc = new Vector3f();
		ray.intersectsWherePlane(p, loc);
		return loc;
	}

	public static void applyBlendState(Quad q) {
		BlendState bs =
			DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		bs.setEnabled(true);
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		q.setRenderState(bs);
	}

}
