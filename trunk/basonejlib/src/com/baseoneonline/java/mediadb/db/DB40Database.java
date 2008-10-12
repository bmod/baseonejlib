package com.baseoneonline.java.mediadb.db;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

public class DB40Database extends AbstractDatabase {

	private ObjectContainer db;
	private final String dbFile;
	
	public DB40Database(String db4oFile) {
		dbFile = db4oFile;
	}
	
	
	@Override
	public void addItem(Item item) {
		db.set(item);
	}

	@Override
	public boolean contains(Item item) {
		db.get(item);
		return false;
	}

	@Override
	public Item getItem(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadDatabase() {
		db = Db4o.openFile(dbFile);
	}

	@Override
	public void removeItem(Item item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeDatabase() {
		// TODO Auto-generated method stub

	}

}
