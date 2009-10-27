package test;

import game.ResourceManager;

public class TestLoadLevels {

	public static void main(final String[] args) {
		for (final String h : ResourceManager.get().getLevelHandles()) {
			ResourceManager.get().getLevel(h);
		}
	}
}
