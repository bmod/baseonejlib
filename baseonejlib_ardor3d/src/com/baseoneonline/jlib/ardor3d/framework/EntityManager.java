package com.baseoneonline.jlib.ardor3d.framework;

import java.util.ArrayList;
import java.util.List;

public class EntityManager implements Manager {

	private final List<Entity> entities = new ArrayList<Entity>();

	public void add(Entity e) {
		entities.add(e);
		e.onAdded();
	}

	public void remove(Entity e) {
		entities.remove(e);
		e.onRemoved();
	}

	public void update(double t) {
		for (Entity e : entities)
			e.update(t);
	}

	public Entity create(String name) {
		Entity e = new Entity(name);
		add(e);
		return e;
	}

}
