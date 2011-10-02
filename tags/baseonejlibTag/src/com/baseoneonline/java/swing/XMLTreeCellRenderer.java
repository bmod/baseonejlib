package com.baseoneonline.java.swing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLTreeCellRenderer extends DefaultTreeCellRenderer
{
	private String labelAttribute = "name";

	public void setLabelAttribute(String labelAttribute)
	{
		this.labelAttribute = labelAttribute;
	}

	public String getLabelAttribute()
	{
		return labelAttribute;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value,
				sel, expanded, leaf, row, hasFocus);
		XMLElement xml;
		try
		{
			xml = (XMLElement) value;
		} catch (Exception e)
		{
			throw new RuntimeException(
					"This component should be used only in conjunction with XMLElement objects");
		}
		String txt = xml.getStringAttribute(labelAttribute);
		if (null == txt || txt.isEmpty())
		{
			label.setText(xml.getName());
		} else
		{
			label.setText(txt);
		}

		return label;
	}
}
