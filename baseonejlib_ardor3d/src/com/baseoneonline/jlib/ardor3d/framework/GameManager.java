package com.baseoneonline.jlib.ardor3d.framework;

import com.ardor3d.renderer.Renderer;
import com.baseoneonline.jlib.ardor3d.IGame;

public class GameManager {

	private static GameManager instance;

	private EntityManager entityManager;
	private SceneManager sceneManager;
	private PhysicsManager physicsManager;
	private ResourceManager resourceManager;
	private InputManager inputManager;

	private GameManager() {

	}

	public void init(IGame game, InputMap inputMap) {
		entityManager = new EntityManager();
		sceneManager = new SceneManager();
		physicsManager = new PhysicsManager();
		resourceManager = new ResourceManager(game.getClass());
		inputManager = new InputManager(game.getLogicalLayer(), inputMap);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public SceneManager getSceneManager() {
		return sceneManager;
	}

	public PhysicsManager getPhysicsManager() {
		return physicsManager;
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public void update(double t) {
		inputManager.update(t);
		entityManager.update(t);
		sceneManager.update(t);
	}

	public static GameManager get() {
		if (instance == null)
			instance = new GameManager();
		return instance;
	}

	public void render(Renderer r) {
		sceneManager.render(r);
	}
}
