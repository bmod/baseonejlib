package game.states;

import game.Level;
import game.managers.ResourceManager;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameState;

public class MainGameState extends GameState {

	private Node rootNode;

	public MainGameState() {
		initInput();
		initScene();
	}

	private void initScene() {
		rootNode = new Node();
		Box box = new Box("Box", new Vector3f(), 1, 1, 1);
		rootNode.attachChild(box);

		Level lvl = ResourceManager.get().loadLevel("level1");
		rootNode.attachChild(lvl.node);
	}

	@Override
	public void cleanup() {}

	@Override
	public void render(final float tpf) {
		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);
	}

	@Override
	public void update(final float tpf) {
		handleInput();
	};

	private void initInput() {
		final KeyBindingManager key = KeyBindingManager.getKeyBindingManager();
		key.add("EXIT", KeyInput.KEY_ESCAPE);
	}

	private void handleInput() {
		final KeyBindingManager key = KeyBindingManager.getKeyBindingManager();
		if (key.isValidCommand("EXIT", false)) {
			System.exit(0);
		}
	}

}
