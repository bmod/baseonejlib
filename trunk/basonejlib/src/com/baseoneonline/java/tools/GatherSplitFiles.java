package com.baseoneonline.java.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GatherSplitFiles {

	public static void main(final String[] args) {
		new GatherSplitFiles();
	}

	public GatherSplitFiles() {

		final String path = "/home/bmod/Desktop/tmp/";

		final File dir = new File(path);

		final List<File> files = new ArrayList<File>();

		for (final File f : dir.listFiles()) {
			files.add(f);
		}

		Collections.sort(files);

		for (final File f : files) {
			trace(f.getName());
		}

	}

	private void trace(final Object o) {
		System.out.println(o);
	}

}
