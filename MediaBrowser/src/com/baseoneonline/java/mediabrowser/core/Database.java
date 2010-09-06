package com.baseoneonline.java.mediabrowser.core;

import java.util.List;


public interface Database {
	public List<MediaFile> getMediaFiles();

	public void storeMediaFiles(List<MediaFile> chunks);
}
