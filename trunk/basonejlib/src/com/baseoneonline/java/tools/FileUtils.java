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
	public static String readFile(File f) {
		try {
			FileReader reader = new FileReader(f);
			StringBuffer buf = new StringBuffer();
			int c;
			while ((c = reader.read()) != -1) {
				buf.append((char) c);
			}
			reader.close();
			return buf.toString();
		} catch (FileNotFoundException e) {
			Logger.getLogger(StringUtils.class.getName()).warning(
				"File not found: " + f.getAbsolutePath());
		} catch (IOException e) {
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
	public static void writeFile(File f, String s) {
		try {
			FileWriter writer = new FileWriter(f);
			writer.write(s);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			Logger.getLogger(StringUtils.class.getName()).warning(
				"IO Exception while writing: " + f.getAbsolutePath());
		}
	}
	
	public static void scanDirectory(File dir, FileScanClient client, boolean recursive, FileFilter filter) {
		File[] files = dir.listFiles(filter);
		for (File f : files) {
			client.operate(f);
			if (recursive && f.isDirectory()) {
				scanDirectory(f, client, recursive, filter);
			}
		}
	}
	
	public static interface FileScanClient {
		public void operate(File f);
	}
	
}
