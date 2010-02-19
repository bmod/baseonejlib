package com.baseoneonline.java.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Example of a simple static TreeModel. It contains a (java.io.File) directory
 * structure. (C) 2001 Christian Kaufhold (ch-kaufhold@gmx.de)
 */

public class FileTreeModel implements TreeModel {

	FileNode root;

	HashMap<FileNode, ArrayList<FileNode>> map = new HashMap<FileNode, ArrayList<FileNode>>();

	ArrayList<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	public FileTreeModel(final String path) {
		this.root = new FileNode(path);
	}

	@Override
	public void addTreeModelListener(final TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		return getChildren((FileNode) parent).get(index);
	}

	private List<FileNode> getChildren(final FileNode parent) {
		final FileNode par = parent;
		List<FileNode> children = map.get(par);
		if (null == children) {
			children = new ArrayList<FileNode>();
			for (final File f : par.listFiles()) {
				children.add(new FileNode(f));
			}
		}
		return children;
	}

	@Override
	public int getChildCount(final Object parent) {
		return getChildren((FileNode) parent).size();
	}

	@Override
	public int getIndexOfChild(final Object parent, final Object child) {
		final List<FileNode> children = getChildren((FileNode) parent);
		final int len = children.size();
		for (int i = 0; i < len; i++) {
			if (children.get(i) == child) return i;
		}
		return 0;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(final Object node) {
		return ((FileNode) node).isFile();
	}

	@Override
	public void removeTreeModelListener(final TreeModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(final TreePath arg0, final Object arg1) {

	}

}

class FileNode extends File {

	public FileNode(final String pathname) {
		super(pathname);
	}

	public FileNode(final File root) {
		super(root.getAbsolutePath());
	}

	@Override
	public String toString() {
		return getName();
	}

}