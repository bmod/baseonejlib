package com.baseoneonline.java.mediabrowser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

public class SimpleListModel extends AbstractListModel {
	
	private final ArrayList<Object> data = new ArrayList<Object>();
	
	public void addItems(final List<?> items) {
		final int start = data.size();
		data.addAll(items);
		//fireContentsChanged(this, 0, data.size());
		fireIntervalAdded(this, start, data.size()-1);
	}
	
	
	@Override
	public Object getElementAt(final int index) {
		return data.get(index);
	}

	@Override
	public int getSize() {
		return data.size();
	}
}
