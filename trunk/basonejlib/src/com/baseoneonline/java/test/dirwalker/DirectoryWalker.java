package com.baseoneonline.java.test.dirwalker;

import java.io.File;
import java.io.FileFilter;

public class DirectoryWalker {

	private FileFilter filter;
	private final FileOperation op;
	
	public DirectoryWalker(FileOperation op) {
		this.op = op;
	}
	
	public void setFileFilter(final FileFilter flt) {
		if (null == flt) {
			this.filter = null;
		} else {
			this.filter = new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if (pathname.isDirectory()) return true;
					return flt.accept(pathname);
				}
			};
		}
	}
	
	public void walk(File dir) {
		if (null == filter) {
			doWalk(dir);
		} else {
			doWalkFilter(dir);
		}
	}
	
	private void doWalk(File dir) {
		if (dir.isDirectory()) {
			for (File f : dir.listFiles()) {
				doWalk(f);
			}
		} else {
			op.operate(dir);
		}
	}
	
	private void doWalkFilter(File dir) {
		if (dir.isDirectory()) {
			for (File f : dir.listFiles(filter)) {
				doWalkFilter(f);
			}
		} else {
			op.operate(dir);
		}
	}
	
	
	
	

}
