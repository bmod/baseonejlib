package com.baseoneonline.java.mediadb.util;

import java.io.File;
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
		
	public static boolean isMovieType(File f) {
		for (String ext : MOVIE_TYPES) {
			if (f.getName().endsWith(ext)) return true;
		}
		return false;
	}
	
	public static boolean isMusicType(File f) {
		for (String ext : MUSIC_TYPES) {
			if (f.getName().endsWith(ext)) return true;
		}
		return false;
	}
	
	public static boolean isImageType(File f) {
		for (String ext : IMAGE_TYPES) {
			if (f.getName().endsWith(ext)) return true;
		}
		return false;
	}

		
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
