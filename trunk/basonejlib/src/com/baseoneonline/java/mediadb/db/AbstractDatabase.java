package com.baseoneonline.java.mediadb.db;


public abstract class AbstractDatabase {
	
	public abstract void storeDatabase();
	public abstract void loadDatabase();
	
	public abstract MediaItem getItem(int index);
	public abstract boolean contains(MediaItem item);
	public abstract void addItem(MediaItem item);
	public abstract void removeItem(MediaItem item);
	
}
