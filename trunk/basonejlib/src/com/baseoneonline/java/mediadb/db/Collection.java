package com.baseoneonline.java.mediadb.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Collection {
	
	
	private final ArrayList<MediaItem> data = new ArrayList<MediaItem>();
	private static Collection instance;
	
	private Collection() {
		
	}
	
	public void add(MediaItem record) {
		for (MediaItem r : data) {
			if (r.filename == record.filename) {
				return;
			}
		}
		data.add(record);
	}
	
	

	public static Collection getInstance() {
		if (null == instance) instance = new Collection();
		return instance;
	}

	public void add(int arg0, MediaItem arg1) {
		// TODO Auto-generated method stub
		
	}

	public boolean addAll(java.util.Collection<? extends MediaItem> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addAll(int arg0,
			java.util.Collection<? extends MediaItem> arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsAll(java.util.Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public MediaItem get(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int indexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public Iterator<MediaItem> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public int lastIndexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ListIterator<MediaItem> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public ListIterator<MediaItem> listIterator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public MediaItem remove(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean removeAll(java.util.Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean retainAll(java.util.Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public MediaItem set(int arg0, MediaItem arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<MediaItem> subList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
