package com.baseoneonline.java.tools;

import java.io.File;

public interface FileVisitor {

	public static int FILES_ONLY = 0, DIRECTORIES_ONLY = 1, FILES_AND_DIRS = 2;

	public void visit(File f);
}
