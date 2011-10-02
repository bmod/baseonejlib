package com.baseoneonline.java.swing;

import java.util.HashSet;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.baseoneonline.java.nanoxml.XMLElement;

/**
 * @author bmod
 * 
 * 
 * 
 */
public class XMLTreeModel implements TreeModel
{

	private final HashSet<TreeModelListener> listeners = new HashSet<TreeModelListener>();

	private String leafNodeName = null;

	private String labelField = "name";

	private XMLElement xmlRoot;

	public XMLTreeModel()
	{
	}

	public String getLabelField()
	{
		return labelField;
	}

	public void setLabelField(String labelField)
	{
		this.labelField = labelField;
	}

	public String getLeafNodeName()
	{
		return leafNodeName;
	}

	public void setXmlRoot(XMLElement xmlRoot)
	{
		this.xmlRoot = xmlRoot;
		fireStructureChanged();
	}

	public XMLElement getXmlRoot()
	{
		return xmlRoot;
	}

	/**
	 * @param leafNodeName
	 *            The XML node name to consider a leaf node. Providing null will
	 *            ignore this.
	 */
	public void setLeafNodeName(String leafNodeName)
	{
		this.leafNodeName = leafNodeName;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l)
	{
		listeners.add(l);
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		return ((XMLElement) parent).getChildren().get(index);
	}

	@Override
	public int getChildCount(Object parent)
	{
		if (null == xmlRoot)
			return 0;
		if (parent instanceof XMLElement)
			return ((XMLElement) parent).getChildren().size();
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		return ((XMLElement) parent).getChildren().indexOf(child);
	}

	@Override
	public Object getRoot()
	{
		return xmlRoot;
	}

	@Override
	public boolean isLeaf(Object node)
	{
		if (null == leafNodeName)
			return ((XMLElement) node).getChildren().isEmpty();

		return ((XMLElement) node).getName().equals(leafNodeName);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l)
	{
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		throw new UnsupportedOperationException();
	}

	private void fireStructureChanged()
	{
		Object[] path = { xmlRoot };
		TreeModelEvent ev = new TreeModelEvent(this, path);
		for (TreeModelListener l : listeners)
			l.treeStructureChanged(ev);
	}

}
