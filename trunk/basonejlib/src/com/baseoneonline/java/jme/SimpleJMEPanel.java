package com.baseoneonline.java.jme;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

import javax.swing.JPanel;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.system.canvas.JMECanvas;
import com.jme.system.canvas.SimpleCanvasImpl;
import com.jme.system.lwjgl.LWJGLSystemProvider;
import com.jmex.awt.input.AWTMouseInput;
import com.jmex.awt.lwjgl.LWJGLAWTCanvasConstructor;

public abstract class SimpleJMEPanel extends JPanel {

	Canvas canvas = null;
	private SimpleCanvasImpl impl;
	private boolean queueForResize = true;

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
				resizeComponent();
			}

			@Override
			public void componentMoved(final ComponentEvent e) {
				resizeComponent();
			}

			@Override
			public void componentShown(final ComponentEvent e) {
				resizeComponent();
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

	private void resizeComponent() {
		impl.resizeCanvas(getWidth(), getHeight());
		canvas.setSize(getWidth(), getHeight());
		canvas.setLocation(0, 0);
		((JMECanvas) canvas).makeDirty();

		final Renderer renderer = DisplaySystem.getDisplaySystem()
				.getRenderer();
		if (null != renderer) {
			final Camera cam = renderer.getCamera();
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
		if (h < 100) {
			h = 100;
		}
		return h;
	}

	public Node getRootNode() {
		return impl.getRootNode();
	}

	protected abstract void initJME();

	protected abstract void updateJME(float t);

	class MyImplementor extends SimpleCanvasImpl {

		long startTime = 0;
		long fps = 0;
		private InputHandler input;

		private final Logger logger = Logger.getLogger(getClass().getName());

		public MyImplementor(final int width, final int height) {
			super(width, height);
		}

		@Override
		public Node getRootNode() {
			return super.getRootNode();
		}

		@Override
		public void simpleSetup() {

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
			startTime = System.currentTimeMillis() + 5000;
			initJME();
		}

		@Override
		public void simpleUpdate() {
			if (queueForResize) {
				resizeComponent();
				queueForResize = false;
			}

			input.update(tpf);

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

			updateJME(tpf);
		}
	}
}
