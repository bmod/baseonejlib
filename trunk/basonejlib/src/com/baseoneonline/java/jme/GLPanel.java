package com.baseoneonline.java.jme;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.concurrent.Callable;

import javax.swing.JPanel;

import com.jme.input.InputSystem;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.system.canvas.JMECanvas;
import com.jme.system.canvas.SimpleCanvasImpl;
import com.jme.system.lwjgl.LWJGLSystemProvider;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jmex.awt.input.AWTKeyInput;
import com.jmex.awt.input.AWTMouseInput;
import com.jmex.awt.lwjgl.LWJGLAWTCanvasConstructor;

public abstract class GLPanel extends JPanel {

	private Canvas glCanvas;
	protected DisplaySystem display;
	private final int cwidth, cheight;
	private Node rootNode;
	private MyImpl impl;

	public GLPanel() {

		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(150, 150));
		// Set initial dimensions
		cwidth = 640;
		cheight = 480;

		this.setSize(cwidth, cheight);
		// Add canvas to the canvasPanel
		add(getGLCanvas(), BorderLayout.CENTER);
		// Frame stuff

		((JMECanvas) getGLCanvas()).setTargetRate(61);
	}

	protected Canvas getGLCanvas() {
		if (glCanvas == null) {
			// -------------GL STUFF------------------

			// make the canvas:
			display = DisplaySystem
					.getDisplaySystem(LWJGLSystemProvider.LWJGL_SYSTEM_IDENTIFIER);
			display.registerCanvasConstructor("AWT",
					LWJGLAWTCanvasConstructor.class);
			glCanvas = (Canvas) display.createCanvas(cwidth, cheight);
			glCanvas.setMinimumSize(new Dimension(100, 100));

			// add a listener... if window is resized, we can do something about
			// it.
			glCanvas.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(final ComponentEvent ce) {
					doResize();
				}
			});

			// Add in a focus listener so the canvas can respond to mouse
			// and key inputs
			glCanvas.addFocusListener(new FocusListener() {

				public void focusGained(final FocusEvent arg0) {
					((AWTKeyInput) KeyInput.get()).setEnabled(true);
					// ((AWTMouseInput) MouseInput.get()).setEnabled(true);
				}

				public void focusLost(final FocusEvent arg0) {
					((AWTKeyInput) KeyInput.get()).setEnabled(false);
					// ((AWTMouseInput) MouseInput.get()).setEnabled(false);
				}

			});

			KeyInput.setProvider(InputSystem.INPUT_SYSTEM_AWT);
			((AWTKeyInput) KeyInput.get()).setEnabled(false);
			final KeyListener kl = (KeyListener) KeyInput.get();
			glCanvas.addKeyListener(kl);
			AWTMouseInput.setup(glCanvas, true);
			((AWTMouseInput) MouseInput.get()).setEnabled(true);

			impl = new MyImpl(getWidth(), getHeight());
			
			((JMECanvas) glCanvas).setImplementor(impl);

			// -----------END OF GL STUFF-------------
			final Callable<Void> exe = new Callable<Void>() {
				public Void call() {
					forceUpdateToSize();
					((JMECanvas) glCanvas).setTargetRate(60);
					return null;
				}
			};
			GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER)
					.enqueue(exe);
		}
		return glCanvas;
	}

	protected void doResize() {
		if (impl != null) {
			impl.resizeCanvas(glCanvas.getWidth(), glCanvas.getHeight());
			if (impl.getCamera() != null) {
				final Callable<Void> exe = new Callable<Void>() {
					public Void call() {
						impl.getCamera().setFrustumPerspective(
								45.0f,
								(float) glCanvas.getWidth()
										/ (float) glCanvas.getHeight(), 1,
								10000);
						return null;
					}
				};
				GameTaskQueueManager.getManager()
						.getQueue(GameTaskQueue.RENDER).enqueue(exe);
			}
		}
	}

	public void forceUpdateToSize() {
		// force a resize to ensure proper canvas size.
		glCanvas.setSize(glCanvas.getWidth(), glCanvas.getHeight() + 1);
		glCanvas.setSize(glCanvas.getWidth(), glCanvas.getHeight() - 1);
	}
	
	public Node getRootNode() {
		return impl.getRootNode();
	}
	
	protected abstract void init();
	
	protected abstract void update(float t);

	private class MyImpl extends  SimpleCanvasImpl {
		
		
		public MyImpl(int w, int h) {
			super(w,h);
		}
		@Override
		public void simpleSetup() {
			init();
		}

		@Override
		public void resizeCanvas(int newWidth, int newHeight) {
			if (newWidth < 100)
				newWidth = 100;
			if (newHeight < 100)
				newHeight = 100;
			super.resizeCanvas(newWidth, newHeight);
		}

		@Override
		public void simpleUpdate() {
			update(tpf);
		}

		@Override
		public void simpleRender() {
		}

	};
}
