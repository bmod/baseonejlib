package com.baseoneonline.java.mediabrowser.core;

import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TreeModelAdapter implements TreeModel, DataViewListener {
	
	private final DataView dataView;
	
	private ArrayList<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
	
	public TreeModelAdapter(final DataView dataView) {
		this.dataView = dataView;
		dataView.addListener(this);
	}

	@Override
	public void addTreeModelListener(final TreeModelListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		return dataView.getChildren((MediaNode) parent).get(index);
	}

	@Override
	public int getChildCount(final Object parent) {
		return dataView.getChildren((MediaNode) parent).size();
	}

	@Override
	public int getIndexOfChild(final Object parent, final Object child) {
		return dataView.getChildren((MediaNode)parent).indexOf(child);
	}

	@Override
	public Object getRoot() {
		return dataView.getRoot();
	}

	@Override
	public boolean isLeaf(final Object child) {
		return child instanceof MediaFile;
	}

	@Override
	public void removeTreeModelListener(final TreeModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(final TreePath arg0, final Object arg1) {
		throw new RuntimeException("Not implemented");
	}

}
