package com.baseoneonline.java.test.testStandardGame;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.baseoneonline.java.test.testStandardGame.gameStates.GlobalState;
import com.baseoneonline.java.test.testStandardGame.gameStates.MenuState;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;

public class TestStandardGame {

	public static void main(final String[] args) {

		Logger.getLogger("com.jme").setLevel(Level.WARNING);
		// System.setProperty("jme.stats", "set");

		final StandardGame game = new StandardGame("My Game");
		game.start();

		final GlobalState gstate = new GlobalState(game);
		gstate.setActive(true);
		GameStateManager.getInstance().attachChild(gstate);

		final MenuState menuState = new MenuState();
		menuState.setActive(true);
		GameStateManager.getInstance().attachChild(menuState);

	}
}
