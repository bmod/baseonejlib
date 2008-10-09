package com.baseoneonline.java.mediadb.util;

import java.util.ArrayList;

public class SupportedTypes {
	
	public static final String[] MOVIE_TYPES = {
		"mov",
		"avi",
		"flv",
		"mpg"
	};
	
	public static final String[] MUSIC_TYPES = {
		"mp3",
		"mpa"
	};
	
	public static final String[] IMAGE_TYPES = {
		"jpg",
		"jpeg",
		"tif",
		"tga"
	};
	
	public static final String[][] ALL_TYPES = {
		IMAGE_TYPES,
		MUSIC_TYPES,
		MOVIE_TYPES
	};
		
		
	public static String[] getAllSupportedExtensions() {
		ArrayList<String> out = new ArrayList<String>();
		for (String[] type : ALL_TYPES) {
			for (String t : type) {
				out.add(t);
			}
		}
		return out.toArray(new String[out.size()]);
	}
	
		
}
