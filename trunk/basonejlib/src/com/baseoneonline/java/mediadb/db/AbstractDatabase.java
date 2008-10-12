package com.baseoneonline.java.mediadb.db;


public abstract class AbstractDatabase {
	
	public abstract void storeDatabase();
	public abstract void loadDatabase();
	
	public abstract Item getItem(int index);
	public abstract boolean contains(Item item);
	public abstract void addItem(Item item);
	public abstract void removeItem(Item item);
	
}
