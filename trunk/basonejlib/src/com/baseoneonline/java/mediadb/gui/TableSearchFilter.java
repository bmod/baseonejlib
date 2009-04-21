package com.baseoneonline.java.mediadb.gui;

import javax.swing.RowFilter;

public class TableSearchFilter extends RowFilter<Object, Object> {

	private final int[] searchFields;

	private String searchString = "";

	public TableSearchFilter(final int[] searchFields) {
		this.searchFields = searchFields;
	}

	public void setSearchString(final String searchString) {
		this.searchString = searchString;
	}

	@Override
	public boolean include(
			final javax.swing.RowFilter.Entry<? extends Object, ? extends Object> entry) {
		if (""==searchString) {
			return true;
		}

		for (final int field : searchFields) {
			if (entry.getStringValue(field).contains(searchString)) {
				return true;
			}
		}

		return false;
	}
}
