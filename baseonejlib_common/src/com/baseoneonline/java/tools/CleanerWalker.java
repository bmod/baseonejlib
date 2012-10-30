package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

class CleanerWalker extends DirectoryWalker<File> {

	private FileFilter keepFilter;

	public CleanerWalker() {
		setVisitMode(Mode.DirsOnly);
	}

	public void setKeepFilter(final FileFilter filter) {
		keepFilter = filter;
	}

	@Override
	public boolean visit(final File f, final List<File> results)
			throws Exception {
		if (isEmpty(f)) {
			results.add(f);
			return true;
		}
		return true;
	}

	private boolean isKeepFile(final File f) {
		if (keepFilter == null)
			return true;
		return keepFilter.accept(f);
	}

	private boolean isEmpty(final File dir) throws IOException {
		if (dir == null)
			throw new IOException("Directory does not exist: " + dir);
		if (!dir.isDirectory())
			throw new IOException("Not a directory: " + dir);

		for (final File f : dir.listFiles()) {
			if (f.isDirectory()) {
				if (!isEmpty(f))
					return false;
			}
			if (isKeepFile(f))
				return false;

		}
		return true;
	}

}