/*
 * Copyright (c) 2003-2009 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edit;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.system.DisplaySystem;
import com.jme.system.canvas.JMECanvas;
import com.jme.system.canvas.JMECanvasImplementor;
import com.jme.system.canvas.SimpleCanvasImpl;
import com.jme.system.lwjgl.LWJGLSystemProvider;
import com.jmex.awt.input.AWTMouseInput;
import com.jmex.awt.lwjgl.LWJGLAWTCanvasConstructor;
import com.jmex.awt.lwjgl.LWJGLCanvas;

/**
 * <code>JMESwingTest</code> is a test demoing the JMEComponent and
 * HeadlessDelegate integration classes allowing jME generated graphics to be
 * displayed in a AWT/Swing interface. Note the Repaint thread and how you grab
 * a canvas and add an implementor to it.
 * 
 * @author Joshua Slack
 * @version $Id: JMESwingTest.java 4130 2009-03-19 20:04:51Z blaine.dev $
 */

public abstract class JMESwingPanel extends JPanel {

	int width = 640, height = 480;

	private Node rootNode;

	public JMESwingPanel() {

		init();

	}

	// **************** SWING FRAME ****************

	private static final long serialVersionUID = 1L;

	LWJGLCanvas canvas = null;
	JMECanvasImplementor impl;

	// Component initialization
	private void init() {
		setLayout(new BorderLayout());
		// -------------GL STUFF------------------

		// make the canvas:
		final DisplaySystem display = DisplaySystem
				.getDisplaySystem(LWJGLSystemProvider.LWJGL_SYSTEM_IDENTIFIER);

		display.registerCanvasConstructor("AWT",
				LWJGLAWTCanvasConstructor.class);
		canvas = (LWJGLCanvas) display.createCanvas(width, height);
		canvas.setUpdateInput(true);
		canvas.setTargetRate(60);

		// add a listener... if window is resized, we can do something about
		// it.
		canvas.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(final ComponentEvent ce) {
				doResize();
			}
		});

		// Setup key and mouse input
		KeyInput.setProvider(KeyInput.INPUT_AWT);
		final KeyListener kl = (KeyListener) KeyInput.get();
		canvas.addKeyListener(kl);
		AWTMouseInput.setup(canvas, false);

		// Important! Here is where we add the guts to the panel:
		impl = new MyImplementor(width, height);
		canvas.setImplementor(impl);

		// -----------END OF GL STUFF-------------
		canvas.setBounds(0, 0, width, height);
		add(canvas, BorderLayout.CENTER);
	}

	protected void doResize() {
		impl.resizeCanvas(canvas.getWidth(), canvas.getHeight());
		if (null != DisplaySystem.getDisplaySystem()
				&& null != DisplaySystem.getDisplaySystem().getRenderer()) {
			final Camera cam = DisplaySystem.getDisplaySystem().getRenderer()
					.getCamera();
			cam.setFrustumPerspective(90, (float) canvas.getWidth()
					/ (float) canvas.getHeight(), 1, 100);
		}
		((JMECanvas) canvas).makeDirty();
	}

	// IMPLEMENTING THE SCENE:

	class MyImplementor extends SimpleCanvasImpl {

		private Quaternion rotQuat;
		private final float angle = 0;
		private Vector3f axis;
		private Box box;
		long startTime = 0;
		long fps = 0;
		private InputHandler input;

		public MyImplementor(final int width, final int height) {
			super(width, height);
		}

		@Override
		public void simpleSetup() {
			doResize();
			DisplaySystem.getDisplaySystem().getRenderer().setBackgroundColor(
					ColorRGBA.darkGray);
			JMESwingPanel.this.rootNode = rootNode;
			initJME();
		}

		@Override
		public void simpleUpdate() {
			updateJME(tpf);
		}
	}

	public Node getRootNode() {
		return rootNode;
	}

	public abstract void initJME();

	public abstract void updateJME(float t);

}
