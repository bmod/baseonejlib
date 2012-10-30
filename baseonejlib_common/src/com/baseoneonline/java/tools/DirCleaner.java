package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import com.baseoneonline.java.tools.FileExensionFilter;

public class DirCleaner {

	public static void main(final String[] args) throws Exception {

		final String[] notTrash = { "mp3", "m4a", "flac", "ogg", "wma" };

		final CleanerWalker cleaner = new CleanerWalker();
		cleaner.setKeepFilter(new FileExensionFilter(notTrash));
		final ArrayList<File> results = new ArrayList<File>();
		cleaner.walk(new File("c:/music_unsorted"), results);

		System.out.println("DOne");
	}
}

class NotFileFilter implements FileFilter {

	private final FileFilter filter;

	public NotFileFilter(final FileFilter filter) {
		this.filter = filter;
	}

	@Override
	public boolean accept(final File pathname) {
		return !filter.accept(pathname);
	}
}