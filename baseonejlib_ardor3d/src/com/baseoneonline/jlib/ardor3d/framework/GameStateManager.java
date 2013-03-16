package com.baseoneonline.jlib.ardor3d.framework;

public class GameStateManager {

	private GameState currentState;

	public GameStateManager() {}

	public void setCurrentState(GameState currentState)
	{
		this.currentState = currentState;
	}

	public GameState getCurrentState()
	{
		return currentState;
	}

}