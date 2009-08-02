package com.baseoneonline.java.test.testStandardGame;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.baseoneonline.java.test.testStandardGame.editor.EditorGameState;
import com.baseoneonline.java.test.testStandardGame.global.GlobalCommands;
import com.baseoneonline.java.test.testStandardGame.global.GlobalGameState;
import com.baseoneonline.java.test.testStandardGame.global.GlobalController.Command;
import com.baseoneonline.java.test.testStandardGame.global.GlobalController.Listener;
import com.baseoneonline.java.test.testStandardGame.menu.MenuInput;
import com.baseoneonline.java.test.testStandardGame.menu.MenuInput.Trigger;
import com.jme.input.KeyInput;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;

public class TestStandardGame {

	public static void main(final String[] args) {
		new TestStandardGame();
	}

	StandardGame game;

	public TestStandardGame() {
		Logger.getLogger("com.jme").setLevel(Level.WARNING);
		// System.setProperty("jme.stats", "set");

		game = new StandardGame("My Game");
		game.start();

		mapKeys();

		final GlobalGameState gstate = new GlobalGameState(game);
		gstate.setActive(true);
		GameStateManager.getInstance().attachChild(gstate);

		final MainGameState gameState = new MainGameState();
		gameState.setActive(true);
		GameStateManager.getInstance().attachChild(gameState);

		final EditorGameState editorState = new EditorGameState();
		editorState.setActive(true);
		GameStateManager.getInstance().attachChild(editorState);

		final StatusState statusState = new StatusState();
		statusState.setActive(true);
		GameStateManager.getInstance().attachChild(statusState);

		// final MenuGameState menuState = new MenuGameState();
		// menuState.setActive(true);
		// GameStateManager.getInstance().attachChild(menuState);

	}

	private final Listener globalListener = new Listener() {

		@Override
		public void command(final Command c) {
			if (GlobalCommands.EXIT_APP == c) {
				game.finish();
			}
		}
	};

	private void mapKeys() {

		final MenuInput keyMap = MenuInput.get();
		keyMap.mapKey(KeyInput.KEY_UP, Trigger.Up);
		keyMap.mapKey(KeyInput.KEY_DOWN, Trigger.Down);
		keyMap.mapKey(KeyInput.KEY_LEFT, Trigger.Left);
		keyMap.mapKey(KeyInput.KEY_RIGHT, Trigger.Right);
		keyMap.mapKey(KeyInput.KEY_RETURN, Trigger.Accept);
		keyMap.mapKey(KeyInput.KEY_BACK, Trigger.Back);

	}
}
