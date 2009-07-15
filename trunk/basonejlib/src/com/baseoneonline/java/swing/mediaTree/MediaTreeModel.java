package com.baseoneonline.java.swing.mediaTree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class MediaTreeModel implements TreeModel {

	List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	MediaNode root;

	public MediaTreeModel(final MediaNode root) {
		this.root = root;
	}

	@Override
	public void addTreeModelListener(final TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		return ((MediaNode) parent).getChildren()[index];
	}

	@Override
	public int getChildCount(final Object parent) {
		return ((MediaNode) parent).getChildren().length;
	}

	@Override
	public int getIndexOfChild(final Object parent, final Object child) {
		final MediaNode[] children = ((MediaNode) parent).getChildren();
		final int len = children.length;
		for (int i = 0; i < len; i++) {
			if (children[i] == child) return i;
		}
		return -1;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(final Object node) {
		return ((MediaNode) node).isLeaf();
	}

	@Override
	public void removeTreeModelListener(final TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public void valueForPathChanged(final TreePath path, final Object newValue) {}

}
