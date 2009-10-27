package game;

import java.net.URL;

import org.lwjgl.input.Mouse;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;

public class C3 {

	private static final String EXIT = "exit", EDIT = "edit";

	public static void main(final String[] args) {

		new C3();

	}

	final StandardGame game;

	public C3() {
		final boolean edit = false;

		game = new StandardGame("C3");
		DisplaySystem.getDisplaySystem().setMinDepthBits(8);
		game.setBackgroundColor(ColorRGBA.blue);
		game.start();

		initMouse();

		final GameStateManager gsm = GameStateManager.getInstance();

		final MainGameState mainState = new MainGameState();
		mainState.setActive(!edit);
		gsm.attachChild(mainState);

		final DebugState debugState = new DebugState();
		debugState.setActive(true);
		gsm.attachChild(debugState);

		final KeyBindingManager key = KeyBindingManager.getKeyBindingManager();
		key.add(EXIT, KeyInput.KEY_ESCAPE);
		key.add(EDIT, KeyInput.KEY_APOSTROPHE);

		final GameState globalState = new GameState() {

			@Override
			public void cleanup() {}

			@Override
			public void render(final float tpf) {}

			@Override
			public void update(final float tpf) {
				if (key.isValidCommand(EXIT, false)) {
					game.shutdown();
				}

			}

		};
		globalState.setActive(true);
		gsm.attachChild(globalState);
	}

	private void initMouse() {
		final URL cursor = ResourceManager.get().getAsset(
				"images/handCursor.png");
		Mouse.setGrabbed(false);
		MouseInput.get().setHardwareCursor(cursor, 3, 28);
	}
}
