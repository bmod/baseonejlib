package com.baseoneonline.java.media;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.baseoneonline.java.media.nodes.INode;

public class NodeTreeModel implements TreeModel {

	List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	INode root;

	public NodeTreeModel(final INode root) {
		this.root = root;
	}

	@Override
	public void addTreeModelListener(final TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		return ((INode) parent).getChildAt(index);
	}

	@Override
	public int getChildCount(final Object parent) {
		return ((INode) parent).numChildren();
	}

	@Override
	public int getIndexOfChild(final Object parent, final Object child) {
		final INode parentNode = (INode) parent;
		final int len = parentNode.numChildren();
		for (int i = 0; i < len; i++) {
			if (parentNode.getChildAt(i) == child) return i;
		}
		return -1;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(final Object node) {
		return ((INode) node).isLeaf();
	}

	@Override
	public void removeTreeModelListener(final TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public void valueForPathChanged(final TreePath path, final Object newValue) {}

}
