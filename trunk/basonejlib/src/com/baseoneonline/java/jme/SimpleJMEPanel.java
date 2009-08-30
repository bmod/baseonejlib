package com.baseoneonline.java.jme;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

import javax.swing.JPanel;

import jmetest.util.JMESwingTest;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.system.canvas.JMECanvas;
import com.jme.system.canvas.SimpleCanvasImpl;
import com.jme.system.lwjgl.LWJGLSystemProvider;
import com.jme.util.TextureManager;
import com.jmex.awt.input.AWTMouseInput;
import com.jmex.awt.lwjgl.LWJGLAWTCanvasConstructor;

public abstract class SimpleJMEPanel extends JPanel {

	Canvas canvas = null;
	private SimpleCanvasImpl impl;

	public SimpleJMEPanel() {
		init();

	}

	private void init() {

		// make the canvas:
		final DisplaySystem display = DisplaySystem
				.getDisplaySystem(LWJGLSystemProvider.LWJGL_SYSTEM_IDENTIFIER);
		display.registerCanvasConstructor("AWT",
				LWJGLAWTCanvasConstructor.class);
		canvas = (Canvas) display.createCanvas(getWidth(), getHeight());
		((JMECanvas) canvas).setUpdateInput(true);
		((JMECanvas) canvas).setTargetRate(60);

		// add a listener... if window is resized, we can do something about
		// it.

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(final ComponentEvent ce) {
				myComponentResized();
			}
		});

		// Setup key and mouse input
		KeyInput.setProvider(KeyInput.INPUT_AWT);
		final KeyListener kl = (KeyListener) KeyInput.get();
		canvas.addKeyListener(kl);
		AWTMouseInput.setup(canvas, false);

		// Important! Here is where we add the guts to the panel:
		impl = new MyImplementor(getWidth(), getHeight());
		((JMECanvas) canvas).setImplementor(impl);

		// -----------END OF GL STUFF-------------
		add(canvas, BorderLayout.CENTER);
	}

	private void myComponentResized() {
		impl.resizeCanvas(getWidth(), getHeight());
		canvas.setSize(getWidth(), getHeight());
		((JMECanvas) canvas).makeDirty();

		final Camera cam = impl.getCamera();
		if (null != cam) {
			final float aspect = (float) getWidth() / (float) getHeight();

			cam.setFrustumPerspective(60, aspect, 1, 1000);
		}
	}

	public void setImplementor(final SimpleCanvasImpl impl) {
		this.impl = impl;
		((JMECanvas) canvas).setImplementor(impl);
	}

	@Override
	public int getWidth() {
		int w = super.getWidth();
		if (w < 100) w = 100;
		return w;
	}

	@Override
	public int getHeight() {
		int h = super.getHeight();
		if (h < 100) h = 100;
		return h;
	}

	protected abstract void initJME();

}

class MyImplementor extends SimpleCanvasImpl {

	private Quaternion rotQuat;
	private float angle = 0;
	private Vector3f axis;
	private Box box;
	long startTime = 0;
	long fps = 0;
	private InputHandler input;

	private final Logger logger = Logger.getLogger(getClass().getName());

	public MyImplementor(final int width, final int height) {
		super(width, height);
	}

	@Override
	public void simpleSetup() {

		// Normal Scene setup stuff...
		rotQuat = new Quaternion();
		axis = new Vector3f(1, 1, 0.5f);
		axis.normalizeLocal();

		final Vector3f max = new Vector3f(5, 5, 5);
		final Vector3f min = new Vector3f(-5, -5, -5);

		box = new Box("Box", min, max);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		box.setLocalTranslation(new Vector3f(0, 0, -10));
		box.setRenderQueueMode(Renderer.QUEUE_SKIP);
		rootNode.attachChild(box);

		box.setRandomColors();

		final TextureState ts = renderer.createTextureState();
		ts.setEnabled(true);
		ts.setTexture(TextureManager.loadTexture(
				JMESwingTest.class.getClassLoader().getResource(
						"jmetest/data/images/Monkey.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));

		rootNode.setRenderState(ts);
		startTime = System.currentTimeMillis() + 5000;

		input = new InputHandler();
		input.addAction(new InputAction() {

			public void performAction(final InputActionEvent evt) {
				logger.info(evt.getTriggerName());
			}
		}, InputHandler.DEVICE_MOUSE, InputHandler.BUTTON_ALL,
				InputHandler.AXIS_NONE, false);

		input.addAction(new InputAction() {

			public void performAction(final InputActionEvent evt) {
				logger.info(evt.getTriggerName());
			}
		}, InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL,
				InputHandler.AXIS_NONE, false);
	}

	@Override
	public void simpleUpdate() {
		input.update(tpf);

		// Code for rotating the box... no surprises here.
		if (tpf < 1) {
			angle = angle + (tpf * 25);
			if (angle > 360) {
				angle = 0;
			}
		}
		rotQuat.fromAngleNormalAxis(angle * FastMath.DEG_TO_RAD, axis);
		box.setLocalRotation(rotQuat);

		if (startTime > System.currentTimeMillis()) {
			fps++;
		} else {
			final long timeUsed = 5000 + (startTime - System
					.currentTimeMillis());
			startTime = System.currentTimeMillis() + 5000;
			logger.info(fps + " frames in " + (timeUsed / 1000f)
					+ " seconds = " + (fps / (timeUsed / 1000f))
					+ " FPS (average)");
			fps = 0;
		}
	}
}