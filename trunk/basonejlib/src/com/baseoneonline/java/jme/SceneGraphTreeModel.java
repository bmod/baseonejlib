package com.baseoneonline.java.jme;

import java.util.ArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class SceneGraphTreeModel implements TreeModel {

    ArrayList<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
    private Node rootNode;

    @Override
    public void addTreeModelListener(final TreeModelListener l) {
	listeners.add(l);
    }

    public Node getRootNode() {
	return rootNode;
    }

    public void setRootNode(final Node rootNode) {
	this.rootNode = rootNode;
	fireTreeStructureChanged();
    }

    @Override
    public Object getChild(final Object parent, final int index) {
	if (parent instanceof Node) {
	    return ((Node) parent).getChild(index);
	}
	return null;
    }

    @Override
    public int getChildCount(final Object parent) {
	if (parent instanceof Node) {
	    return ((Node) parent).getChildren().size();
	}
	return 0;
    }

    @Override
    public int getIndexOfChild(final Object parent, final Object child) {
	if (parent instanceof Node) {
	    return ((Node) parent).getChildIndex((Spatial) child);
	}
	return 0;
    }

    @Override
    public Object getRoot() {
	return rootNode;
    }

    @Override
    public boolean isLeaf(final Object node) {
	return false;
    }

    @Override
    public void removeTreeModelListener(final TreeModelListener l) {
	listeners.remove(l);
    }

    void fireTreeStructureChanged() {
	final Object[] path = { rootNode };
	final TreeModelEvent ev = new TreeModelEvent(this, path);
	for (final TreeModelListener l : listeners) {
	    l.treeStructureChanged(ev);
	}
    }

    @Override
    public void valueForPathChanged(final TreePath path, final Object newValue) {
	// TODO Auto-generated method stub
    }

}
