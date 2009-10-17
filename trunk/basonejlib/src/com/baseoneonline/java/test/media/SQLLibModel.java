package com.baseoneonline.java.test.media;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.baseoneonline.java.test.media.library.SQLLibrary;
import com.baseoneonline.java.test.media.library.items.MediaItem;

public class SQLLibModel extends AbstractTableModel {

	private final String[] fields = { MediaItem.FIELD_ARTIST,
			MediaItem.FIELD_ALBUM, MediaItem.FIELD_TITLE };

	SQLLibrary lib;
	List<MediaItem> items = new ArrayList<MediaItem>();

	public SQLLibModel(final SQLLibrary lib) {
		this.lib = lib;
		items = lib.getItems();
	}

	@Override
	public int getColumnCount() {
		return fields.length;
	}

	@Override
	public int getRowCount() {
		return items.size();
	}

	@Override
	public Object getValueAt(final int row, final int col) {
		return items.get(row).getAttribute(fields[col]);
	}

	@Override
	public String getColumnName(final int col) {
		return fields[col];
	}

	public void filter(final String trim) {
		if (trim.length() > 0) {
			items = lib.getItemsFiltered(trim);
		} else {
			items = lib.getItems();
		}
		fireTableDataChanged();
	}

}
