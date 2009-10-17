package com.baseoneonline.java.media.nodes;

import java.util.ArrayList;

public class DefaultNode implements INode {

	private final String label;
	ArrayList<INode> children = new ArrayList<INode>();

	public DefaultNode(final String label) {
		this.label = label;
	}

	public void add(final INode node) {
		children.add(node);
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public INode getChildAt(final int i) {
		return children.get(i);
	}

	@Override
	public int numChildren() {
		return children.size();
	}

}
