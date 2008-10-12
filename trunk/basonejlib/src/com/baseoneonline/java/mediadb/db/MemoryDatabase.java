package com.baseoneonline.java.mediadb.db;

import java.util.ArrayList;
import java.util.List;

public class MemoryDatabase extends AbstractDatabase {

	private final List<Item> data = new ArrayList<Item>();
	
	public MemoryDatabase() {
		
	}
	
	
	@Override
	public void addItem(Item item) {
		data.add(item);
	}

	@Override
	public boolean contains(Item item) {
		return data.contains(item); 
	}

	@Override
	public Item getItem(int index) {
		return data.get(index);
	}

	@Override
	public void loadDatabase() {
		// Don't!
	}

	@Override
	public void removeItem(Item item) {
		data.remove(item);
	}

	@Override
	public void storeDatabase() {
		// Don't!
	}

}
