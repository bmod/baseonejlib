package com.baseoneonline.java.swing;

import java.util.HashSet;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public abstract class AbstractTreeModel implements TreeModel
{

	private final HashSet<TreeModelListener> listeners = new HashSet<TreeModelListener>();

	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{
	}

	@Override
	public void addTreeModelListener(TreeModelListener l)
	{
		listeners.add(l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l)
	{
		listeners.remove(l);
	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		int childCount = getChildCount(parent);
		for (int i = 0; i < childCount; i++)
		{
			if (getChild(parent, i) == child)
				return i;
		}
		return -1;
	}

	public void fireTreeStructureChanged()
	{

		Object[] path = { getRoot() };
		TreeModelEvent ev = new TreeModelEvent(this, path);
		for (TreeModelListener l : listeners)
		{

			l.treeStructureChanged(ev);
		}
	}

	public void fireNodesChanged(TreePath path)
	{
		TreeModelEvent ev = new TreeModelEvent(this, path);
		for (TreeModelListener l : listeners)
		{
			l.treeNodesChanged(ev);
		}
	}

	public void fireNodesInserted(TreePath treePath)
	{
		TreeModelEvent ev = new TreeModelEvent(this, treePath);
		for (TreeModelListener l : listeners)
		{

			l.treeNodesInserted(ev);
		}
	}

	public void fireNodesRemoved(TreePath path)
	{
		TreeModelEvent ev = new TreeModelEvent(this, path);
		for (TreeModelListener l : listeners)
		{
			l.treeNodesRemoved(ev);
		}

	}
}
