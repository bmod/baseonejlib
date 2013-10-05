package com.baseoneonline.jlib.ardor3d.framework.entities;

import java.util.ArrayList;
import java.util.List;

import com.baseoneonline.jlib.ardor3d.framework.ResourceManager;
import com.baseoneonline.jlib.ardor3d.framework.SceneManager;

public class EntityManager {

	private static EntityManager instance;

	private final List<Entity> entities = new ArrayList<Entity>();

	public static EntityManager get() {
		if (null == instance)
			instance = new EntityManager();
		return instance;
	}

	private void add(Entity e) {
		assert isUniqueName(e.getName()) : "Entity must have a unique name: "
				+ e.getName();
		entities.add(e);
		SceneManager.get().add(e.getNode());
	}

	private boolean isUniqueName(String name) {
		for (Entity e : entities)
			if (e.getName().equals(name))
				return false;
		return true;
	}

	public String getUniqueName(String baseName) {
		String name = baseName;
		int i = 1;
		while (!isUniqueName(name)) {
			name = baseName + i;
			i++;
		}
		return name;
	}

	public void remove(Entity e) {
		entities.remove(e);
		SceneManager.get().remove(e.getNode());
	}

	public void update(double t) {
		for (Entity e : entities)
			e.update(t);
	}

	public Entity load(String resource) {
		Entity e = ResourceManager.get().getEntity(resource);
		e.setName(EntityManager.get().getUniqueName(e.getName()));
		add(e);
		return e;
	}

	public Entity create(String name) {
		Entity e = new Entity(name);
		add(e);
		return e;
	}

	public Entity getEntity(String name) {
		for (Entity e : entities) {
			if (e.getName().equals(name))
				return e;
		}
		assert false : "Entity does not exist: " + name;
		return null;
	}

}
