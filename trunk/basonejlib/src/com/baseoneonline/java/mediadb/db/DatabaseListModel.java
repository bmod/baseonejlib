package com.baseoneonline.java.mediadb.db;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

public class DatabaseListModel<T> extends AbstractListModel {

	private final ArrayList<T> data = new ArrayList<T>();
	
	public DatabaseListModel() {
	}
	
	public void add(T item) {
		data.add(item);
		fireContentsChanged(this, data.size()-1, data.size()-1);
	}
	
	public Object getElementAt(int arg0) {
		return data.get(arg0);
	}

	public int getSize() {
		return data.size();
	}

}
