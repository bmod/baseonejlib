package com.baseoneonline.jlib.ardor3d.framework;

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

	public Entity() {
		node = new Node();
	}

	public Entity(String name) {
		node = new Node(name);
	}

	public Node getNode() {
		return node;
	}

	public String getName() {
		return node.getName();
	}

	public void setName(String name) {
		node.setName(name);
	}

	public void addComponent(Component c) {
		Class<? extends Component> type = c.getClass();
		if (components.containsKey(type))
			throw new RuntimeException("Component type already added: " + c);
		components.put(type, c);
		c.setOwner(this);
		c.onAdded();
	}

	public void removeComponent(Component c) {
		components.remove(c.getClass());
		c.onRemoved();
		c.setOwner(null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> type) {
		return (T) components.get(type);
	}

	private Collection<? extends Component> getComponents() {
		return components.values();
	}

	public void update(double t) {
		for (Component c : getComponents())
			c.update(t);
	}

	public void setTransform(ReadOnlyTransform xf) {
		PhysicsComponent phys = getComponent(PhysicsComponent.class);
		if (null != phys) {
			phys.setTransform(xf);
		} else {
			node.setTransform(xf);
		}
	}

	@Override
	public void write(OutputCapsule capsule) throws IOException {
		capsule.write(getName(), "name", null);
		capsule.writeSavableList(new ArrayList<Savable>(components.values()),
				"components", null);
	}

	@Override
	public void read(InputCapsule capsule) throws IOException {
		setName(capsule.readString("name", null));
		List<Component> components = capsule.readSavableList("components",
				Collections.<Component> emptyList());
		this.components.clear();
		for (Component c : components) {
			System.out.println(c);
			addComponent(c);
		}
	}

	@Override
	public Class<?> getClassTag() {
		return null;
	}
}
