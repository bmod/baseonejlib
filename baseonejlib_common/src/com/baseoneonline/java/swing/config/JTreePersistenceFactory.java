package com.baseoneonline.java.swing.config;

import java.util.StringTokenizer;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class JTreePersistenceFactory implements PersistenceFactory<JTree>
{

	@Override
	public void store(Config conf, String key, JTree tree)
	{
		conf.putString(key + "ExpandedPaths", getExpansionState(tree, 0));

	}

	@Override
	public void restore(Config conf, String key, JTree tree)
	{
		String exp = conf.getString(key + "ExpandedPaths", null);
		if (null != exp)
			restoreExpansionState(tree, 0, exp);
	}

	private String getExpansionState(JTree tree, int row)
	{
		TreePath rowPath = tree.getPathForRow(row);
		StringBuffer buf = new StringBuffer();
		int rowCount = tree.getRowCount();
		for (int i = row; i < rowCount; i++)
		{
			TreePath path = tree.getPathForRow(i);
			if (i == row || isDescendant(path, rowPath))
			{
				if (tree.isExpanded(path))
					buf.append("," + String.valueOf(i - row));
			} else
				break;
		}
		return buf.toString();
	}

	private boolean isDescendant(TreePath path1, TreePath path2)
	{
		int count1 = path1.getPathCount();
		int count2 = path2.getPathCount();
		if (count1 <= count2)
			return false;
		while (count1 != count2)
		{
			path1 = path1.getParentPath();
			count1--;
		}
		return path1.equals(path2);
	}

	private void restoreExpansionState(JTree tree, int row,
			String expansionState)
	{
		StringTokenizer stok = new StringTokenizer(expansionState, ",");
		while (stok.hasMoreTokens())
		{
			int token = row + Integer.parseInt(stok.nextToken());
			tree.expandRow(token);
		}
	}

	@Override
	public Class<? extends JTree> getType()
	{
		return JTree.class;
	}

}
