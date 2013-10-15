package com.baseoneonline.java.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;

public class FileUtils {
	private static final int TEMP_DIR_ATTEMPTS = 10000;

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

	private static char[] ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!()-_.: "
			.toCharArray();

	public static String removeIllegalChars(final String filename,
			final boolean allowSeparators) {
		final StringBuffer buf = new StringBuffer();
		final char[] fname = filename.toCharArray();
		for (final char fc : fname) {
			if (allowSeparators && (fc == '/' || fc == '\\')) {
				buf.append(fc);
			} else {
				for (final char c : ALLOWED_CHARS) {
					if (fc == c) {
						buf.append(c);
						break;
					}
				}
			}
		}
		return buf.toString();
	}

	public static String readFile(final URL url) {
		try {
			final BufferedInputStream bin = new BufferedInputStream(
					url.openStream());
			final StringBuffer buf = new StringBuffer();
			int c;
			while ((c = bin.read()) != -1) {
				buf.append((char) c);
			}
			return buf.toString();
		} catch (final IOException e) {
			Logger.getLogger(FileUtils.class.getName()).severe(
					"Error loading: " + url);
		}
		return null;
	}

	/**
	 * @param f
	 * @param directory
	 * @return True if the specified file is inside the specified directory
	 */
	public static boolean isChildOf(final File f, final File directory) {
		if (!directory.isDirectory())
			return false;

		File current = f;
		while (true) {
			final File parent = current.getParentFile();
			if (null == parent)
				return false;
			if (current.getParentFile().equals(directory))
				return true;

			current = parent;
		}

	}

	/**
	 * Recursively searches through a directory and returns a list of empty
	 * directories.
	 * 
	 * @param directory
	 * @return
	 */
	public static List<File> findEmpty(final File directory,
			final DisposableFilter filter) {
		final List<File> stor = new ArrayList<File>();
		FileUtils.findEmpty(directory, stor, filter);
		return stor;
	}

	public static void findEmpty(final File parent, final List<File> stor,
			final DisposableFilter filter) {
		if (FileUtils.isEmpty(parent, filter)) {
			stor.add(parent);
			return;
		}

		for (final File dir : parent.listFiles()) {

			if (dir.isDirectory()) {
				if (FileUtils.isEmpty(dir, filter))
					stor.add(dir);
				else
					FileUtils.findEmpty(dir, stor, filter);

			}
		}
	}

	/**
	 * Recursively searches for files in the provided directory to see if it's
	 * empty.
	 * 
	 * 
	 * @param dir
	 *            The directory to search. If a file was provided, this method
	 *            will return <code>false</code>.
	 * @param disposableFilter
	 *            Will determine whether a file is considered disposable. If the
	 *            filter accepts a file, it will ignore the file. For example,
	 *            when the filter accepts all files in a folder, the folder will
	 *            be considered empty.
	 * @return <code>true</code> if the provided directory contains no files or
	 *         only empty directories.
	 */
	public static boolean isEmpty(final File dir,
			final DisposableFilter disposableFilter) {
		if (dir.isFile())
			return false;

		for (final File f : dir.listFiles()) {
			if (f.isFile() && !disposableFilter.isDisposable(f))
				return false;

			else if (f.isDirectory() && !FileUtils.isEmpty(f, disposableFilter))
				return false;
		}

		return true;
	}

	public static interface DisposableFilter {
		/**
		 * For use in {@link FileUtils#isEmpty(File, FileFilter)} and
		 * {@link FileUtils#findEmpty(File)}. Determines whether a file should
		 * be ignored when looking for empty directories.
		 * 
		 * @param f
		 *            The file check for ignoring.
		 * @return True if this file is considered disposable.
		 */
		public boolean isDisposable(File f);
	}

	public static interface FileListener {
		public void onFile(File f);
	}

	public static File canonical(final Object... args) throws IOException {
		final String[] elements = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			final Object o = args[i];
			if (o instanceof Character) {
				elements[i] = Character.toString((Character) o);
			} else if (o instanceof String) {
				elements[i] = ((String) o).trim();
			} else if (o instanceof File) {
				elements[i] = ((File) o).getAbsolutePath();
			}
		}
		return new File(StringUtils.join(elements, "")).getCanonicalFile();
	}

	public static File relative(final File f, final File base) {
		return new File(f.toURI().relativize(base.toURI()).getPath());
	}

	public static String replaceExtension(final String filename,
			final String rep) {
		return FilenameUtils.removeExtension(filename) + "." + rep;
	}

	public static File file(final String... args) {
		return new File(StringUtils.join(args, "/"));
	}

	public static File createTempDir(final File baseDir) {
		final String baseName = System.currentTimeMillis() + "-";

		for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
			final File tempDir = new File(baseDir, baseName + counter);
			if (tempDir.mkdir()) {
				return tempDir;
			}
		}
		throw new IllegalStateException("Failed to create directory within "
				+ TEMP_DIR_ATTEMPTS + " attempts (tried " + baseName + "0 to "
				+ baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
	}

	public static File replaceExtension(File file, String ext) {
		return new File(replaceExtension(file.getAbsolutePath(), ext));
	}

}
