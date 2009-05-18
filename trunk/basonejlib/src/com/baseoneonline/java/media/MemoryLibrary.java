package com.baseoneonline.java.media;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class MemoryLibrary extends Library {

	private final HashMap<String, MediaItem> items = new HashMap<String, MediaItem>();
	private TreeModel treeModel;

	@Override
	public void add(final MediaItem item) {
		items.put(item.file.getAbsolutePath(), item);
	}

	public TreeModel getTreeModel() {
		if (null == treeModel) {
			treeModel = new MyTreeModel();
		}
		return treeModel;
	}

	private class MyTreeModel implements TreeModel {

		List<TreeModelListener> listeners = new Vector<TreeModelListener>();

		@Override
		public void addTreeModelListener(final TreeModelListener l) {
			listeners.add(l);
		}

		@Override
		public Object getChild(final Object parent, final int index) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getChildCount(final Object parent) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getIndexOfChild(final Object parent, final Object child) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getRoot() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isLeaf(final Object node) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeTreeModelListener(final TreeModelListener l) {
			listeners.remove(l);
		}

		@Override
		public void valueForPathChanged(final TreePath path,
				final Object newValue) {
		// TODO Auto-generated method stub

		}

	}

}
