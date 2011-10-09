package com.baseoneonline.java.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class FileUtils
{

	/**
	 * Read a file into a {@link String} and return it. Will not throw errors
	 * but log them instead.
	 * 
	 * @param f
	 *            The file to be read.
	 * @return {@link String} containing the contents of the file or null if the
	 *         file could not be read.
	 */
	public static String readFile(final File f)
	{
		try
		{
			final FileReader reader = new FileReader(f);
			final StringBuffer buf = new StringBuffer();
			int c;
			while ((c = reader.read()) != -1)
			{
				buf.append((char) c);
			}
			reader.close();
			return buf.toString();
		} catch (final FileNotFoundException e)
		{
			Logger.getLogger(StringUtils.class.getName()).warning(
					"File not found: " + f.getAbsolutePath());
		} catch (final IOException e)
		{
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
	public static void writeFile(final File f, final String s)
	{
		try
		{
			final FileWriter writer = new FileWriter(f);
			writer.write(s);
			writer.flush();
			writer.close();
		} catch (final IOException e)
		{
			Logger.getLogger(StringUtils.class.getName()).warning(
					"IO Exception while writing: " + f.getAbsolutePath());
		}
	}

	private static char[] ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!()-_.: "
			.toCharArray();

	public static String removeIllegalChars(final String filename,
			final boolean allowSeparators)
	{
		final StringBuffer buf = new StringBuffer();
		final char[] fname = filename.toCharArray();
		for (final char fc : fname)
		{
			if (allowSeparators && (fc == '/' || fc == '\\'))
			{
				buf.append(fc);
			} else
			{
				for (final char c : ALLOWED_CHARS)
				{
					if (fc == c)
					{
						buf.append(c);
						break;
					}
				}
			}
		}
		return buf.toString();
	}

	public static String readFile(final URL url)
	{
		try
		{
			final BufferedInputStream bin = new BufferedInputStream(
					url.openStream());
			final StringBuffer buf = new StringBuffer();
			int c;
			while ((c = bin.read()) != -1)
			{
				buf.append((char) c);
			}
			return buf.toString();
		} catch (final IOException e)
		{
			Logger.getLogger(FileUtils.class.getName()).severe(
					"Error loading: " + url);
		}
		return null;
	}

}
