package com.baseoneonline.jlib.ardor3d.framework;

import java.util.ArrayList;
import java.util.List;

import com.baseoneonline.jlib.ardor3d.framework.entities.Entity;

public class EntityManager {

	private static EntityManager instance;

	private final List<Entity> entities = new ArrayList<Entity>();

	public static EntityManager get() {
		if (null == instance)
			instance = new EntityManager();
		return instance;
	}

	private void add(Entity e) {
		entities.add(e);
		SceneManager.get().add(e.getNode());
	}

	private void remove(Entity e) {
		entities.remove(e);
		SceneManager.get().remove(e.getNode());
	}

	public void update(double t) {
		for (Entity e : entities)
			e.update(t);
	}

	public Entity load(String resource) {
		Entity e = ResourceManager.get().getEntity(resource);
		add(e);
		return e;
	}

	public Entity create(String name) {
		Entity e = new Entity(name);
		add(e);
		return e;
	}

}
