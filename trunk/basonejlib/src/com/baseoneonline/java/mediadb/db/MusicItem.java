package com.baseoneonline.java.mediadb.db;

import java.io.File;

import entagged.audioformats.Tag;

public class MusicItem extends MediaItem {
	
	private Tag tag;
	
	
	public MusicItem(File f) {
		super(f);
	}
	
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public Tag getTag() {
		return tag;
	}
	
}
