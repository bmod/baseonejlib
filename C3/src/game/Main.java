package game;

import game.states.MainGameState;

import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;

public class Main {

	public static void main(final String[] args) {
		new Main();
	}

	public Main() {
		final StandardGame game = new StandardGame("C3");
		game.start();

		final GameStateManager gsm = GameStateManager.getInstance();

		final MainGameState mainState = new MainGameState();
		mainState.setActive(true);
		gsm.attachChild(mainState);
	}
}
