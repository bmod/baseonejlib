package com.baseoneonline.jlib.ardor3d.framework;

import java.util.ArrayList;
import java.util.List;

public class TheGame {

	private static TheGame instance;

	private final List<Entity> entities = new ArrayList<Entity>();

	private TheGame() {
	}

	public void update(double t) {

	}

	public void render(double t) {

	}

	public void add(Entity e) {
		entities.add(e);
	}

	public static TheGame get() {
		if (null == instance)
			instance = new TheGame();
		return instance;
	}

}
