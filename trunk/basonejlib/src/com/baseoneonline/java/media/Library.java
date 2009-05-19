package com.baseoneonline.java.media;

public abstract class Library {
	public abstract void add(MediaItem item);

	public abstract MediaItem get(int index);

	public abstract int size();
}
