package com.baseoneonline.java.test.testStandardGame.editor;

import java.util.concurrent.Callable;

import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.LineGrid;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Plane;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jmex.game.state.GameState;

public class EditorGameState extends GameState {

	private Plane mousePlane = new Plane(Vector3f.UNIT_Z, 0);

	private Box box;

	DisplaySystem display;

	private LineGrid grid;
	
	private float dummy;

	private float keyScrollSpeed = .01f;

	private Node rootNode = new Node("EditorRootNode");

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
		MaterialState ms = display.getRenderer().createMaterialState();
		box.setRenderState(ms);
		box.updateRenderState();
		rootNode.attachChild(box);

		grid = new LineGrid(10);
		grid.getLine().setColorBuffer(null);
		grid.getLine().setDefaultColor(new ColorRGBA(0, 0, 0, .3f));
		grid.getLocalRotation().fromAngles(FastMath.HALF_PI, 0, 0);
		rootNode.attachChild(grid);

		cam = display.getRenderer().getCamera();
		cam.setLocation(new Vector3f(0, 0, 30));

		initKeys();
	}

	private void initKeys() {
		KeyBindingManager key = KeyBindingManager.getKeyBindingManager();
		key.add("scrollLeft", KeyInput.KEY_A);
		key.add("scrollRight", KeyInput.KEY_D);
		key.add("scrollUp", KeyInput.KEY_W);
		key.add("scrollDown", KeyInput.KEY_S);
	}

	private void handleKeys() {
		KeyBindingManager key = KeyBindingManager.getKeyBindingManager();

		Vector3f camPos = cam.getLocation();
		float spd = keyScrollSpeed * camPos.z;
		if (key.isValidCommand("scrollLeft"))
			camPos.x -= spd;
		if (key.isValidCommand("scrollRight"))
			camPos.x += spd;
		if (key.isValidCommand("scrollUp"))
			camPos.y += spd;
		if (key.isValidCommand("scrollDown"))
			camPos.y -= spd;

		cam.setLocation(camPos);

	}

	@Override
	public void setActive(boolean active) {

		MouseInput.get().setCursorVisible(active);
		super.setActive(active);
	}

	@Override
	public void update(float tpf) {
		handleKeys();

		Vector3f mpos = JMEUtil.getMousePosition3D(mousePlane);

		Vector3f v1 = JMEUtil.getPosition3D(
				new Vector2f(0, display.getHeight()), mousePlane);
		Vector3f v2 = JMEUtil.getPosition3D(
				new Vector2f(display.getWidth(), 0), mousePlane);

		// System.out.println(cam.getLocation().z-1);

		box.setLocalTranslation(v1);
		float margin = .5f;

		grid.setBounds(v1.x + margin, v2.x - margin, v2.y + margin, v1.y
				- margin);

		rootNode.updateGeometricState(tpf, true);
	}

	@Override
	public void render(float tpf) {
		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);
	}

	@Override
	public void cleanup() {

	}

}
