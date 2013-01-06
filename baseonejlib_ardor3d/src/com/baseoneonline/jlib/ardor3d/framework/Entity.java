package com.baseoneonline.jlib.ardor3d.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;
import com.ardor3d.util.export.Savable;

public class Entity implements Savable {

	private String name;
	// private final List<Component> components = new ArrayList<Component>();
	private final HashMap<Class<? extends Component>, Component> components = new HashMap<Class<? extends Component>, Component>();

	public Entity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addComponent(Component c) {
		c.setOwner(this);
		Class<? extends Component> type = c.getClass();
		if (components.containsKey(type))
			throw new RuntimeException(String.format(
					"Component of type %s was already added.", type.getName()));
		components.put(type, c);
	}

	@SuppressWarnings("unchecked")
	public <T> T getComponent(Class<T> type) {
		if (!components.containsKey(type))
			throw new RuntimeException(
					"Component of this type was not on entity: "
							+ type.getName());
		return (T) components.get(type);
	}

	public void removeComponent(Component c) {
		c.setOwner(null);
		components.remove(c);
	}

	@Override
	public void write(OutputCapsule capsule) throws IOException {
		capsule.write(name, "name", null);

		capsule.writeSavableList(new ArrayList<Component>(components.values()),
				"components", null);
	}

	@Override
	public void read(InputCapsule capsule) throws IOException {
		name = capsule.readString("name", null);
		clearComponents();
		for (Component c : capsule.readSavableList("components",
				Collections.<Component> emptyList())) {
			addComponent(c);
		}
	}

	private void clearComponents() {
		for (Component c : getComponents())
			removeComponent(c);
	}

	private Collection<Component> getComponents() {
		return components.values();
	}

	@Override
	public Class<?> getClassTag() {
		return getClass();
	}

	public void update(double t) {
		for (Component c : getComponents())
			c.update(t);
	}

	public void onRemoved() {
		for (Component c : getComponents())
			c.onRemoved();
	}

	public void onAdded() {
		for (Component c : getComponents())
			c.onAdded();
	}

}
