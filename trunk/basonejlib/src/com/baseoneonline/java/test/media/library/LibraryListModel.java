package com.baseoneonline.java.media.library;

import javax.swing.AbstractListModel;

public class LibraryListModel extends AbstractListModel {

	LibraryView lib;
	
	public LibraryListModel(LibraryView library) {
		lib = library;
		fireContentsChanged(this, 0, getSize());
	}
	
	@Override
	public Object getElementAt(int i) {
		return lib.get(i);
	}

	@Override
	public int getSize() {
		return lib.size();
	}

}
