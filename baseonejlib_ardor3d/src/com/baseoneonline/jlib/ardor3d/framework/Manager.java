package com.baseoneonline.jlib.ardor3d.framework;

import com.baseoneonline.jlib.ardor3d.IGame;


public abstract class Manager {

	protected final IGame game;

	public Manager(IGame game) {
		this.game = game;
	}
}
