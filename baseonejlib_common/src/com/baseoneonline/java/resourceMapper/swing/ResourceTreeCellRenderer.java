package com.baseoneonline.java.resourceMapper.swing;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.baseoneonline.java.resourceMapper.ListResource;
import com.baseoneonline.java.resourceMapper.Resource;
import com.baseoneonline.java.resourceMapper.ResourceNode;
import com.baseoneonline.java.resourceMapper.SetResource;

public class ResourceTreeCellRenderer extends DefaultTreeCellRenderer
{

	private static HashMap<Class<?>, Icon> iconMap = new HashMap<Class<?>, Icon>();

	static
	{
		iconMap.put(Resource.class, getIcon("resource.png"));
		iconMap.put(SetResource.class, getIcon("setresource.png"));
		iconMap.put(ListResource.class, getIcon("listresource.png"));
	}

	private static Icon getIcon(String name)
	{
		return new ImageIcon(ResourceTreeCellRenderer.class.getResource(name));
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		Component comp = super.getTreeCellRendererComponent(tree, value, sel,
				expanded, leaf, row, hasFocus);

		if (comp instanceof JLabel)
		{
			JLabel label = (JLabel) comp;
			if (value instanceof ResourceNode)
			{
				Icon icon = iconMap.get(((ResourceNode) value).getResource()
						.getClass());
				if (null != icon)
				{
					label.setIcon(icon);
				} else
				{
					label.setIcon(iconMap.get(Resource.class));
				}
			}

		}

		return comp;
	}

}
