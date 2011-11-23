package com.baseoneonline.java.swing.config;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class JTablePersistenceFactory implements PersistenceFactory<JTable>
{

	private static final String COLUMN_WIDTHS = "ColumnWidths";

	@Override
	public void store(final Config conf, final String key, final JTable value)
	{

		conf.putIntArray(key + COLUMN_WIDTHS, getColumnWidths(value));
	}

	@Override
	public void restore(final Config conf, final String key, final JTable value)
	{
		// TODO: REstore final column table widths

		setColumnWidths(value, conf.getIntArray(key + COLUMN_WIDTHS, null));
		value.setModel(value.getModel());
	}

	private int[] getColumnWidths(final JTable table)
	{

		final TableColumnModel colModel = table.getColumnModel();
		final int[] re = new int[colModel.getColumnCount()];
		for (int i = 0; i < colModel.getColumnCount(); i++)
		{
			final TableColumn col = colModel.getColumn(i);
			re[i] = col.getWidth();
		}
		return re;
	}

	private void setColumnWidths(final JTable table, final int[] widths)
	{

		if (null == widths || widths.length != table.getColumnCount())
			return;

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		final TableColumnModel colModel = table.getColumnModel();

		for (int i = 0; i < widths.length; i++)
		{
			final TableColumn col = colModel.getColumn(i);
			col.setPreferredWidth(widths[i]);
		}
	}

	@Override
	public Class<? extends JTable> getType()
	{
		return JTable.class;
	}

}
