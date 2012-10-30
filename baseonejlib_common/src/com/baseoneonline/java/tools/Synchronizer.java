package com.baseoneonline.java.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Synchronizer {

	public static void main(final String[] args) throws Exception {

		Synchronizer.cleanDirectory(new File("c:/music_unsorted"));

		System.out.println("DOne");
	}

	private static void cleanDirectory(final File dir) throws Exception {
		final String[] notTrash = { "mp3", "m4a", "flac", "ogg", "wma" };
		final CleanerWalker cleaner = new CleanerWalker();
		cleaner.setKeepFilter(new FileExensionFilter(notTrash));
		final List<File> results = new LoggingList<File>();
		cleaner.walk(dir, results);
	}
}

class LoggingList<T> extends ArrayList<T> {
	@Override
	public boolean add(final T e) {
		System.out.println("Adding: " + e);
		return super.add(e);
	};
}