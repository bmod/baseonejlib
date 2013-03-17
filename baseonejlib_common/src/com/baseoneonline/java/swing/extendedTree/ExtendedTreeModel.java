package com.baseoneonline.java.swing.extendedTree;

import java.util.HashMap;
import java.util.List;

import javax.swing.Icon;

import com.baseoneonline.java.swing.AbstractTreeModel;

public class ExtendedTreeModel extends AbstractTreeModel {

	private Object rootNode;

	private final HashMap<Class<?>, ExtendedNodeAdapter<?>> adapters = new HashMap<Class<?>, ExtendedNodeAdapter<?>>();

	public ExtendedTreeModel() {
	}

	public String getLabel(Object o) {
		ExtendedNodeAdapter<Object> adapter = getAdapter(o);

		if (null == adapter)
			return o.toString();

		return getAdapter(o).getLabel(o);
	}

	public Icon getIcon(Object value) {
		ExtendedNodeAdapter<Object> adapter = getAdapter(value);
		if (null != adapter) {
			return adapter.getIcon(value);
		}
		return null;
	}

	public void addAdapter(ExtendedNodeAdapter<?> adapter) {
		adapters.put(adapter.getType(), adapter);
	}

	@SuppressWarnings("unchecked")
	private <T> ExtendedNodeAdapter<T> getAdapter(T object) {
		Class<T> type = (Class<T>) object.getClass();
		for (Class<?> other : adapters.keySet()) {
			if (other.isAssignableFrom(type))
				return (ExtendedNodeAdapter<T>) adapters.get(other);
		}
		return null;
	}

	public void setRoot(final Object rootNode) {
		this.rootNode = rootNode;
		fireTreeStructureChanged();
	}

	public Object getRootNode() {
		return rootNode;
	}

	@Override
	public Object getRoot() {
		return rootNode;
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		return getAdapter(parent).getChildren(parent).get(index);
	}

	@Override
	public int getChildCount(final Object parent) {
		ExtendedNodeAdapter<Object> adapter = getAdapter(parent);

		if (null == adapter)
			return 0;

		List<Object> children = adapter.getChildren(parent);
		if (children == null)
			return 0;

		return children.size();
	}

	@Override
	public boolean isLeaf(final Object node) {
		return getChildCount(node) == 0;
	}

}
