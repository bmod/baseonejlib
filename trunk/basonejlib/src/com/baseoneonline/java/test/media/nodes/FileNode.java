package com.baseoneonline.java.media.nodes;

import java.io.File;
import java.util.ArrayList;

import com.baseoneonline.java.media.Launcher;

public class FileNode implements INode {

	private FileNode[] childNodes;
	private final boolean leaf;
	private final String label;
	public final File file;
	private final Launcher launcher;

	public FileNode(final String path, final Launcher launcher) {
		this(new File(path), launcher);
	}

	public FileNode(final File file) {
		this(file, null);
	}

	public FileNode(final String path, final String name,
			final Launcher launcher) {
		this(new File(path), null, launcher);
	}

	public FileNode(final File file, final Launcher launcher) {
		this(file, null, launcher);
	}

	public FileNode(final File file, final String name, final Launcher launcher) {
		this.file = file;
		this.launcher = launcher;
		this.label = (null != name) ? name : file.getName();
		leaf = file.isFile();
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public boolean isLeaf() {
		return leaf;
	}

	@Override
	public INode getChildAt(final int i) {
		return childNodes[i];
	}

	@Override
	public int numChildren() {
		if (null == childNodes) childNodes = fetchChildren();
		return childNodes.length;
	}

	private FileNode[] fetchChildren() {

		final ArrayList<FileNode> nodes = new ArrayList<FileNode>();
		File[] files = null;

		files = file.listFiles(launcher.fileFilter);

		for (final File f : files) {
			final FileNode n = new FileNode(f, launcher);
			nodes.add(n);
		}
		return nodes.toArray(new FileNode[nodes.size()]);
	}
}
