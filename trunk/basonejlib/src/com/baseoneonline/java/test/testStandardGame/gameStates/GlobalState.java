package com.baseoneonline.java.test.testStandardGame.gameStates;

import java.util.concurrent.Callable;

import com.jme.app.AbstractGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jmex.game.state.GameState;

public class GlobalState extends GameState {

	InputHandler input = new InputHandler();

	AbstractGame game;

	public GlobalState(final AbstractGame game) {

		this.game = game;
		GameTaskQueueManager.getManager().update(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				DisplaySystem.getDisplaySystem().getRenderer()
						.setBackgroundColor(ColorRGBA.gray);
				return null;
			}
		});

		final KeyBindingManager key = KeyBindingManager.getKeyBindingManager();
		key.add("exit", KeyInput.KEY_ESCAPE);
	}

	@Override
	public void cleanup() {}

	@Override
	public void render(final float tpf) {

	}

	@Override
	public void update(final float tpf) {
		final KeyBindingManager key = KeyBindingManager.getKeyBindingManager();
		if (key.isValidCommand("exit", false)) {
			game.finish();
		}
	}

}
