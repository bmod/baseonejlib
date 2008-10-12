package com.baseoneonline.java.mediadb.db;

import java.util.List;
import java.util.Vector;

public class Collection {


	private final List<Item> data = new Vector<Item>();
	private static Collection instance;

	private Collection() {

	}

	public void add(final Item record) {

		data.add(record);
	}

	public Item get(final int index) {
		return data.get(index);
	}

	public boolean contains(final Item item) {
		for (final Item m : data) {
			if (m.getFile().equals(item.getFile())) {
				return true;
			}
		}
		return false;
	}

	public int size() {
		return data.size();
	}

	public static Collection getInstance() {
		if (null == Collection.instance) {
			Collection.instance = new Collection();
		}
		return Collection.instance;
	}

}
