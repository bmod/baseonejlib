package com.baseoneonline.java.mediabrowser.core;

import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public abstract class DataTreeModel implements TreeModel, DataViewListener {

	private final ArrayList<TreeModelListener> listeners =
			new ArrayList<TreeModelListener>();

	private final DataView view;

	public DataTreeModel(final DataView view) {
		this.view = view;
		view.addListener(this);
	}

	@Override
	public void addTreeModelListener(final TreeModelListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		return view.getChildren((MediaNode) parent).get(index);
	}

	@Override
	public int getChildCount(final Object parent) {
		return view.getChildren((MediaNode) parent).size();
	}

	@Override
	public int getIndexOfChild(final Object parent, final Object child) {
		return view.getChildren((MediaNode) parent).indexOf(child);
	}

	@Override
	public Object getRoot() {
		return view.getRoot();
	}

	@Override
	public boolean isLeaf(final Object node) {
		return node instanceof MediaNode;
	}

	@Override
	public void removeTreeModelListener(final TreeModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(final TreePath path, final Object newValue) {
		throw new RuntimeException("Not implemented!");
	}

}
