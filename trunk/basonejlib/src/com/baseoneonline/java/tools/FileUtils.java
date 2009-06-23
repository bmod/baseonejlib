package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class FileUtils {
	
	/**
	 * Read a file into a {@link String} and return it. Will not throw errors
	 * but log them instead.
	 * 
	 * @param f
	 *            The file to be read.
	 * @return {@link String} containing the contents of the file or null if the
	 *         file could not be read.
	 */
	public static String readFile(final File f) {
		try {
			final FileReader reader = new FileReader(f);
			final StringBuffer buf = new StringBuffer();
			int c;
			while ((c = reader.read()) != -1) {
				buf.append((char) c);
			}
			reader.close();
			return buf.toString();
		} catch (final FileNotFoundException e) {
			Logger.getLogger(StringUtils.class.getName()).warning(
					"File not found: " + f.getAbsolutePath());
		} catch (final IOException e) {
			Logger.getLogger(StringUtils.class.getName()).warning(
					"IO Exception while reading file: " + f.getAbsolutePath());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Write a string into a file. If the file exists, it will be overwritten.
	 * 
	 * @param f
	 *            The file to be written.
	 * @param s
	 *            Write this {@link String} to the file.
	 */
	public static void writeFile(final File f, final String s) {
		try {
			final FileWriter writer = new FileWriter(f);
			writer.write(s);
			writer.flush();
			writer.close();
		} catch (final IOException e) {
			Logger.getLogger(StringUtils.class.getName()).warning(
					"IO Exception while writing: " + f.getAbsolutePath());
		}
	}

	public static void visitFiles(final File dir, final int visitMode,
			final FileVisitor visitor) {
		visitFiles(dir, visitMode, true, visitor, null);
	}

	public static void visitFiles(final File dir, final int visitMode,
			final boolean recursive, final FileVisitor visitor,
			final FileFilter filter) {
		final File[] files = dir.listFiles(filter);
		for (final File f : files) {
			if (visitMode == FileVisitor.DIRECTORIES_ONLY && f.isDirectory()) {
				visitor.visit(f);
			} else if (visitMode == FileVisitor.FILES_ONLY && !f.isDirectory()) {
				visitor.visit(f);
			} else {
				visitor.visit(f);
			}
			if (recursive && f.isDirectory()) {
				visitFiles(f, visitMode, recursive, visitor, filter);
			}
		}
	}

}
