package com.baseoneonline.java.jlib;

import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TypedTreeModel implements TreeModel {
	
	Vector<TreeModelListener> listeners = new Vector<TreeModelListener>();

	private TreeNode root;
	
	
	@Override
	public Object getChild(Object parent, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf(Object node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		if (!listeners.contains(l)) listeners.add(l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		if (listeners.contains(l)) listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}

}
