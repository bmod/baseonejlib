package com.baseoneonline.java.jme;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class JMETreeModel implements TreeModel {

	private Node rootNode;

	List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	public JMETreeModel(Node rootNode) {
		this.rootNode = rootNode;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		return ((Node) parent).getChild(index);
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof Node) {
			List<Spatial> children = ((Node) parent).getChildren();
			if (null != children)
				return children.size();
		}
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return ((Node) parent).getChildIndex((Spatial) child);
	}

	@Override
	public Object getRoot() {
		return rootNode;
	}

	@Override
	public boolean isLeaf(Object node) {
		return getChildCount(node) <= 0;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// no editing!
	}

	public void fireStructureChanged() {
		TreePath path = new TreePath(rootNode);
		TreeModelEvent ev = new TreeModelEvent(this, path);
		for (TreeModelListener l : listeners) {
			l.treeStructureChanged(ev);
		}
	}

}
