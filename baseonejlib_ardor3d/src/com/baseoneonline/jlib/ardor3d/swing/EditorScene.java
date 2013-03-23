package com.baseoneonline.jlib.ardor3d.swing;

import java.util.Random;

import com.ardor3d.annotation.MainThread;
import com.ardor3d.image.Texture;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.intersection.PickResults;
import com.ardor3d.light.PointLight;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Ray3;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.ContextManager;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.renderer.state.LightState;
import com.ardor3d.renderer.state.TextureState;
import com.ardor3d.renderer.state.ZBufferState;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.ui.text.BasicText;
import com.ardor3d.util.ContextGarbageCollector;
import com.ardor3d.util.GameTaskQueue;
import com.ardor3d.util.GameTaskQueueManager;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.TextureManager;
import com.baseoneonline.jlib.ardor3d.controllers.EditorCameraController;
import com.baseoneonline.jlib.ardor3d.spatials.Grid;

public class EditorScene implements UpdaterScene {

	private final LogicalLayer logicalLayer = ArdorManager.get()
			.getLogicalLayer();

	private final static float CUBE_ROTATE_SPEED = 1;
	private final Vector3 rotationAxis = new Vector3(1, 1, 0);
	private double angle = 0;
	private Mesh box;
	private final Matrix3 rotation = new Matrix3();

	private static final int MOVE_SPEED = 4;
	private final int rotationSign = 1;
	private boolean inited;

	public EditorScene() {

	}

	@Override
	@MainThread
	public void init() {
		if (inited) {
			return;
		}
		// add a cube to the scene
		// add a rotating controller to the cube
		// add a light
		Grid grid = new Grid();
		root.attachChild(grid);

		box = new Box("The cube", new Vector3(-1, -1, -1), new Vector3(1, 1, 1));

		final ZBufferState buf = new ZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		root.setRenderState(buf);

		// Add a texture to the box.
		final TextureState ts = new TextureState();
		ts.setTexture(TextureManager.load("images/ardor3d_white_256.jpg",
				Texture.MinificationFilter.Trilinear, true));
		box.setRenderState(ts);

		final PointLight light = new PointLight();

		final Random random = new Random();

		final float r = random.nextFloat();
		final float g = random.nextFloat();
		final float b = random.nextFloat();
		final float a = random.nextFloat();

		light.setDiffuse(new ColorRGBA(r, g, b, a));
		light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
		light.setLocation(new Vector3(MOVE_SPEED, MOVE_SPEED, MOVE_SPEED));
		light.setEnabled(true);

		/** Attach the light to a lightState and the lightState to rootNode. */
		final LightState lightState = new LightState();
		lightState.setEnabled(true);
		lightState.attach(light);
		root.setRenderState(lightState);

		root.attachChild(box);

		registerInputTriggers();

		final BasicText text = BasicText.createDefaultTextLabel("test",
				"Hello World!");
		root.attachChild(text);

		inited = true;
	}

	private void registerInputTriggers() {
		new EditorCameraController(logicalLayer);
		// final FirstPersonControl control = FirstPersonControl.setupTriggers(
		// logicalLayer, Vector3.UNIT_Y, true);
		// control.setMoveSpeed(MOVE_SPEED);

	}

	@Override
	@MainThread
	public void update(final ReadOnlyTimer timer) {
		final double tpf = timer.getTimePerFrame();

		angle += tpf * CUBE_ROTATE_SPEED * rotationSign;

		rotation.fromAngleAxis(angle, rotationAxis);
		box.setRotation(rotation);

		root.updateGeometricState(tpf, true);
	}

	private Node root = new Node("root");;

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		if (root == null)
			throw new NullPointerException();
		this.root = root;

	}

	@Override
	@MainThread
	public boolean renderUnto(final Renderer renderer) {
		GameTaskQueueManager.getManager(ContextManager.getCurrentContext())
				.getQueue(GameTaskQueue.RENDER).execute(renderer);
		ContextGarbageCollector.doRuntimeCleanup(renderer);
		renderer.draw(root);
		return true;
	}

	@Override
	public PickResults doPick(final Ray3 pickRay) {
		return null;
	}
}