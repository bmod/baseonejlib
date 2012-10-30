package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public abstract class DirectoryWalker<T> {

	public enum Mode {
		FilesOnly, DirsOnly, FilesAndDirs
	}

	private Mode visitMode = Mode.FilesOnly;
	private boolean recursive = true;
	private FileFilter fileFilter;
	private FileFilter filter;

	public DirectoryWalker() {
	}

	public FileFilter getFileFilter() {
		return fileFilter;
	}

	public void setFileFilter(final FileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}

	public Mode getVisitMode() {
		return visitMode;
	}

	public void setVisitMode(final Mode visitMode) {
		this.visitMode = visitMode;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(final boolean recursive) {
		this.recursive = recursive;
	}

	/**
	 * @param f
	 *            The current file.
	 * @param results
	 *            Store any results here.
	 * @return true if the walker should continue to drill down in this
	 *         directory. Obviously, this does not apply to files.
	 * @throws Exception
	 */
	public abstract boolean visit(File f, List<T> results) throws Exception;

	public void walk(final File dir, final List<T> results) throws Exception {
		final File[] files = dir.listFiles(getFilter());
		for (final File f : files) {
			if (!f.getName().startsWith(".")) {
				boolean drill = false;
				if (visitMode == Mode.DirsOnly && f.isDirectory()) {
					if (visit(f, results))
						drill = true;
				} else if (visitMode == Mode.FilesOnly && f.isFile()) {
					if (visit(f, results))
						drill = true;
				}
				if (drill && recursive && f.isDirectory()) {
					walk(f, results);
				}
			}
		}
	}

	private FileFilter getFilter() {
		if (null == filter) {
			filter = new FileFilter() {
				@Override
				public boolean accept(final File f) {
					if (f.isDirectory())
						return true;
					if (null != fileFilter)
						return fileFilter.accept(f);
					return true;
				}
			};
		}
		return filter;
	}

}
