package com.baseoneonline.java.swing.extendedTree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ExtendedCellRenderer extends DefaultTreeCellRenderer {

	private final ExtendedTreeModel model;

	public ExtendedCellRenderer(ExtendedTreeModel model) {
		this.model = model;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Component comp = super.getTreeCellRendererComponent(tree, value, sel,
				expanded, leaf, row, hasFocus);

		setText(model.getLabel(value));
		Icon icon = model.getIcon(value);
		if (null != icon)
			setIcon(model.getIcon(value));

		return comp;
	}
}