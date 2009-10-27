import game.DebugState;
import game.MainGameState;
import game.ResourceManager;

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

	public static void main(final String[] args) {

		new C3();

	}

	final StandardGame game;

	public C3() {

		game = new StandardGame("C3");
		DisplaySystem.getDisplaySystem().setMinDepthBits(8);
		game.setBackgroundColor(ColorRGBA.blue);
		game.start();

		initMouse();

		final GameStateManager gsm = GameStateManager.getInstance();

		final GlobalState globalState = new GlobalState();
		globalState.setActive(true);
		gsm.attachChild(globalState);

		final MainGameState mainState = new MainGameState();
		mainState.setActive(true);
		gsm.attachChild(mainState);

		final DebugState debugState = new DebugState();
		debugState.setActive(true);
		gsm.attachChild(debugState);

	}

	private void initMouse() {
		final URL cursor = ResourceManager.get().getAsset(
				"images/handCursor.png");
		System.out.println(cursor);
		Mouse.setGrabbed(false);
		MouseInput.get().setHardwareCursor(cursor, 3, 28);
		// MouseInput.get().setCursorVisible(true);

	}

	class GlobalState extends GameState {

		public GlobalState() {
			final KeyBindingManager key = KeyBindingManager
					.getKeyBindingManager();
			key.add("EXIT", KeyInput.KEY_ESCAPE);
		}

		@Override
		public void cleanup() {}

		@Override
		public void render(final float tpf) {}

		@Override
		public void update(final float tpf) {
			final KeyBindingManager key = KeyBindingManager
					.getKeyBindingManager();
			if (key.isValidCommand("EXIT", false)) {
				game.shutdown();
			}
		}
	}
}
