package game.states;

import game.managers.ResourceManager;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jmex.game.state.GameState;

public class MainGameState extends GameState {

	public MainGameState() {
		initInput();
		ResourceManager.get().getLevel("level1/geometry.obj");
	}

	@Override
	public void cleanup() {}

	@Override
	public void render(final float tpf) {}

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
