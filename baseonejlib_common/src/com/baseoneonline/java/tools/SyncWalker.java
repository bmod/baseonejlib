package com.baseoneonline.java.tools;

import java.io.File;
import java.util.List;

public class SyncWalker extends DirectoryWalker<FileOperation> {

	private final File targetDir;
	private final File sourceDir;

	public SyncWalker(final File sourceDir, final File targetDir) {
		this.targetDir = targetDir;
		this.sourceDir = sourceDir;
	}

	@Override
	public boolean visit(final File f, final List<FileOperation> results)
			throws Exception {

		return true;
	}

	private File toTarget(final File f) {

	}

}
