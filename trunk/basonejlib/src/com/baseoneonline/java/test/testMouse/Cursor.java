package com.baseoneonline.java.test.testMouse;

import java.util.HashMap;

import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.input.MouseInput;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class Cursor extends Quad {

	private final HashMap<CursorState, TextureState> texStates = new HashMap<CursorState, TextureState>();

	private CursorState state;
	private Spatial trackObject = null;
	Vector3f offset = new Vector3f(22, -12, 0);
	Vector3f screenPos = new Vector3f();
	float mix = 0;
	float tmix = 0f;
	Vector3f trackPos = new Vector3f();
	Vector3f position = new Vector3f();

	public static enum CursorState {
		Default, Open, Grab
	}

	public Cursor() {
		super("Cursor", 64, 64);
		texStates.put(CursorState.Default, getTex("hand_default.png"));
		texStates.put(CursorState.Open, getTex("hand_open.png"));
		texStates.put(CursorState.Grab, getTex("hand_grab.png"));

		final BlendState bs = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		bs.setBlendEnabled(true);
		// bs.setTestEnabled(true);
		// bs.setTestFunction(BlendState.TestFunction.GreaterThan);
		// bs.setReference(0.3f);
		bs.setEnabled(true);
		setRenderState(bs);
		setLightCombineMode(LightCombineMode.Off);
		setState(CursorState.Default);
	}

	public void setState(final CursorState s) {
		state = s;
		setRenderState(texStates.get(s));
		updateRenderState();
	}

	private TextureState getTex(final String name) {
		final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setTexture(TextureManager.loadTexture(getClass().getClassLoader()
				.getResource("com/baseoneonline/java/assets/hand/" + name),
				MinificationFilter.Trilinear, MagnificationFilter.Bilinear));
		ts.setEnabled(true);
		return ts;
	}

	@Override
	public void updateGeometricState(final float time, final boolean initiator) {
		screenPos.x = MouseInput.get().getXAbsolute();
		screenPos.y = MouseInput.get().getYAbsolute();
		if (null != trackObject) {
			trackPos.set(DisplaySystem.getDisplaySystem().getRenderer()
					.getCamera().getScreenCoordinates(
							trackObject.getWorldTranslation()));
		}
		mix += (tmix - mix) * .3f;
		final Vector3f dif = trackPos.subtract(screenPos);
		position.set(screenPos.add(dif.multLocal(mix)));

		setLocalTranslation(position.add(offset));
		super.updateGeometricState(time, initiator);
	}

	public void trackObject(final Spatial entity) {
		trackObject = entity;
		tmix = (entity == null) ? 0 : 1;
	}

}
