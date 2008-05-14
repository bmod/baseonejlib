package com.baseoneonline.java.jlib;

public interface TreeNode {
	
	TreeNode getParent();
	
	TreeNode[] getChildren();
	
	void addChild(TreeNode n);

	void addChildAt(TreeNode n, int index);
	
	void removeChild(TreeNode n);
	
	void removeChildAt(int index);
	
}
