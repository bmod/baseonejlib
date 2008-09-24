package com.baseoneonline.java.mediadb.db;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class DatabaseListModel extends AbstractListModel {

	private final ArrayList<ImageRecord> data = new ArrayList<ImageRecord>();
	private final ObjectContainer db;
	private ObjectSet<ImageRecord> result;

	public DatabaseListModel(ObjectContainer db) {
		this.db = db;
	}

	public void refresh() {
		ImageRecord proto = new ImageRecord(null);
		result = db.get(proto);

	}

	public Object getElementAt(int arg0) {
		return result.get(arg0);
	}

	public int getSize() {
		if (null == result) return 0;
		return result.size();
		
	}

}
