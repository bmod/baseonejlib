package com.baseoneonline.java.mediadb.db;

import javax.swing.AbstractListModel;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class DatabaseListModel extends AbstractListModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4287794057450110812L;

	private ObjectSet<Item> result;

	public boolean showImages = false;
	public boolean showMusic = false;
	public boolean showMovies = false;

	public DatabaseListModel(final ObjectContainer db) {
	}

	public void clear() {
		result = null;
		fireContentsChanged(this, 0,0);
	}



	public void refresh() {
		/*
		result = null;
		Query q = db.query();
		q.constrain(MediaItem.class);
		if (showImages)
			q.descend("type").constrain(new Integer(MediaItem.TYPE_IMAGE));

		result = q.execute();
		fireContentsChanged(this, 0, result.size() - 1);
		 */
	}

	public Object getElementAt(final int arg0) {
		return result.get(arg0);
	}

	public int getSize() {
		if (null == result) {
			return 0;
		}
		return result.size();

	}

}
