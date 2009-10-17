package com.baseoneonline.java.test.media.nodes;

public interface INode {

	public boolean isLeaf();

	public int numChildren();

	public INode getChildAt(int i);

}
