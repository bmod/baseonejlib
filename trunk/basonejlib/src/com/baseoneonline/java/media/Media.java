package com.baseoneonline.java.media;

import java.io.File;

import com.baseoneonline.java.tools.FileUtils;
import com.baseoneonline.java.tools.FileVisitor;

public class Media {
	
	
	
	public static void scan(final File dir, final Library lib) {
		FileUtils.visitFiles(dir, FileVisitor.FILES_ONLY, new FileVisitor() {
			@Override
			public void visit(final File f) {
				lib.add(new MediaItem(f));
			}
		});
	}
}
