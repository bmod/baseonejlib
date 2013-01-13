package com.baseoneonline.jlib.ardor3d.framework.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.ardor3d.math.type.ReadOnlyTransform;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;
import com.ardor3d.util.export.Savable;

public class Entity implements Savable {

	private final HashMap<Class<? extends Component>, Component> components = new HashMap<Class<? extends Component>, Component>();

	private final Node node;

	private boolean initialized = false;

	public Entity() {
		node = new Node();
	}

	public Entity(final String name) {
		node = new Node(name);
	}

	public Node getNode() {
		return node;
	}

	public String getName() {
		return node.getName();
	}

	public void setName(final String name) {
		node.setName(name);
	}

	public void addComponent(final Component c) {
		final Class<? extends Component> type = c.getClass();
		if (hasComponent(type))
			throw new RuntimeException("Component type "
					+ c.getClass().getName() + "  already added to: " + this);
		components.put(type, c);
		c.setOwner(this);
	}

	private boolean hasComponent(final Class<? extends Component> class1) {
		return components.containsKey(class1);
	}

	public void removeComponent(final Component c) {
		components.remove(c.getClass());
		c.suspend();
		c.setOwner(null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(final Class<T> type) {
		final T comp = (T) components.get(type);
		if (comp == null)
			throw new RuntimeException("Component of type " + type.getName()
					+ " not found on entity: " + getName());
		return comp;
	}

	public Collection<? extends Component> getComponents() {
		return components.values();
	}

	public void update(final double t) {
		if (!initialized) {
			for (final Component c : getComponents())
				c.resume();
			initialized = true;
		}

		for (final Component c : getComponents())
			c.update(t);
	}

	public void setTransform(final ReadOnlyTransform xf) {
		if (hasComponent(PhysicsComponent.class)) {
			getComponent(PhysicsComponent.class).setTransform(xf);
		} else {
			node.setTransform(xf);
		}
	}

	@Override
	public void write(final OutputCapsule capsule) throws IOException {
		capsule.write(getName(), "name", null);
		capsule.writeSavableList(new ArrayList<Savable>(components.values()),
				"components", null);
	}

	@Override
	public void read(final InputCapsule capsule) throws IOException {
		setName(capsule.readString("name", null));
		components.clear();
		final List<Component> components = capsule.readSavableList(
				"components", Collections.<Component> emptyList());

		for (final Component c : components)
			addComponent(c);

	}

	@Override
	public Class<?> getClassTag() {
		return getClass();
	}

	@Override
	public String toString() {
		return String.format("Entity (name: %s)", getName());
	}
}
