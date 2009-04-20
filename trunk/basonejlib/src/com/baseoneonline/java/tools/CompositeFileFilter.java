package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class CompositeFileFilter implements FileFilter {

	private List<FileFilter> filters = new ArrayList<FileFilter>();

	public CompositeFileFilter() {

	}

	public CompositeFileFilter(List<FileFilter> filters) {
		this.filters = filters;
	}

	public void addFilter(FileFilter filter) {
		filters.add(filter);
	}

	@Override
	public boolean accept(File pathname) {
		for (FileFilter f : filters) {
			if (f.accept(pathname))
				return true;
		}
		return false;
	}

}
