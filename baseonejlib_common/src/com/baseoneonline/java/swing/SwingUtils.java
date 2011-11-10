package com.baseoneonline.java.swing;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class SwingUtils
{
	public static void setContextMenu(final Component comp,
			final JPopupMenu menu)
	{
		comp.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mousePressed(final MouseEvent e)
			{
				showPopup(e);
			}

			@Override
			public void mouseReleased(final MouseEvent e)
			{
				showPopup(e);
			}

			private void showPopup(final MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					menu.show(comp, e.getX(), e.getY());
				}
			}
		});
	}

	public static void expandAll(JTree tree, boolean expand)
	{
		expandAll(tree, new TreePath(tree.getModel().getRoot()), expand);
	}

	private static void expandAll(JTree tree, TreePath parent, boolean expand)
	{
		// Traverse children
		Object node = parent.getLastPathComponent();

		for (int i = 0; i < tree.getModel().getChildCount(node); i++)
		{
			Object child = tree.getModel().getChild(node, i);
			TreePath path = parent.pathByAddingChild(child);
			expandAll(tree, path, expand);
		}
		if (expand)
		{
			tree.expandPath(parent);
		} else
		{
			tree.collapsePath(parent);
		}
	}
}
