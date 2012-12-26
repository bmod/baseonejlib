package com.baseoneonline.jlib.ardor3d.tools;

import com.ardor3d.scenegraph.Node;
import com.baseoneonline.java.swing.AbstractTreeModel;

public class SceneGraphModel extends AbstractTreeModel {

	private Node rootNode;

	public SceneGraphModel() {
		System.out.println("Hello here i am");
	}

	public void setRootNode(final Node rootNode) {
		this.rootNode = rootNode;
		fireTreeStructureChanged();
	}

	public Node getRootNode() {
		return rootNode;
	}

	@Override
	public Object getRoot() {
		return rootNode;
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		if (parent instanceof Node)
			return ((Node) parent).getChild(index);
		return null;
	}

	@Override
	public int getChildCount(final Object parent) {
		if (parent instanceof Node)
			return ((Node) parent).getNumberOfChildren();
		return 0;
	}

	@Override
	public boolean isLeaf(final Object node) {
		return getChildCount(node) == 0;
	}
}
