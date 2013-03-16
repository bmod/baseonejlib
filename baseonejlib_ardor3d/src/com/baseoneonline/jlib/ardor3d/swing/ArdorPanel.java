package com.baseoneonline.jlib.ardor3d.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import org.lwjgl.LWJGLException;

import com.ardor3d.framework.DisplaySettings;
import com.ardor3d.framework.lwjgl.LwjglAwtCanvas;
import com.ardor3d.framework.lwjgl.LwjglCanvasRenderer;
import com.ardor3d.input.ControllerWrapper;
import com.ardor3d.input.PhysicalLayer;
import com.ardor3d.input.awt.AwtFocusWrapper;
import com.ardor3d.input.awt.AwtKeyboardWrapper;
import com.ardor3d.input.awt.AwtMouseManager;
import com.ardor3d.input.awt.AwtMouseWrapper;
import com.ardor3d.input.logical.DummyControllerWrapper;
import com.ardor3d.renderer.Camera;

public class ArdorPanel extends JPanel {

	private final Camera cam = new Camera();
	private LwjglAwtCanvas canvas;

	public ArdorPanel(UpdaterScene scene) {
		setLayout(new BorderLayout());

		final LwjglCanvasRenderer canvasRenderer = new LwjglCanvasRenderer(
				scene);

		final DisplaySettings settings = new DisplaySettings(400, 300, 24, 0,
				0, 16, 0, 0, false, false);
		try {
			canvas = new LwjglAwtCanvas(settings, canvasRenderer);

		} catch (LWJGLException e1) {
			throw new RuntimeException(e1);
		}

		add(canvas);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);

			}
		});
		canvas.setBackground(Color.black);
		// canvas.setSize(new Dimension(400, 300));
		// // theCanvas.setVisible(true);

		final AwtKeyboardWrapper keyboardWrapper = new AwtKeyboardWrapper(
				canvas);
		final AwtFocusWrapper focusWrapper = new AwtFocusWrapper(canvas);
		final AwtMouseManager mouseManager = new AwtMouseManager(canvas);
		final AwtMouseWrapper mouseWrapper = new AwtMouseWrapper(canvas,
				mouseManager);
		final ControllerWrapper controllerWrapper = new DummyControllerWrapper();

		final PhysicalLayer pl = new PhysicalLayer(keyboardWrapper,
				mouseWrapper, controllerWrapper, focusWrapper);

		ArdorManager.get().getLogicalLayer().registerInput(canvas, pl);

		canvas.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				resizeCanvas(canvas);
			}

		});

		ArdorManager.get().getFrameWork().addCanvas(canvas);

	}

	public void setScene(UpdaterScene scene) {

	}

	private static void resizeCanvas(LwjglAwtCanvas canvas) {
		int w = canvas.getWidth();
		int h = canvas.getHeight();
		double r = (double) w / (double) h;

		Camera cam = canvas.getCanvasRenderer().getCamera();
		if (null != cam) {
			cam.resize(w, h);

			cam.setFrustumPerspective(cam.getFovY(), r, cam.getFrustumNear(),
					cam.getFrustumFar());
		}
	}
}