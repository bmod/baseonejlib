package com.baseoneonline.java.test.testStandardGame.editor;

import java.util.concurrent.Callable;

import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.LineGrid;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.math.Plane;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jmex.game.state.GameState;

public class EditorGameState extends GameState {

	private static final String SCROLL_LEFT = "scrollLeft",
			SCROLL_RIGHT = "scrollRight", SCROLL_UP = "scrollUp",
			SCROLL_DOWN = "scrollDown", ZOOM_IN = "zoomIn",
			ZOOM_OUT = "zoomOut";

	private final Plane mousePlane = new Plane(Vector3f.UNIT_Z, 0);

	private Box box;

	DisplaySystem display;

	private LineGrid grid;

	private final float keyScrollSpeed = .01f;

	private final Node rootNode = new Node("EditorRootNode");

	private Camera cam;

	public EditorGameState() {

		GameTaskQueueManager.getManager().update(new Callable<Void>() {

			public Void call() throws Exception {
				init();
				return null;
			};
		});
	}

	private void init() {
		display = DisplaySystem.getDisplaySystem();

		box = new Box("Boxy", new Vector3f(), 1, 1, 1);
		final MaterialState ms = display.getRenderer().createMaterialState();
		box.setRenderState(ms);
		box.updateRenderState();
		rootNode.attachChild(box);

		grid = new LineGrid(10);
		rootNode.attachChild(grid);

		cam = display.getRenderer().getCamera();
		cam.setLocation(new Vector3f(0, 0, 30));

		initInput();
	}

	private void initInput() {
		final KeyBindingManager key = KeyBindingManager.getKeyBindingManager();

		key.add(SCROLL_LEFT, KeyInput.KEY_A);
		key.add(SCROLL_RIGHT, KeyInput.KEY_D);
		key.add(SCROLL_UP, KeyInput.KEY_W);
		key.add(SCROLL_DOWN, KeyInput.KEY_S);
		key.add(ZOOM_IN, KeyInput.KEY_E);
		key.add(ZOOM_OUT, KeyInput.KEY_Z);

		key.add("SCROLL", MouseButtonBinding.LEFT_BUTTON);

		MouseInput.get().addListener(new MouseInputListener() {

			@Override
			public void onWheel(final int wheelDelta, final int x, final int y) {
			// TODO Auto-generated method stub

			}

			@Override
			public void onMove(final int xDelta, final int yDelta,
					final int newX, final int newY) {
			// TODO Auto-generated method stub

			}

			@Override
			public void onButton(final int button, final boolean pressed,
					final int x, final int y) {
				System.out.println(button);
			}
		});
	}

	private void handleInput() {
		final KeyBindingManager key = KeyBindingManager.getKeyBindingManager();

		final Vector3f camPos = cam.getLocation();
		final float spd = keyScrollSpeed * camPos.z;
		if (key.isValidCommand(SCROLL_LEFT)) camPos.x -= spd;
		if (key.isValidCommand(SCROLL_RIGHT)) camPos.x += spd;
		if (key.isValidCommand(SCROLL_UP)) camPos.y += spd;
		if (key.isValidCommand(SCROLL_DOWN)) camPos.y -= spd;
		if (key.isValidCommand(ZOOM_IN)) camPos.z -= spd;
		if (key.isValidCommand(ZOOM_OUT)) camPos.z += spd;
		if (key.isValidCommand("SCROLL")) {
			System.out.println("SCROLL");
		}

		cam.setLocation(camPos);

	}

	@Override
	public void setActive(final boolean active) {

		MouseInput.get().setCursorVisible(active);
		super.setActive(active);
	}

	@Override
	public void update(final float tpf) {
		handleInput();

		final Vector3f v1 = JMEUtil.getPosition3D(new Vector2f(0, 0),
				mousePlane);
		final Vector3f v2 = JMEUtil.getPosition3D(new Vector2f(display
				.getWidth(), display.getHeight()), mousePlane);

		// System.out.println(cam.getLocation().z-1);

		// box.setLocalTranslation(v1);
		final float margin = .5f;

		grid.setBounds(v1.x + margin, v2.x - margin, v2.y - margin, v1.y
				+ margin);

		rootNode.updateGeometricState(tpf, true);
	}

	@Override
	public void render(final float tpf) {
		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);
	}

	@Override
	public void cleanup() {

	}

}
