package com.baseoneonline.java.swing.mediaTree;

import java.io.File;

public class FileNode extends File implements MediaNode {

	private MediaNode[] children;
	private final boolean leaf;

	public FileNode(final String pathname) {
		super(pathname);
		leaf = isFile();
	}

	public FileNode(final File file) {
		super(file.getAbsolutePath());
		leaf = isFile();
	}

	@Override
	public MediaNode[] getChildren() {
		if (null == children) {
			final File[] files = listFiles();
			children = new FileNode[files.length];
			for (int i = 0; i < files.length; i++) {
				children[i] = new FileNode(files[i]);
			}
		}
		return children;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean isLeaf() {
		return leaf;
	}

}
