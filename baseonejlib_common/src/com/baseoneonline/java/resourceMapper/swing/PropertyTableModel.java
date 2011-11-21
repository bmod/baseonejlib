package com.baseoneonline.java.resourceMapper.swing;

import javax.swing.table.AbstractTableModel;

import com.baseoneonline.java.resourceMapper.Resource;
import com.baseoneonline.java.resourceMapper.ResourceProperty;
import com.baseoneonline.java.resourceMapper.ResourceUtil;

class PropertyTableModel extends AbstractTableModel
{
	private final String[] columnNames =
	{ "Key", "Value" };
	private Resource res = null;

	public void setResource(Resource res)
	{
		this.res = res;
		fireTableDataChanged();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		// Second column is always editable
		return columnIndex == 1;
	}

	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}

	@Override
	public int getRowCount()
	{
		if (null == res)
			return 0;

		return ResourceUtil.getProperties(res).size();

	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		ResourceProperty prop = ResourceUtil.getProperties(res).get(rowIndex);

		switch (columnIndex)
		{
		case 0:
			return prop.getId();
		case 1:
			return prop.getValue();
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		ResourceProperty prop = ResourceUtil.getProperties(res).get(rowIndex);
		if (columnIndex == 1)
		{
			prop.setValue(aValue);
		}

	}

}