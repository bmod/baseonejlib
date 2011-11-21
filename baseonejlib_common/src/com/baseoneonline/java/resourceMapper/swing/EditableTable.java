package com.baseoneonline.java.resourceMapper.swing;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

class EditableTable extends JTable
{
	private Class<?> editingClass;

	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);

		if (modelColumn == 1)
		{
			Class<?> rowClass = getModel().getValueAt(row, modelColumn)
					.getClass();
			return getDefaultRenderer(rowClass);
		} else
			return super.getCellRenderer(row, column);
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column)
	{
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);

		if (modelColumn == 1)
		{
			editingClass = getModel().getValueAt(row, modelColumn).getClass();
			return getDefaultEditor(editingClass);
		} else
			return super.getCellEditor(row, column);
	}

	// This method is also invoked by the editor when the value in the editor
	// component is saved in the TableModel. The class was saved when the
	// editor was invoked so the proper class can be created.

	@Override
	public Class<?> getColumnClass(int column)
	{
		return editingClass != null ? editingClass : super
				.getColumnClass(column);
	}
}