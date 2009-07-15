package com.baseoneonline.java.media.nodes;

public interface INode {

	public boolean isLeaf();

	public int numChildren();

	public INode getChildAt(int i);

}
