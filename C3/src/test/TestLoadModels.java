package test;

import game.Level;
import game.managers.ResourceManager;

import java.util.logging.Logger;

import com.jme.app.SimpleGame;

public class TestLoadModels extends SimpleGame {

	public static void main(final String[] args) {
		Logger.getLogger("com.jme").setLevel(java.util.logging.Level.WARNING);
		new TestLoadModels().start();
	}

	@Override
	protected void simpleInitGame() {
		final Level level = ResourceManager.get().loadLevel("level1");
		rootNode.attachChild(level.node);
	}

}
