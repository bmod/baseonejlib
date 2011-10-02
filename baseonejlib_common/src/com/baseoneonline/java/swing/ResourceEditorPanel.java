package com.baseoneonline.java.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.baseoneonline.java.resourceMapper.ListResource;
import com.baseoneonline.java.resourceMapper.Resource;

public class ResourceEditorPanel extends JPanel
{

	private final ResourceTreeModel treeModel = new ResourceTreeModel();
	private JTree tree;

	/**
	 * Create the panel.
	 */
	public ResourceEditorPanel()
	{

		initComponents();
		tree.setCellRenderer(new MyTreeCellRenderer());
		tree.setRootVisible(false);
		tree.setModel(treeModel);
	}

	public void setResource(final Resource resource)
	{
		treeModel.setRoot(resource);
	}

	public Resource getResource()
	{
		return (Resource) treeModel.getRoot();
	}

	private void initComponents()
	{
		setLayout(new BorderLayout(0, 0));

		final JSplitPane splitPane = new JSplitPane();
		add(splitPane);

		final JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		tree = new JTree();
		scrollPane.setViewportView(tree);
	}

}

class MyTreeCellRenderer extends DefaultTreeCellRenderer
{
	@Override
	public Component getTreeCellRendererComponent(final JTree tree,
			final Object value, final boolean sel, final boolean expanded,
			final boolean leaf, final int row, final boolean hasFocus)
	{

		final JLabel comp = (JLabel) super.getTreeCellRendererComponent(tree,
				value, sel, expanded, leaf, row, hasFocus);

		String label;

		if (value instanceof ListResource)
			label = ((ListResource<?>) value).name;
		else
			label = value.getClass().getSimpleName();

		comp.setText(label);
		return comp;
	}
}

class ResourceTreeModel extends AbstractTreeModel
{

	private Resource root;

	public ResourceTreeModel()
	{

	}

	public void setRoot(final Resource root)
	{
		this.root = root;
		fireTreeStructureChanged();
	}

	@Override
	public Object getRoot()
	{
		return root;
	}

	@Override
	public Object getChild(final Object parent, final int index)
	{
		if (parent instanceof ListResource<?>)
			return ((ListResource<?>) parent).get(index);
		return getChildren(parent).get(index);

	}

	private List<Object> getChildren(final Object parent)
	{

		final List<Object> children = new ArrayList<Object>();

		try
		{
			for (final Field field : parent.getClass().getFields())
			{
				if (ListResource.class.isAssignableFrom(field.getType()))
				{
					children.add(field.get(parent));
				} else if (Resource.class.isAssignableFrom(field.getType()))
				{
					final Resource child = (Resource) field.get(parent);
					children.add(child);
				}
			}
		} catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
		return children;

	}

	@Override
	public int getChildCount(final Object parent)
	{
		if (parent instanceof ListResource<?>)
			return ((ListResource<?>) parent).size();
		return parent.getClass().getFields().length;
	}

	@Override
	public boolean isLeaf(final Object node)
	{
		return false;
	}

}
