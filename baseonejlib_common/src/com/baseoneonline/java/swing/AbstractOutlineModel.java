package com.baseoneonline.java.swing;

import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.OutlineModel;
import org.netbeans.swing.outline.RowModel;

public abstract class AbstractOutlineModel extends AbstractTreeModel implements
		RowModel
{
	public static OutlineModel create(AbstractOutlineModel model)
	{
		return DefaultOutlineModel.createOutlineModel(model, model);
	}
}
